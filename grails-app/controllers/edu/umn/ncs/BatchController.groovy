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

		// if the batch ID was passed, and a date was passed...
		if (batchId && referenceDate) {

			if (batchId[0].matches('b') || batchId[0].matches('B')) {
				batchId = batchId.toLowerCase().replace('b','')

				try {
					def batchInstance = Batch.get(batchId)

					if (batchInstance) {
						// update batch mail date
						batchInstance.mailDate = referenceDate
						// TODO: we should probably update the provenance fields here
						batchInstance.save(flush:true)
						flash.message = "${batchInstance.primaryInstrument.study} ${batchInstance.primaryInstrument} generated on ${batchInstance.dateCreated} has been updated."

					} else {
						flash.message = "Batch not found: ${batchId}"
					}

				} catch (Exception) {
					flash.message = "Invalid Batch ID: ${batchId}"
				}

			} else {
				println "fail..."
				flash.message = "Invalid Batch ID ${batchId}"
			}
		}

		// choose today if no reference date was specified
		if ( ! referenceDate ) {
			referenceDate = new Date()
			referenceDate.hours = 0
			referenceDate.minutes = 0
			referenceDate.seconds = 0
		}

		// create a criteria for querying
		def c = Batch.createCriteria()

		// all batches that have a maildate that matches today.
        def sentBatchInstanceList = c.list{
			between("mailDate", referenceDate, referenceDate + 1)
		}

		c = Batch.createCriteria()
		// all batches that don't have a mail date
        def unsentBatchInstanceList = c.list{
			and {
				isNull("mailDate")
				between("dateCreated", referenceDate - 14, referenceDate + 1)
			}
		}
		[ referenceDate: referenceDate, 
			sentBatchInstanceList:sentBatchInstanceList,
			unsentBatchInstanceList:unsentBatchInstanceList ]
	}

    def listByDate = {
        def listByDateSelect = params?.listByDateSelect

        println "params.batchListDate -> ${listByDateSelect}"

        if (!listByDateSelect) {
            listByDateSelect = new Date()
        }

        // create a criteria for querying
        def c = Batch.createCriteria()

        //All batches for the passed date month that do not have mail date
        /*def unsentBatchInstanceList = c.list{
            and {
                isNull("mailDate")
             ???   between("dateCreated", listByDateSelect, listByDateSelect.addMonth(1))
            }
        }*/

        [ listByDateSelect: listByDateSelect ]
    }
	
    def noneFound = {}
}
