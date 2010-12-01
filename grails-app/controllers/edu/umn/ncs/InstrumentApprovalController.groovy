package edu.umn.ncs

class InstrumentApprovalController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [instrumentApprovalInstanceList: InstrumentApproval.list(params), instrumentApprovalInstanceTotal: InstrumentApproval.count()]
    }

    def create = {
        def instrumentApprovalInstance = new InstrumentApproval()
        instrumentApprovalInstance.properties = params
        return [instrumentApprovalInstance: instrumentApprovalInstance]
    }

    def save = {
        def instrumentApprovalInstance = new InstrumentApproval(params)
        if (instrumentApprovalInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), instrumentApprovalInstance.id])}"
            redirect(action: "show", id: instrumentApprovalInstance.id)
        }
        else {
            render(view: "create", model: [instrumentApprovalInstance: instrumentApprovalInstance])
        }
    }

    def show = {
        def instrumentApprovalInstance = InstrumentApproval.get(params.id)
        if (!instrumentApprovalInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), params.id])}"
            redirect(action: "list")
        }
        else {
            [instrumentApprovalInstance: instrumentApprovalInstance]
        }
    }

    def edit = {
        def instrumentApprovalInstance = InstrumentApproval.get(params.id)
        if (!instrumentApprovalInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [instrumentApprovalInstance: instrumentApprovalInstance]
        }
    }

    def update = {
        def instrumentApprovalInstance = InstrumentApproval.get(params.id)
        if (instrumentApprovalInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (instrumentApprovalInstance.version > version) {
                    
                    instrumentApprovalInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'instrumentApproval.label', default: 'InstrumentApproval')] as Object[], "Another user has updated this InstrumentApproval while you were editing")
                    render(view: "edit", model: [instrumentApprovalInstance: instrumentApprovalInstance])
                    return
                }
            }
            instrumentApprovalInstance.properties = params
            if (!instrumentApprovalInstance.hasErrors() && instrumentApprovalInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), instrumentApprovalInstance.id])}"
                redirect(action: "show", id: instrumentApprovalInstance.id)
            }
            else {
                render(view: "edit", model: [instrumentApprovalInstance: instrumentApprovalInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def instrumentApprovalInstance = InstrumentApproval.get(params.id)
        if (instrumentApprovalInstance) {
            try {
                instrumentApprovalInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval'), params.id])}"
            redirect(action: "list")
        }
    }
}
