package edu.umn.ncs
// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class InstrumentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def letterhead = {}

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [instrumentInstanceList: Instrument.list(params), instrumentInstanceTotal: Instrument.count()]
    }

    def create = {
        def instrumentInstance = new Instrument()
        instrumentInstance.properties = params
        return [instrumentInstance: instrumentInstance]
    }

    def save = {
		//println "PARAMS: ${params}\n\n"
        def instrumentInstance = new Instrument(params)
        if (instrumentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'instrument.label', default: 'Instrument'), instrumentInstance.id])}"
            redirect(action: "edit", id: instrumentInstance.id)
        }
        else {
            render(view: "create", model: [instrumentInstance: instrumentInstance])
        }
    }

    def edit = {
        def instrumentInstance = Instrument.get(params.id)
        if (!instrumentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrument.label', default: 'Instrument'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [instrumentInstance: instrumentInstance]
        }
    }

    def update = {
        def instrumentInstance = Instrument.get(params.id)
        if (instrumentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (instrumentInstance.version > version) {
                    
                    instrumentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'instrument.label', default: 'Instrument')] as Object[], "Another user has updated this Instrument while you were editing")
                    render(view: "edit", model: [instrumentInstance: instrumentInstance])
                    return
                }
            }
            instrumentInstance.properties = params
            if (!instrumentInstance.hasErrors() && instrumentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'instrument.label', default: 'Instrument'), instrumentInstance.id])}"
                redirect(action: "edit", id: instrumentInstance.id)
            }
            else {
                render(view: "edit", model: [instrumentInstance: instrumentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrument.label', default: 'Instrument'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def instrumentInstance = Instrument.get(params.id)
        if (instrumentInstance) {
            try {
                instrumentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'instrument.label', default: 'Instrument'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'instrument.label', default: 'Instrument'), params.id])}"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrument.label', default: 'Instrument'), params.id])}"
            redirect(action: "list")
        }
    }
}
