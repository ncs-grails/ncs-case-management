package edu.umn.ncs

class TrackedItemService {

    static transactional = true

	def mySqlService

	def deleteTrackedItems(Set<Integer> itemIds) {
		
		def errors = []
		def itemsDeleted = []
		def itemIdList = []
		itemIdList.addAll(itemIds)
		
		def trackedItemInstanceList = TrackedItem.getAll(itemIdList)
		def itemsToDelete = findTrackedItemsToDelete(trackedItemInstanceList)
		def fkRefs = mySqlService.findForeignKeyReferences('tracked_item')

		TrackedItem.withTransaction {
			// Before we delete these, we have to remove all references to them
			itemsToDelete.each{ trackedItemInstance ->
				if (trackedItemInstance) {
					// Remove parent item references
					trackedItemInstance.parentItem = null
					trackedItemInstance.save()

					// delete records from foreign key tables
					mySqlService.deleteForignKeyReferences(fkRefs, trackedItemInstance.id)
				}
			}
		}

		// Now we can delete them.
		itemsToDelete.each{ trackedItemInstance ->
			if (trackedItemInstance) {
				// Delete Item
				def itemId = trackedItemInstance.id
				try {
					trackedItemInstance.delete()
					itemsDeleted.add(itemId)
				} catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e){
					def anError = "MySQL Integrity Contstrain violation for item, ${trackedItemInstance}])}"
					log.error anError
					errors.add(anError)
				} catch(org.springframework.dao.DataIntegrityViolationException e){
					def anError = "Grails data integrity violation for item, ${trackedItemInstance}])}"
					log.error anError
					errors.add(anError)
				}
			}
		}

		return [ errors: errors, itemsDeleted: itemsDeleted ]
	}

	/** This finds tracked items and their children
	*
	*/
	Set<TrackedItem> findTrackedItemsToDelete(Collection<TrackedItem> items) {
		Set<TrackedItem> itemSet = items as Set
		findTrackedItemsToDelete(itemSet)
	}

	Set<TrackedItem> findTrackedItemsToDelete(Set<TrackedItem> items) {
		def children = [] as Set

		if (items != null) {
			// First find all the child items of this set of items
			items.each{
				if (it) { children.addAll( TrackedItem.findAllByParentItem(it) ) }
			}
			// then go find any children of the children (recurse)
			if (children) {
				children.addAll(findTrackedItemsToDelete(children))
			}
			// Then add the main set
			children.addAll(items)
		}
		// now return the full list of items in the chain
		return children
	}
}
