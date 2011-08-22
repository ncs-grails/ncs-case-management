package edu.umn.ncs
// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent
import edu.umn.ncs.phone.Call

@Secured(['ROLE_NCS_PROTECTED'])
class PersonController {

	@Secured(['ROLE_NCS_LOOKUP'])
	def show = {
		def personInstance = Person.read(params.id)
		if (!personInstance) {
			response.sendError(404)
			render "Person ${params.id} Not found"
		}
		else {
			def personLinkInstance = PersonLink.findByPerson(personInstance)

			def trackedItemInstanceList = TrackedItem.findAllByPerson(personInstance)
			
			def appointmentInstanceList = Appointment.findAllByPerson(personInstance)
			
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

			// Find Matching Audit Events for ItemResults
			def auditLogEventInstanceList = AuditLogEvent.createCriteria().list{
					eq('className', 'edu.umn.ncs.ItemResult')
					'in'('persistedObjectId', itemResultIdList)
			}
			
			auditLogEventInstanceList.each{
				def item = [:]
				
				item.id = it.persistedObjectId.toInteger()
				def ti = trackedItemInstanceList.find{it.result?.id == item.id}
				item.trackedItem = TrackedItem.read(ti.id)
				item.username = it.actor
				item.dateCreated = it.dateCreated
				
				def resultId = it?.oldValue?.replace('edu.umn.ncs.Result : ', '')?.toInteger()
				item.oldResult = Result.read(resultId)
				
				if (item.oldResult) {
					resultHistoryList.add(item)
				}
			}

			[personInstance: personInstance,
				personLinkInstance: personLinkInstance,
				trackedItemInstanceList: trackedItemInstanceList, 
				appointmentInstanceList: appointmentInstanceList,
				householdInstanceList: householdInstanceList,
				callInstanceList: callInstanceList,
				eventReportInstanceList: eventReportInstanceList,
				resultHistoryList: resultHistoryList ]
		}
		
	}

}
