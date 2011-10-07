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

		// this update looks very involved.
		// I realize that some of it is tricky due to the result changing?
		// but could it have used the "trackedItemInstance.parameters = params" trick?
		// ... the answer may be "no", but I thought I'd ask.
		
		// TODO: Expiration date doesn't show up for addition/modification/deletion
		def username = authenticateService.principal().getUsername()
		def appName = "ncs-case-management"

		def message = ''
		def today = new LocalDate()	
		
		def itemResultInstance = null
		
		def oldResult = null
		def oldReceivedDate = null
		

		// Next 4 used for audit logging
		def oldItemResultDateCreated = null
		def oldItemResultLastUpdated = null
		def oldItemResultUserCreated = null
		def oldItemResultUserUpdated = null
		
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
				
				oldItemResultDateCreated = itemResultInstance?.dateCreated
				oldItemResultLastUpdated = itemResultInstance?.lastUpdated
				
				oldItemResultUserCreated = itemResultInstance?.userCreated
				oldItemResultUserUpdated = itemResultInstance?.userUpdated
			}

			if (params.receiptDate && params?.result?.id) {
				// Save Result
				
				// Mon Jan 31 13:38:42 CST 2011
				//receivedDate = Date.parse('EEE MMM d HH:mm:ss z yyyy', receivedDate)
				def receivedDate = new LocalDate(params.receiptDate)
				
				// TODO: This could be a contstraint on the domain class
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
			// TODO: Do not allow parent item to be assigned to different person/household/dwelling unit
			// TODO: Do not allow parent item to be assigned to existing child item (parent -> child loop)
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

/* 
 				Skip manually logging "DELETE" events. 
				They logged automatically. (problem: actor not saved on auto logging. But it works for ajz)
				UPDATEs are not auto logged should be manually logged here:
 */
 
				// Log studyYear
				if (oldStudyYear && trackedItemInstance.studyYear && oldStudyYear != trackedItemInstance.studyYear) {
					// Log studyYear "UPDATE" 
					message += auditLog('edu.umn.ncs.trackedItemInstance',
						"UPDATE",
						trackedItemInstance.studyYear,
						oldStudyYear,
						trackedItemInstance.id,
						"studyYear")
				}
				
				// Log parentItem
				if (oldParentItem && trackedItemInstance.parentItem && trackedItemInstance.parentItem && oldParentItem != trackedItemInstance.parentItem) {
					// Log parentItem "UPDATE" 
					// Result for 85753 of edu.umn.ncs.Result : 281 on 2011-09-10 00:00:00.0. 
						message += auditLog('edu.umn.ncs.trackedItemInstance',
							"UPDATE",
							trackedItemInstance.parentItem.toString(),
							oldParentItem.toString(),
							trackedItemInstance.id,
							"parentItem")
				}

				// Log result, receivedDate, user "UPDATE" events
				if (oldResult && trackedItemInstance?.result?.id && oldResult != trackedItemInstance?.result?.result?.id) {
					// Log result
					message += auditLog("edu.umn.ncs.ItemResult",
						"UPDATE",
						"edu.umn.ncs.Result:${itemResultInstance.result.id}",
						"edu.umn.ncs.Result:${oldResult}" + " Created: [on: ${oldItemResultDateCreated} by: ${oldItemResultUserCreated}] Updated: [on: ${oldItemResultLastUpdated} by ${oldItemResultUserUpdated} ]",
						itemResultInstance.id,
						"result")
				}
				
				if (oldReceivedDate && trackedItemInstance?.result?.receivedDate && oldReceivedDate != trackedItemInstance?.result?.receivedDate) {
					// Log receivedDate
					message += auditLog('edu.umn.ncs.ItemResult',
						"UPDATE",
						"${itemResultInstance.receivedDate.toString()}",
						oldReceivedDate.toString() + " Created: [on: ${oldItemResultDateCreated} by: ${oldItemResultUserCreated}] Updated: [on: ${oldItemResultLastUpdated} by ${oldItemResultUserUpdated} ]",
						itemResultInstance.id,
						"receivedDate")
				}

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
	
	// this should probably be in a service? (so it can be re-used )
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
			if (debug && propertyName == "studyYear") {
				def studyYear = AuditLogEvent.findByPersistedObjectIdAndPropertyName(85758, "studyYear")
				println "LOGGED STUDYYEAR: ${studyYear}"
			}
			
		} else {
			message += "Failed to log old ${propertyName}: ${oldValue} <br/>"
			auditLog.errors.each{ e ->
				e.fieldErrors.each{fe -> println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"}
			}
		}
		return message
	}
	
}
