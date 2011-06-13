package edu.umn.ncs
// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
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

			def householdInstanceList = Household.createCriteria().list{
				people {
					idEq(personInstance.id)
				}
			}

			[personInstance: personInstance,
				personLinkInstance: personLinkInstance,
				trackedItemInstanceList: trackedItemInstanceList, 
				appointmentInstanceList: appointmentInstanceList,
				householdInstanceList: householdInstanceList,
				callInstanceList: callInstanceList ]
		}
		
	}

}
