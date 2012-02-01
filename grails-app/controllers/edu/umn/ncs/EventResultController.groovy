package edu.umn.ncs

// Let's us use security annotations
import grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT'])
class EventResultController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [eventResultInstanceList: EventResult.list(params), eventResultInstanceTotal: EventResult.count()]
    }

    def create = {
        def eventResultInstance = new EventResult()
        eventResultInstance.properties = params
        return [eventResultInstance: eventResultInstance]
    }

    def save = {
        def eventResultInstance = new EventResult(params)
        if (eventResultInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eventResult.label', default: 'EventResult'), eventResultInstance.id])}"
            redirect(action: "show", id: eventResultInstance.id)
        }
        else {
            render(view: "create", model: [eventResultInstance: eventResultInstance])
        }
    }

    def show = {
        def eventResultInstance = EventResult.get(params.id)
        if (!eventResultInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventResult.label', default: 'EventResult'), params.id])}"
            redirect(action: "list")
        }
        else {
            [eventResultInstance: eventResultInstance]
        }
    }

    def edit = {
        def eventResultInstance = EventResult.get(params.id)
        if (!eventResultInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventResult.label', default: 'EventResult'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [eventResultInstance: eventResultInstance]
        }
    }

    def update = {
        def eventResultInstance = EventResult.get(params.id)
        if (eventResultInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (eventResultInstance.version > version) {
                    
                    eventResultInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'eventResult.label', default: 'EventResult')] as Object[], "Another user has updated this EventResult while you were editing")
                    render(view: "edit", model: [eventResultInstance: eventResultInstance])
                    return
                }
            }
            eventResultInstance.properties = params
            if (!eventResultInstance.hasErrors() && eventResultInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'eventResult.label', default: 'EventResult'), eventResultInstance.id])}"
                redirect(action: "show", id: eventResultInstance.id)
            }
            else {
                render(view: "edit", model: [eventResultInstance: eventResultInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventResult.label', default: 'EventResult'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def eventResultInstance = EventResult.get(params.id)
        if (eventResultInstance) {
            try {
                eventResultInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'eventResult.label', default: 'EventResult'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'eventResult.label', default: 'EventResult'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventResult.label', default: 'EventResult'), params.id])}"
            redirect(action: "list")
        }
    }
}
