package edu.umn.ncs

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT'])
class EventSourceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [eventSourceInstanceList: EventSource.list(params), eventSourceInstanceTotal: EventSource.count()]
    }

    def create = {
        def eventSourceInstance = new EventSource()
        eventSourceInstance.properties = params
        return [eventSourceInstance: eventSourceInstance]
    }

    def save = {
        def eventSourceInstance = new EventSource(params)
        if (eventSourceInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eventSource.label', default: 'EventSource'), eventSourceInstance.id])}"
            redirect(action: "show", id: eventSourceInstance.id)
        }
        else {
            render(view: "create", model: [eventSourceInstance: eventSourceInstance])
        }
    }

    def show = {
        def eventSourceInstance = EventSource.get(params.id)
        if (!eventSourceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventSource.label', default: 'EventSource'), params.id])}"
            redirect(action: "list")
        }
        else {
            [eventSourceInstance: eventSourceInstance]
        }
    }

    def edit = {
        def eventSourceInstance = EventSource.get(params.id)
        if (!eventSourceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventSource.label', default: 'EventSource'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [eventSourceInstance: eventSourceInstance]
        }
    }

    def update = {
        def eventSourceInstance = EventSource.get(params.id)
        if (eventSourceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (eventSourceInstance.version > version) {
                    
                    eventSourceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'eventSource.label', default: 'EventSource')] as Object[], "Another user has updated this EventSource while you were editing")
                    render(view: "edit", model: [eventSourceInstance: eventSourceInstance])
                    return
                }
            }
            eventSourceInstance.properties = params
            if (!eventSourceInstance.hasErrors() && eventSourceInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'eventSource.label', default: 'EventSource'), eventSourceInstance.id])}"
                redirect(action: "show", id: eventSourceInstance.id)
            }
            else {
                render(view: "edit", model: [eventSourceInstance: eventSourceInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventSource.label', default: 'EventSource'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def eventSourceInstance = EventSource.get(params.id)
        if (eventSourceInstance) {
            try {
                eventSourceInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'eventSource.label', default: 'EventSource'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'eventSource.label', default: 'EventSource'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventSource.label', default: 'EventSource'), params.id])}"
            redirect(action: "list")
        }
    }
}
