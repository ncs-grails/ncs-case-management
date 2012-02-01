package edu.umn.ncs

import grails.converters.JSON

// Let's us use security annotations
import grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT','ROLE_NCS'])
class EventTypeController {

	def springSecurityService
	def debug = true
	def appName = 'ncs-case-management'
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [eventTypeInstanceList: EventType.list(params), eventTypeInstanceTotal: EventType.count()]
    }

    def create = {
        def eventTypeInstance = new EventType()
        eventTypeInstance.properties = params
        return [eventTypeInstance: eventTypeInstance]
    }

    def save = {
		// get the username of the logged in user
		def username = springSecurityService?.principal?.getUsername()

        def eventTypeInstance = new EventType(params)

		eventTypeInstance.userCreated = username
		eventTypeInstance.appCreated = appName

        if (eventTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eventType.label', default: 'EventType'), eventTypeInstance.id])}"
            redirect(action: "save", id: eventTypeInstance.id)
        }
        else {
            render(view: "create", model: [eventTypeInstance: eventTypeInstance])
        }
    }

    def show = {
		flash.message = flash.message
		redirect(action:'edit', params:params)
    }

	def jsonNames = {
		def eventTypeInstance = EventType.get(params.id)
		render eventTypeInstance as JSON
	}
	
	// This will display the form elements needed to input a particular eventType
	def form = {
		def eventTypeInstance = EventType.get(params.id)
		// Notice, there is no failure, or redirect if eventTypeInstance is null.
		[ eventTypeInstance: eventTypeInstance ]
	}

    def edit = {
        def eventTypeInstance = EventType.get(params.id)
        if (!eventTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventType.label', default: 'EventType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [eventTypeInstance: eventTypeInstance]
        }
    }

    def update = {
		// get the username of the logged in user
		def username = springSecurityService?.principal?.getUsername()

        def eventTypeInstance = EventType.get(params.id)
        if (eventTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (eventTypeInstance.version > version) {
                    
                    eventTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'eventType.label', default: 'EventType')] as Object[], "Another user has updated this EventType while you were editing")
                    render(view: "edit", model: [eventTypeInstance: eventTypeInstance])
                    return
                }
            }
			if (debug) {
				println "eventTypeInstance.dateCreated::${eventTypeInstance?.dateCreated}"
			}
            eventTypeInstance.properties = params
			if ( ! eventTypeInstance.userCreated ) {
				eventTypeInstance.userCreated = username
			}
			if ( ! eventTypeInstance.appCreated ) {
				eventTypeInstance.appCreated = appName
			}
			eventTypeInstance.lastUpdated = new Date()
			eventTypeInstance.userUpdated = username

            if (!eventTypeInstance.hasErrors() && eventTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'eventType.label', default: 'EventType'), eventTypeInstance.id])}"
                redirect(action: "list", id: eventTypeInstance.id)
            }
            else {
                render(view: "edit", model: [eventTypeInstance: eventTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventType.label', default: 'EventType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def eventTypeInstance = EventType.get(params.id)
        if (eventTypeInstance) {
            try {
                eventTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'eventType.label', default: 'EventType'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'eventType.label', default: 'EventType'), params.id])}"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventType.label', default: 'EventType'), params.id])}"
            redirect(action: "list")
        }
    }

	def getUseElements = {
		if (debug) {
			println "params::${params}"
		}
		if (params.id) {
			def eventTypeInstance = EventType.read(params.id)
			if (eventTypeInstance) {
				def eventPickOneList = EventPickOne.findAllByEventType(eventTypeInstance)
				def jsonList = eventTypeInstance.collect { [
						controlId: params.controlId,
						useEventCode: it.useEventCode,
						eventCode: it?.nameEventCode,
						useEventDate: it.useEventDate,
						useEventDescription: it.useEventDescription,
						eventDescription: it?.nameEventDescription,
						useEventPickOne: it.useEventPickOne,
						eventPickOneList:eventPickOneList
					]}
		
				if (debug) {
					println "return::${jsonList}"
				}
				render jsonList as JSON
			}	
		}
		return null
	}
}
