package edu.umn.ncs

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class BatchController {

	def emailService

    def index = { 
        redirect(action:'entry',params:params)
    }

	def sendNightlyReport = {

		// emailService.sendProductionReport(params)
		emailService.sendProductionReport()
		
		redirect(controller:"mainMenu", action:"index")

	}

	def nightlyReport = {
		def referenceDate = params.referenceDate
		def midnight = new LocalTime(0, 0)

		if ( ! referenceDate ) {
			referenceDate = new LocalDate()
		} else {
			referenceDate = new LocalDate(referenceDate)
		}


		def startDate = referenceDate.toDateTime(midnight).toCalendar().getTime()
		def endDate = referenceDate.plusDays(1).toDateTime(midnight).toCalendar().getTime()
		
		def c = Batch.createCriteria()

		def batchInstanceList = c.list{
			gt("dateCreated", startDate)
			lt("dateCreated", endDate)
		}


		[ referenceDate: startDate,
			batchInstanceList: batchInstanceList,
			customizable: true]
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

        println "listByDateSelect before new Date()-> ${listByDateSelect}"

        if (!listByDateSelect) {
            listByDateSelect = new Date()
        }
         println "listByDateSelect + 30-> ${listByDateSelect + 30}"

        // create a criteria for querying
        def c = Batch.createCriteria()

        //All batches for the passed date month that do not have mail date
        def unsentBatchInstanceList = c.list{
            and {

                between("dateCreated", listByDateSelect, listByDateSelect + 30)
            }
        }


        unsentBatchInstanceList.each{
            println "${it.id}"
        }


        [ listByDateSelect: listByDateSelect ]
    }
}
