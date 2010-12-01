package edu.umn.ncs

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class BatchController {

    def index = { 
        redirect(action:'show',params:params)
    }

    def show = {
        def batchInstance = Batch.get(params.id)

        if (!batchInstance) {
            redirect(action:'noneFound',params:params)
        }
        [batchInstance:batchInstance]
    }

	def entry = {
		// reference date
		def referenceDate = params?.referenceDate

		// look for batch ID
		def batchId = params.id

		if (batchId && referenceDate) {
			if (batchId[0].matches('b') || batchId[0].matches('B')) {
				batchId = batchId.toLowerCase().replace('b','')
				def batchInstance = Batch.get(params.id)

				if (batchInstance) {
					// update batch mail date
					batchInstance.mailDate = referenceDate
					// TODO: we should probably update the provenance fields here
					batchInstance.save(flush:true)
				}
			} else {
				flash.message = "invalid Batch ID"
			}
		}

		// create a criteria for querying
		def c = Batch.createCriteria()

		// all batches that have a maildate that matches today.
        def sentBatchInstanceList = c.listAll{
			between("mailDate", referenceDate, referenceDate + 1)
		}

		// all batches that don't have a mail date
        def unsentBatchInstanceList = c.listAll{
			and {
				isNull("mailDate")
				between("dateCreated", referenceDate - 14, referenceDate + 1)
			}
		}

		// choose today if no reference date was specified
		if ( ! referenceDate ) {
			referenceDate = new Date()
		}
		
		[ referenceDate: referenceDate, 
			batchInstance: batchInstance,
			sentBatchInstanceList:sentBatchInstanceList,
			unsentBatchInstanceList:unsentBatchInstanceList ]

	}
	
    def noneFound = {}
}
