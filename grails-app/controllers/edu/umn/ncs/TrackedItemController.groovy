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

		def username = authenticateService.principal().getUsername()
		def appName = "ncs-case-management"

		def message = ''
		def today = new LocalDate()	
		def eventName = ''
		def newValue = ''
		
		def itemResultInstance = null
		
		def oldResult = null
		def oldReceivedDate = null
		def oldUserName = null
		def oldUserPropertyName = null
		
		def trackedItemInstance = TrackedItem.read(params.id)
		if (trackedItemInstance){

			if (params.version) {
				def version = params.version.toLong()
				if (trackedItemInstance.version > version) {

					trackedItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", "Another user has updated this trackedItem while you were editing")
					render(view: "edit", model: [trackedItemInstance: trackedItemInstance])
					return
				}
			}
			
			if (params?.result?.id == 'null') {
				params?.result?.id = null
			}
			
			if (!params.receiptDate && params?.result?.id || params.receiptDate && !params?.result?.id) {
				trackedItemInstance.errors.rejectValue("result", "trackedItem.result", "RECEIPT DATE & RESULT values required.")
					render(view: "edit", model: [ trackedItemInstance: trackedItemInstance])
					return
			}
			
			if (trackedItemInstance.result) {
				// Get old values
				itemResultInstance = ItemResult.read(trackedItemInstance?.result?.id)
				
				oldResult = itemResultInstance.result.id
				oldReceivedDate = itemResultInstance.receivedDate
				oldUserName = (itemResultInstance.userUpdated ? itemResultInstance.userUpdated : itemResultInstance.userCreated)
				oldUserPropertyName = (itemResultInstance.userUpdated ? "userUpdated" : "userCreated")
			}

			if (params.receiptDate && params?.result?.id) {
				// Save Result
				
				// Mon Jan 31 13:38:42 CST 2011
				//receivedDate = Date.parse('EEE MMM d HH:mm:ss z yyyy', receivedDate)
				def receivedDate = new LocalDate(params.receiptDate)
				
				if (receivedDate.isAfter(today)) {
					trackedItemInstance.errors.rejectValue("result", "trackedItem.result", "Received date must not be greater than today's date.")
						render(view: "edit", model: [ trackedItemInstance: trackedItemInstance])
						return
				}
				
				// converting receivedDate from Joda LocalDate to Java Date
				def javaReceivedDate = receivedDate.toDateTime(midnight).toCalendar().time
				def result = Result.read(params.result.id)
				
				if (result.id != oldResult || javaReceivedDate != oldReceivedDate) {
					
					if (oldResult && oldReceivedDate) {
						if (result.id != oldResult) {
							itemResultInstance.result = result
						}
						
						if (javaReceivedDate != oldReceivedDate) {
							itemResultInstance.receivedDate = javaReceivedDate
						}
						
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
					} else {
						trackedItemInstance.result = new ItemResult(result: result,
							userCreated: username,
							appCreated: appName,
							receivedDate: javaReceivedDate,
							trackedItem: trackedItemInstance)
					}
				}
				
			} else if (!params.receiptDate && !params?.result?.id && oldResult && oldReceivedDate) {
			
				// DELETE Result
				try {
					
					trackedItemInstance.result = null
					if (!trackedItemInstance.hasErrors() && trackedItemInstance.save(flush: true)) {
						flash.message += "<br/>Tracked Item ID: ${trackedItemInstance.id} updated."
					} else {
						flash.message += "<br/>Tracked Item ID: ${trackedItemInstance.id} NOT updated."
					}
					
					itemResultInstance.delete(flush: true)
					flash.message += "Item Result ID: ${itemResultInstance.id} successfully deleted."
				} catch (org.springframework.dao.DataIntegrityViolationException e) {
					flash.message += "Item Result ID: ${itemResultInstance.id} not deleted."
					
					itemResultInstance.errors.each{ err ->
						err.fieldErrors.each{fe -> println "! Rejected '${fe.rejectedValues}' for field '${fe.field}'<br/>"}
					}
				}
			
			}

			// Update parentItem			 
			def oldParentItem = trackedItemInstance.parentItem
			if (params?.parentItem?.id) {
				def parentItem = TrackedItem.read(params?.parentItem?.id)
				if (parentItem) {
					trackedItemInstance.parentItem = parentItem
				} else {
					trackedItemInstance.errors.rejectValue("parentItem", "trackedItem.parentItem.notFound", "Parent tracked item for entered parent item ID not found.")
					render(view: "edit", model: [trackedItemInstance: trackedItemInstance])
					return
				}
			} else if (oldParentItem && !params?.parentItem?.id){
				trackedItemInstance.parentItem = null
			}
			
			// Update studyYear
			def oldStudyYear = trackedItemInstance.studyYear
			if (params?.studyYear) {
				trackedItemInstance.studyYear = params?.studyYear?.toInteger()
				
			} else if (oldStudyYear && !params?.studyYear){
				trackedItemInstance.studyYear = null
			}
			
			if (!trackedItemInstance.hasErrors() && trackedItemInstance.save(flush: true)) {

/*********** Disable audit logging. It should be log automatically. It does not work if I run it locally. 
 
				// Log studyYear
				if (oldStudyYear && oldStudyYear != trackedItemInstance.studyYear) {
					eventName = (trackedItemInstance.studyYear ? "UPDATE" : "DELETE")
					message += auditLog('edu.umn.ncs.trackedItemInstance',
						eventName,
						trackedItemInstance.studyYear,
						oldStudyYear,
						trackedItemInstance.id,
						'studyYear')
				}
				
				// Log parentItem
				if (oldParentItem && oldParentItem != trackedItemInstance.parentItem) {
					eventName = (trackedItemInstance.parentItem ? "UPDATE" : "DELETE")
					message += auditLog('edu.umn.ncs.trackedItemInstance',
						eventName,
						trackedItemInstance.parentItem,
						oldParentItem,
						trackedItemInstance.id,
						'parentItem')
				}

				// Log result, receivedDate, user
				eventName = (trackedItemInstance?.result ? "UPDATE" : "DELETE")
				
				if (oldResult && oldResult != trackedItemInstance?.result?.id) {
				
				newValue = (eventName == "DELETE" ? null : "edu.umn.ncs.Result:${itemResultInstance.result.id}")

					// Log result
					message += auditLog("edu.umn.ncs.ItemResult",
						eventName,
						newValue,
						"edu.umn.ncs.Result:${oldResult}",
						itemResultInstance.id,
						"result")
				}
				
				if (oldReceivedDate && oldReceivedDate != trackedItemInstance?.result?.receivedDate) {
					
					newValue = (eventName == "DELETE" ? null : "${itemResultInstance.receivedDate.toString()}")
					
					// Log receivedDate
					message += auditLog('edu.umn.ncs.ItemResult',
						eventName,
						newValue,
						oldReceivedDate.toString(),
						itemResultInstance.id,
						'receivedDate')
				}
				
				if (oldUserName && (oldUserName != trackedItemInstance?.result?.userUpdated && oldUserPropertyName == "userUpdated"
						|| oldUserName != trackedItemInstance?.result?.userCreated && oldUserPropertyName == "userCreated")) {
					
					newValue = (eventName == "DELETE" ? null : username)
					
					// Log user
					message += auditLog('edu.umn.ncs.ItemResult',
						eventName,
						newValue,
						oldUserName,
						itemResultInstance.id,
						oldUserPropertyName)
					
				}
				
*/				
				
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
