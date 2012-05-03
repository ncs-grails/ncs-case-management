package edu.umn.ncs
import grails.plugins.springsecurity.Secured
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.orm.auditable.*


@Secured(['ROLE_NCS'])
class TrackedItemController {
	private boolean debug = true
	private static LocalTime midnight = new LocalTime(0,0)

	def springSecurityService

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
	
		// TO-DO: 2012-05-03 Noticed with ReceiptItemsService.receiptItem the ItemResult updates are logging properly in auditLog by auditLog plugin. Change "update tracked_item result" here to call receiptItemsService.receiptItem 

		// TODO: Expiration date doesn't show up for addition/modification/deletion
		def username = springSecurityService.principal.getUsername()
		def appName = "ncs-case-management"

		def messages = []
		def errors = []
		def auditLogMap = []

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
							messages.add("Result for Item ID: ${trackedItemInstance.id} updated. <br/>")
							trackedItemInstance.result = itemResultInstance
						} else {
							errors.add("Failed updating ItemResult for Item ID: ${trackedItemInstance} ItemResult ID: ${itemResultInstance.id}. ")
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

 

				messages.add("Item ${trackedItemInstance.id} updated successfully!")
				render(view: "edit", model: [trackedItemInstance: trackedItemInstance, messages: messages])
				
			} else {
				messages.add ("Failed updating Item ${trackedItemInstance.id}!")
				render (view: "edit", model: [trackedItemInstance: trackedItemInstance, messages: messages])
			}
			
		} else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'trackedItem.label', default: 'Tracked Item'), params.id])}"
            redirect(controller: "batch", action: "list")
        }
	}
	
}
