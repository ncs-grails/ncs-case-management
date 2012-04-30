package edu.umn.ncs

class BatchService {

    static transactional = true

	def mySqlService

	def deleteBatches(Set<Integer> batchIds) {
		
		def errors = []
		def batchesDeleted = []
		def batchIdList = []
		batchIdList.addAll(batchIds)
		
		def batchInstanceList = Batch.getAll(batchIdList)
		def batchesToDelete = findBatchesToDelete(batchInstanceList)
		def fkRefs = mySqlService.findForeignKeyReferences('batch')

		Batch.withTransaction {
			// Before we delete these, we have to remove all references to them
			batchesToDelete.each{ batchInstance ->
				if (batchInstance) {
					// Remove master batch references
					batchInstance.master = null
					batchInstance.save()

					// delete records from foreign key tables
					mySqlService.deleteForignKeyReferences(fkRefs, batchInstance.id)
				}
			}
		}

		// Now we can delete them.
		batchesToDelete.each{ batchInstance ->
			if (batchInstance) {
				// Delete Item
				def batchId = batchInstance.id
				try {
					batchInstance.delete()
					batchesDeleted.add(batchId)
				} catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e){
					def anError = "MySQL Integrity Contstrain violation for batch, ${batchInstance}])}"
					log.error anError
					errors.add(anError)
				} catch(org.springframework.dao.DataIntegrityViolationException e){
					def anError = "Grails data integrity violation for batch, ${batchInstance}])}"
					log.error anError
					errors.add(anError)
				}
			}
		}

		return [ errors: errors, batchesDeleted: batchesDeleted ]
	}

	/** This finds tracked batches and their children
	*
	*/
	Set<Batch> findBatchesToDelete(Collection<Batch> batches) {
		Set<Batch> batchSet = batches as Set
		findBatchesToDelete(batchSet)
	}

	Set<Batch> findBatchesToDelete(Set<Batch> batches) {
		def children = [] as Set

		if (batches != null) {
			// First find all the child batches of this set of batches
			batches.each{
				if (it) { children.addAll( Batch.findAllByMaster(it) ) }
			}
			// then go find any children of the children (recurse)
			if (children) {
				children.addAll(findBatchesToDelete(children))
			}
			// Then add the main set
			children.addAll(batches)
		}
		// now return the full list of batches in the chain
		return children
	}
}
