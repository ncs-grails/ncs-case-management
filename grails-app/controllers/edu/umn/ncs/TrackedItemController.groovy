package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_LOOKUP'])
class TrackedItemController {

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
}
