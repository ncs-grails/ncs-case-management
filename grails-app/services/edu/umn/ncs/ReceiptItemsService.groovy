package edu.umn.ncs
import grails.converters.*
import org.joda.time.*
import grails.plugins.springsecurity.Secured

class ReceiptItemsService {

    static transactional = true
	def springSecurityService
	
	def receiptItem(itemId, receivedDate, result){

		def username = springSecurityService.principal.getUsername()
		def appName = "ncs-case-management"
		def received = Result.findByName('received')
		def trackedItemInstance = TrackedItem.get(itemId)

		if (!trackedItemInstance) {
			result.success = false
			result.errorText = "   Item not found."
		} else {
			// get received status and received date
			result.trackedItemId = trackedItemInstance.id
			result.instrumentName = trackedItemInstance.batch.primaryInstrument.toString()
			result.studyName =  trackedItemInstance.batch.primaryInstrument.study.toString()

			if (trackedItemInstance.result ) {
				if (trackedItemInstance?.result?.result?.name == received?.name) {
					// item has been recepted already.  See what it was
					result.resultDate = trackedItemInstance.result.receivedDate
					result.resultName = trackedItemInstance.result.result.name

					result.success = false
					result.errorText = "Item already receipted ${result.resultDate}."
				} else {

					// Update result
					trackedItemInstance.result.result = received
					trackedItemInstance.result.userCreated = username
					trackedItemInstance.result.appCreated = appName
					trackedItemInstance.result.receivedDate = receivedDate
					// Transact old result
					// call auditLog(className, eventName, newValue, oldValue, persistedObjectId, propertyName)
					if (!trackedItemInstance.hasErrors() && trackedItemInstance.save(flush: true)){
						result.success = true
						result.resultDate = new LocalDate(trackedItemInstance.result.receivedDate).toString('MM-dd-yyyy')
						result.resultName = trackedItemInstance.result.result.name
					} else {

					trackedItemInstance.errors.each{
						log.debug "   error: ${it}"
					}
						result.success = false
						result.errorText = "unable to update result"
					}
				}

			} else {

				// tie the result back to the item
				trackedItemInstance.result = new ItemResult(result:received,
				userCreated: username,
				appCreated: appName,
				receivedDate: receivedDate)

				if (!trackedItemInstance.hasErrors() && trackedItemInstance.save(flush:true) ) {
					result.success = true
					result.resultDate = new LocalDate(trackedItemInstance.result.receivedDate).toString('MM-dd-yyyy')
					result.resultName = trackedItemInstance.result.result.name
				} else {

					trackedItemInstance.errors.each{
						log.debug "   error: ${it}"
					}
					result.success = false
					result.errorText = "unable to save result"
				}
			}
		}
	
		return result
	}
}
