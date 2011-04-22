package edu.umn.ncs
// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

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

			[personInstance: personInstance,
				personLinkInstance: personLinkInstance,
				trackedItemInstanceList: trackedItemInstanceList, 
				appointmentInstanceList: appointmentInstanceList ]
		}
		
	}

}
