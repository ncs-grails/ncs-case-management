package edu.umn.ncs

// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT'])
class EventPickOneController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [eventPickOneInstanceList: EventPickOne.list(params), eventPickOneInstanceTotal: EventPickOne.count()]
    }

    def create = {
        def eventPickOneInstance = new EventPickOne()
        eventPickOneInstance.properties = params
        return [eventPickOneInstance: eventPickOneInstance]
    }

    def save = {
        def eventPickOneInstance = new EventPickOne(params)
        if (eventPickOneInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), eventPickOneInstance.id])}"
            redirect(action: "show", id: eventPickOneInstance.id)
        }
        else {
            render(view: "create", model: [eventPickOneInstance: eventPickOneInstance])
        }
    }

    def show = {
        def eventPickOneInstance = EventPickOne.get(params.id)
        if (!eventPickOneInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), params.id])}"
            redirect(action: "list")
        }
        else {
            [eventPickOneInstance: eventPickOneInstance]
        }
    }

    def edit = {
        def eventPickOneInstance = EventPickOne.get(params.id)
        if (!eventPickOneInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [eventPickOneInstance: eventPickOneInstance]
        }
    }

    def update = {
        def eventPickOneInstance = EventPickOne.get(params.id)
        if (eventPickOneInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (eventPickOneInstance.version > version) {
                    
                    eventPickOneInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'eventPickOne.label', default: 'EventPickOne')] as Object[], "Another user has updated this EventPickOne while you were editing")
                    render(view: "edit", model: [eventPickOneInstance: eventPickOneInstance])
                    return
                }
            }
            eventPickOneInstance.properties = params
            if (!eventPickOneInstance.hasErrors() && eventPickOneInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), eventPickOneInstance.id])}"
                redirect(action: "show", id: eventPickOneInstance.id)
            }
            else {
                render(view: "edit", model: [eventPickOneInstance: eventPickOneInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def eventPickOneInstance = EventPickOne.get(params.id)
        if (eventPickOneInstance) {
            try {
                eventPickOneInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventPickOne.label', default: 'EventPickOne'), params.id])}"
            redirect(action: "list")
        }
    }
}
