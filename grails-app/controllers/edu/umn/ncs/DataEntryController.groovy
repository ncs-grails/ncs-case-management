package edu.umn.ncs

// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_PROTECTED'])
class DataEntryController {

	static instrumentMapping = [
		[ instrumentNickName:'eligqx', controller: 'eligibilityQuestionnaire'],
		[ instrumentNickName:'fathengitems', controller: 'fatherEngagement'],
		[ instrumentNickName:'fathenhcons', controller: 'fatherEngagement'] ]

    def index = {
		def trackedItemInstance = TrackedItem.read(params?.id)
		[ trackedItemInstance: trackedItemInstance ]
	}

	def search = {
		def id = params?.id ?: params?.value
		def trackedItemInstance = TrackedItem.read(id)
		def controllerList = getControllerByTrackedItem(trackedItemInstance)
		[ trackedItemInstance: trackedItemInstance, controllerList: controllerList ]
	}

	private def getControllerByTrackedItem(TrackedItem trackedItemInstance) {
		if (trackedItemInstance) {
			def defaultAction = 'create'
			def defaultParam = 'trackedItem.id'

			def instrumentInstance = trackedItemInstance?.batch?.primaryInstrument
			def controllers = instrumentMapping.findAll{ it.instrumentNickName == instrumentInstance.nickName }

			controllers.each{
				// set the param map
				def paramMap = [:]
				paramMap[it.param ?: defaultParam] = trackedItemInstance.id
				it.params = paramMap
				// set the default action if it was not provided
				it.action = it.action ?: defaultAction
			}
			return controllers
		} else {
			return []
		}
	}
}
