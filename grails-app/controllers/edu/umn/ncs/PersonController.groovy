package edu.umn.ncs
// Let's us use security annotations
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent
import edu.umn.ncs.phone.Call

@Secured(['ROLE_NCS_PROTECTED'])
class PersonController {
	def lookupService

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

}
