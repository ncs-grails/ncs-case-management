package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.orm.auditable.*


@Secured(['ROLE_NCS'])
class TrackedItemController {
	private boolean debug = true
	private static LocalTime midnight = new LocalTime(0,0)

	def authenticateService
	
    def index = { redirect(controller:'mainMenu') }
	
	def show = {
		def trackedItemInstance = TrackedItem.read(params?.id)
		
		if (trackedItemInstance) {
			// see what this item is tied to...
			if (trackedItemInstance.person) {
				// if it's a person
				redirect(controller:'person', action:'show' , id:trackedItemInstance.person.id)
			} else if (trackedItemInstance.dwellingUnit) {
				// if it's a dwelling unit
				redirect(controller:'dwellingUnit', action:'show' , id:trackedItemInstance.dwellingUnit.id)
			} else if (trackedItemInstance.household) {
				// if it's a household
				redirect(controller:'household', action:'show', id:trackedItemInstance.household.id)
			}
		} else {
			flash.message = "Unable to find Tracked Item: ${params?.id}"
		}
	}
	
	@Secured(['ROLE_NCS_IT'])
	def edit = {
		def trackedItemInstance = TrackedItem.read(params.id)
		if (!trackedItemInstance){
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'trackedItem.label', default: 'TrackedItem'), params.id])}"
		} else {
			return [trackedItemInstance: trackedItemInstance]
		}
	}
	
	@Secured(['ROLE_NCS_IT'])
	def update = {
		
		def message = ''
		def today = new LocalDate()		
		
		def trackedItemInstance = TrackedItem.read(params.id)
		if (trackedItemInstance){

			// Saving Result	
			if (params.receiptDate && params?.result?.id) {

				def username = authenticateService.principal().getUsername()
				def appName = "ncs-case-management"
				def receivedDate = params.receiptDate
				
				if ( ! receivedDate ) {
					receivedDate = new LocalDate()
				} else {
					// Mon Jan 31 13:38:42 CST 2011
					//receivedDate = Date.parse('EEE MMM d HH:mm:ss z yyyy', receivedDate)
					receivedDate = new LocalDate(receivedDate)
				}

				if (receivedDate.isAfter(today)) {
					trackedItemInstance.errors.rejectValue("receiptDate", "trackedItem.result.receivedDate.receiptedWithFutureDate", [message(code: 'trackedItem.label', default: 'Tracked Item')] as Object[], "Received date must not be greater than todays date.")
						render(view: "edit", model: [ trackedItemInstance: trackedItemInstance])
						return
				}
				
				def result = Result.read(params.result.id)
				
				// converting receivedDate from Joda LocalDate to Java Date
				def javaReceivedDate = receivedDate.toDateTime(midnight).toCalendar().time
				
				if(trackedItemInstance.result) {
					
					def itemResultInstance = ItemResult.read(trackedItemInstance?.result?.id)
					
					def jodaOldReceivedDate = new LocalDate(itemResultInstance.receivedDate)
					
					println "NGP Debug; jodaOldReceivedDate: ${jodaOldReceivedDate}"
					println "NGP Debug; receivedDate: ${receivedDate}"
					
					if (jodaOldReceivedDate.isEqual(receivedDate)) {
						println "NGP Debug; Dates are equal;"
					}
					
					if (itemResultInstance.result.id != result.id || itemResultInstance.receivedDate != javaReceivedDate) {
						
						if (itemResultInstance.result.id != result.id) {
							//Log old result
							//def auditLogInstance = AuditLogEvent.findByPersistedObjectId(itemResultInstance.id)
								
							// Log result 
							message += auditLog("edu.umn.ncs.ItemResult", 
								"UPDATE", 
								"edu.umn.ncs.Result:${result.id}", 
								"edu.umn.ncs.Result:${itemResultInstance.result.id}", 
								itemResultInstance.id, 
								"result")

							// Update result
							itemResultInstance.result = result
						}
						
						if (itemResultInstance.receivedDate != javaReceivedDate) {
							
							message += auditLog('edu.umn.ncs.ItemResult',
								'UPDATE',
								javaReceivedDate.toString(),
								"${itemResultInstance.receivedDate.toString()}",
								itemResultInstance.id,
								'receivedDate')
						
							// Update receiveDate
							itemResultInstance.receivedDate = javaReceivedDate
						}
						
						// Transact out userUpdated
						message += auditLog('edu.umn.ncs.ItemResult', 
							'UPDATE', 
							username, 
							itemResultInstance.userUpdated, 
							itemResultInstance.id, 
							'userUpdated')

						// Update userUpdated
						itemResultInstance.userUpdated = username
						
						if (!itemResultInstance.hasErrors() && itemResultInstance.save(flush: true)) {
							message += "Result for Item ID: ${trackedItemInstance.id} updated. <br/>"
							trackedItemInstance.result = itemResultInstance
						} else {
							message += "Failed updating ItemResult for Item ID: ${trackedItemInstance} ItemResult ID: ${itemResultInstance.id}. "
							itemResultInstance.errors.each{ e ->
								e.fieldErrors.each{fe -> println "! Rejected '${fe.rejectedValues}' for field '${fe.field}'<br/>"}
							}
						}
					}
					
				} else {
					trackedItemInstance.result = new ItemResult(result: result,
						userCreated: username,
						appCreated: appName,
						receivedDate: javaReceivedDate,
						trackedItem: trackedItemInstance)
				}
			}
			 
			if (params?.parentItem?.id) {
				def parentItem = TrackedItem.read(params?.parentItem?.id)
				if (parentItem) {
					trackedItemInstance.parentItem = parentItem
				} else {
					trackedItemInstance.errors.rejectValue("parentItem", "trackedItem.parentItem.notFound", [message(code: 'trackedItem.label', default: 'trackedItem')] as Object[], "Parent tracked item for entered parent item ID not found.")
					render(view: "edit", model: [trackedItemInstance: trackedItemInstance])
					return
				}
			}
			
			if (params.version) {
				def version = params.version.toLong()
				if (trackedItemInstance.version > version) {

					trackedItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'trackedItem.label', default: 'trackedItem')] as Object[], "Another user has updated this Batch while you were editing")
					render(view: "edit", model: [trackedItemInstance: trackedItemInstance])
					return
				}
			}
			
			if (!trackedItemInstance.hasErrors() && trackedItemInstance.save(flush: true)) {
				message += "Item ${trackedItemInstance.id} updated successfully!"
				render(view: "edit", model: [trackedItemInstance: trackedItemInstance, message: message])
			} else {
				message += "Failed updating Item ${trackedItemInstance.id}!"
				render (view: "edit", model: [trackedItemInstance: trackedItemInstance, message: message])
			}
			
		} else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'trackedItem.label', default: 'Tracked Item'), params.id])}"
            redirect(controller: "batch", action: "list")
        }
	}
	
	private def auditLog(className, eventName, newValue, oldValue, persistedObjectId, propertyName){

		def message = ""		
		def username = authenticateService.principal().getUsername()
		
		def auditLog = new AuditLogEvent(actor: username,
			className: className,
			dateCreated: new Date(),
			eventName: eventName,
			lastUpdated: new Date(),
			newValue: newValue,
			oldValue: oldValue,
			persistedObjectId: persistedObjectId,
			propertyName: propertyName)
		
		if (!auditLog.hasErrors() && auditLog.save(flush: true)) {
			message += "Logged ${propertyName}: ${oldValue} <br/>"
			
		} else {
			message += "Failed to log old ${propertyName}: ${oldValue} <br/>"
			auditLog.errors.each{ e ->
				e.fieldErrors.each{fe -> println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"}
			}
		}
		return message
	}
	
}
