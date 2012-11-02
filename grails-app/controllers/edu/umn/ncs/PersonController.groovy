package edu.umn.ncs
// Let's us use security annotations
import grails.plugins.springsecurity.Secured

import org.apache.jasper.compiler.Node.ParamsAction;
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent
import edu.umn.ncs.phone.Call

@Secured(['ROLE_NCS_PROTECTED'])
class PersonController {
	def debug = true
	
	def lookupService
	def personService
	
	@Secured(['ROLE_NCS_LOOKUP'])
	def show = {

		//println "PERSON CONTROLLER > SHOW"

		def personInstance = Person.read(params.id)
		//println "=> personInstance = ${personInstance}"

		if (!personInstance) {
			response.sendError(404)
			render "Person ${params.id} Not found"
		}
		else {
			def personLinkInstance = PersonLink.findByPerson(personInstance)
			def trackedItemInstanceList = TrackedItem.findAllByPerson(personInstance)
			def appointmentInstanceList = Appointment.findAllByPerson(personInstance)
			def subjectInstanceList = Subject.findAllByPerson(personInstance)

			def incentiveInstanceList = Incentive.createCriteria().list {
				trackedItem {
					person {
						idEq(personInstance.id)
					}
				}
			}
			
			def callInstanceList = Call.createCriteria().list {
				items {
					person {
						idEq(personInstance.id)
					}
				}
			}

			def eventReportInstanceList = EventReport.createCriteria().list{
				person {
					idEq(personInstance.id)
				}
				order('contactDate', 'desc')
			}

			def householdInstanceList = Household.createCriteria().list{
				people {
					idEq(personInstance.id)
				}
			}

			def resultHistoryList = []
			
			// Find IDs for ItemResults
			def itemResultIdList = trackedItemInstanceList.collect{ it?.result?.id?.toString() }
			// get rid of nulls in the list
			itemResultIdList.removeAll([null])

			resultHistoryList = lookupService.resultHistory(resultHistoryList, itemResultIdList, trackedItemInstanceList)

			[personInstance: personInstance,
				personLinkInstance: personLinkInstance,
				subjectInstanceList: subjectInstanceList,
				trackedItemInstanceList: trackedItemInstanceList, 
				appointmentInstanceList: appointmentInstanceList,
				householdInstanceList: householdInstanceList,
				callInstanceList: callInstanceList,
				eventReportInstanceList: eventReportInstanceList,
				incentiveInstanceList : incentiveInstanceList,
				resultHistoryList: resultHistoryList ]
		}
		
	}

    @Secured(['ROLE_NCS_IT'])
    def cleanupContactInfo = {
		[type: [[id: 'address',value: 'addresses'], [id: 'phone',value: 'phone numbers'], [id: 'email', value: 'email addresses']] ]
	}

    @Secured(['ROLE_NCS_IT'])
    def personContactInfo = {
        def personInstance = Person.read(params.id)
        if (personInstance) {
            render( template: "contactInfoForm", model: [ personInstance: personInstance, type: params.type ] )
        } else {
            render "Oops, person not found with id::${params.id}!"
        }
    }

    @Secured(['ROLE_NCS_IT'])
    def findContactInfo = {
		if (debug) {
			println "Getting people with multiple $params.type records"
		}
		def results = personService.multipleContactRecords(params.type)
		if (debug) {
			println "Found ${results?.size()} people with multiple $params.type records"
		}
		render( template: "contactInfoList", model: [ results: results, type: params.type ] )
	}

    @Secured(['ROLE_NCS_IT'])
    def updateContactInfo = {
		if (debug) {
			println "Updating data::$params"
		}
		def personInstance = null
		def contactInfoInstance = null 
		def endDateKey = ""
		def contactInfoInstanceIds = []
		def success = false
		def errors = false
		params.findAll{ it.key =~ /id/ }.each{ contactInfoInstanceIds << it.value.toLong() }
		if (debug) {
			println "Processing person $params.type ids::$contactInfoInstanceIds"
		}
		
		// Process each contact record
		contactInfoInstanceIds.each {
			contactInfoInstance = personService.contactInfoRecord(params.type, it)
			if (contactInfoInstance) {
				if (debug) {
					println "Updating person $params.type::$contactInfoInstance"
				}				
				// TODO: Compare active status of contact record
				endDateKey = "endDate_$it"
				success = personService.updateContactInfoRecord(contactInfoInstance, params[endDateKey])
				personInstance = contactInfoInstance.person
				if (! success) {
					errors = true
				}
			}	
		}
		if (! errors) {
			flash.message = "person.update.message"
			flash.args = [ personInstance ]
			flash.default = "Contact info updated"
		}
		// TODO: Display info to user if update fails due to errors
		
		render( template: "contactInfoForm", model: [ personInstance: personInstance, type: params.type ] )
	}

    @Secured(['ROLE_NCS_IT'])
    def cancelContactUpdate = {
        def personInstance = Person.read(params.id)
        if (personInstance) {
            render( template: "contactInfo", model: [ personInstance: personInstance, type: params.type ] )
        } else {
            render "Oops, person not found with id::${params.id}! "
        }

    }

}
