package edu.umn.ncs

import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS'])
class EventTypeController {
	// def debug = true
	def debug = false
	
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
        def eventTypeInstance = new EventType(params)
        if (eventTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eventType.label', default: 'EventType'), eventTypeInstance.id])}"
            redirect(action: "show", id: eventTypeInstance.id)
        }
        else {
            render(view: "create", model: [eventTypeInstance: eventTypeInstance])
        }
    }

    def show = {
        def eventTypeInstance = EventType.get(params.id)
        if (!eventTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventType.label', default: 'EventType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [eventTypeInstance: eventTypeInstance]
        }
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
            eventTypeInstance.properties = params
            if (!eventTypeInstance.hasErrors() && eventTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'eventType.label', default: 'EventType'), eventTypeInstance.id])}"
                redirect(action: "show", id: eventTypeInstance.id)
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
                redirect(action: "show", id: params.id)
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
						useEventDate: it.useEventDate,
						useEventDescription: it.useEventDescription,
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
