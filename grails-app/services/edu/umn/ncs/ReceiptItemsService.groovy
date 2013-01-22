package edu.umn.ncs
import grails.converters.*
import org.joda.time.*
import grails.plugins.springsecurity.Secured

class ReceiptItemsService {
	
	static transactional = true
	
	def springSecurityService
	def debug = true

	def receiptItem(itemId, receivedDate, result){
	
		if (debug) { 
			log.debug ""
			log.debug "==> Begin RECEIPT-ITEMS SERVICE > RECEIPT-ITEM"
			log.debug "==> param itemId: ${itemId}"
			log.debug "==> param receivedDate: ${receivedDate}"
			log.debug "==> param result: ${result}"
		}
		
		def username = springSecurityService.principal.getUsername()
		def appName = "ncs-case-management"
		if (debug) { 
			log.debug "==> username: ${username}"
			log.debug "==> appName: ${appName}"
		}

		// determine instrument RESULT based on instrument type
		// specifically, if instrument type is "Transition Permission Opt Out Card" (instrument_id = 106), then result is "opt out", otherwise "recevied"
		def trackedItemInstance = TrackedItem.get(itemId)
		def batchId = trackedItemInstance.batchId
		def batchInstance = Batch.get(batchId)
		def batchInstrumentInstance = batchInstance.primaryBatchInstrument
		def instrumentId = batchInstrumentInstance.instrumentId 
		def defaultTrackedItemResult
		if ( instrumentId == 106 ) {
			defaultTrackedItemResult = Result.findByName('opt out')
		} else {
			defaultTrackedItemResult = Result.findByName('received')
		}
		if (debug) { 
			log.debug "==> trackedItemInstance (pulled from TrackedItem.get(itemId)): ${trackedItemInstance}" 
			log.debug "==> batchId (pulled from trackedItemInstance.batchId): ${batchId}" 
			log.debug "==> batchInstance (pulled from Batch.get(batchId)): ${batchInstance}" 
			log.debug "==> batchInstrumentInstance (pulled from batchInstance.primaryBatchInstrument): ${batchInstrumentInstance}" 
			log.debug "==> instrumentId (pulled from batchInstrumentInstance.instrumentId): ${instrumentId}" 
			log.debug "==> defaultTrackedItemResult (based on InstrumentId): ${defaultTrackedItemResult}" 
		}		

		// determine if tracked item exists in db.  If so, process item.
		if (!trackedItemInstance) {

			if (debug) { log.debug "==> No trackedItemInstance found (based on itemId)" }

			result.success = false
			if (debug) { log.debug "==> result.success: ${result.success}" 	}

			result.errorText = "   Tracked Item not found."

		} else {

			if (debug) { log.debug "==> trackedItemInstance exists (based on itemId)" }

			// get tracked item info (id, name, study)
			result.trackedItemId = trackedItemInstance.id
			result.instrumentName = trackedItemInstance.batch.primaryInstrument.toString()
			result.studyName =  trackedItemInstance.batch.primaryInstrument.study.toString()
			if (debug) { 
				log.debug "==> result.trackedItemId (pulled from trackedItemInstance.id): ${result.trackedItemId}"
				log.debug "==> result.instrumentName (pulled from trackedItemInstance.batch.primaryInstrument.toString()): ${result.instrumentName}"
				log.debug "==> result.studyName (pulled from trackedItemInstance.batch.primaryInstrument.study.toString()): ${result.studyName}"
				log.debug "==> trackedItemInstance.result: ${trackedItemInstance.result}"
			}

			// tracked item already has a result
			if (trackedItemInstance.result) {

				if (debug) { 
					log.debug "==> if (trackedItemInstance.result) = TRUE" 
					log.debug "==> if (trackedItemInstance?.result?: ${trackedItemInstance?.result}" 
					log.debug "==> if (trackedItemInstance?.result?.result?: ${trackedItemInstance?.result?.result}" 
					log.debug "==> if (trackedItemInstance?.result?.result?.name: ${trackedItemInstance?.result?.result?.name}" 
				}

				// leave result as is, if tracked item is:
				// of instrument type 106 or
				// other than instrument type 106, but with result of "received"
				if ( instrumentId == 106 || instrumentId != 106 && trackedItemInstance?.result?.result?.name == Result.findByName('received') ) {

					if (debug) { log.debug "if ( (instrumentId == 106) || (instrumentId != 106 && trackedItemInstance?.result?.result?.name == Result.findByName('received') )" }
					result.success = false
					result.resultName = trackedItemInstance.result.result.name
					result.resultDate = trackedItemInstance.result.receivedDate
					result.errorText = "Tracked Item already receipted on ${result.resultDate} with result of ${result.resultName}"
					if (debug) { 
						log.debug "result.success: ${result.success}"
						log.debug "result.resultName (pulled from trackedItemInstance.result.result.name): ${result.resultName}"
						log.debug "result.resultDate (pulled from trackedItemInstance.result.receivedDate): ${result.resultDate}"
						log.debug "result.errorText: ${result.errorText}"
					}

				// for tracked item (other than instrument_id 106), with result other than "received", receipt item as "received"
				} else if ( instrumentId != 106 && trackedItemInstance?.result?.result?.name != Result.findByName('received') ) {

					if (debug) { log.debug "if ( instrumentId != 106 && trackedItemInstance?.result?.result?.name != Result.findByName('received') )" }

					// Update result to RECEIVED
					trackedItemInstance.result.result = defaultTrackedItemResult
					trackedItemInstance.result.receivedDate = receivedDate
					trackedItemInstance.result.userCreated = username
					trackedItemInstance.result.appCreated = appName
					if (debug) { 
						log.debug "trackedItemInstance.result.result (pulled from received): ${trackedItemInstance.result.result}"
						log.debug "trackedItemInstance.result.receivedDate (pulled from receivedDate): ${trackedItemInstance.result.receivedDate}"
						log.debug "trackedItemInstance.result.userCreated (pulled from username): ${trackedItemInstance.result.userCreated}"
						log.debug "trackedItemInstance.result.appCreated (pulled from appName): ${trackedItemInstance.result.appCreated}"
					}

					// Transact old result
					// call auditLog(className, eventName, newValue, oldValue, persistedObjectId, propertyName)
					if ( !trackedItemInstance.hasErrors() && trackedItemInstance.save(flush: true) ) {

						if (debug) { log.debug "if (!trackedItemInstance.hasErrors() && trackedItemInstance.save(flush: true)) = TRUE" }

						result.success = true
						result.resultName = trackedItemInstance.result.result.name
						result.resultDate = new LocalDate(trackedItemInstance.result.receivedDate).toString('MM-dd-yyyy')
						if (debug) { 
							log.debug "result.success: ${result.success}"
							log.debug "result.resultName (based on trackedItemInstance.result.result.name): ${result.resultName}"
							log.debug "result.resultDate (based on new LocalDate(trackedItemInstance.result.receivedDate).toString('MM-dd-yyyy')): ${result.resultDate}"
						}

					} else {

						if (debug) { log.debug "if (!trackedItemInstance.hasErrors() && trackedItemInstance.save(flush: true)) = FALSE" }

						result.success = false
						result.errorText = "Unable to update Tracked Item result"	
						if (debug) { 
							log.debuig "result.success: ${result.success}" 
							log.debuig "result.errorText: ${result.errorText}" 
						}

						trackedItemInstance.errors.each{ log.debug "   error: ${it}" }

					}

				} //if ( (instrumentId == 106) || (instrumentId != 106 && trackedItemInstance?.result?.result?.name == Result.findByName('received') )

			// tracked Item does not have result, therefore create result instance
			} else {

				if (debug) { log.debug "==> if (trackedItemInstance.result) = FALSE" }

				trackedItemInstance.result = new ItemResult(
					result:defaultTrackedItemResult,
					userCreated: username,
					appCreated: appName,
					receivedDate: receivedDate)
				if (debug) { log.debug "==> new trackedItemInstance.result: ${trackedItemInstance.result}" }

				if ( !trackedItemInstance.hasErrors() && trackedItemInstance.save(flush:true) ) {

					if (debug) { log.debug "==> if ( !trackedItemInstance.hasErrors() && trackedItemInstance.save(flush:true) ) = TRUE" }

					result.success = true
					result.resultName = trackedItemInstance.result.result.name
					result.resultDate = new LocalDate(trackedItemInstance.result.receivedDate).toString('MM-dd-yyyy')
					if (debug) { 
						log.debug "result.success: ${result.success}"
						log.debug "new result.resultName (pulled from trackedItemInstance.result.result.name): ${result.resultName}"
						log.debug "new result.riesultDate (pulled fromnew LocalDate(trackedItemInstance.result.receivedDate).toString('MM-dd-yyyy') ): ${result.resultDate}"
					}

				} else {

					if (debug) { log.debug "==> if ( !trackedItemInstance.hasErrors() && trackedItemInstance.save(flush:true) ) = FALSE" }

					result.success = false
					result.errorText = "Unable to save result for Tracked Item"
					if (debug) { 
						log.debug "result.success: ${result.success}" 
						log.debug "result.errorText: ${result.errorText}" 
					} 

					trackedItemInstance.errors.each{ log.debug "   error: ${it}" }
				}

			 } // if (trackedItemInstance.result) {

		} // if (!trackedItemInstance) {
		if (debug) { 
			log.debug "==> End RECEIPT-ITEMS SERVICE > RECEIPT-ITEM"
			log.debug ""
		}
		return result

	 } // def receiptItem(itemId, receivedDate, result){

}
