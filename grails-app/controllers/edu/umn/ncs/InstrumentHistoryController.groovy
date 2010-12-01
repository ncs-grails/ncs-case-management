package edu.umn.ncs

class InstrumentHistoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [instrumentHistoryInstanceList: InstrumentHistory.list(params), instrumentHistoryInstanceTotal: InstrumentHistory.count()]
    }

    def create = {
        def instrumentHistoryInstance = new InstrumentHistory()
        instrumentHistoryInstance.properties = params
        return [instrumentHistoryInstance: instrumentHistoryInstance]
    }

    def save = {
        def instrumentHistoryInstance = new InstrumentHistory(params)
        if (instrumentHistoryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), instrumentHistoryInstance.id])}"
            redirect(action: "show", id: instrumentHistoryInstance.id)
        }
        else {
            render(view: "create", model: [instrumentHistoryInstance: instrumentHistoryInstance])
        }
    }

    def show = {
        def instrumentHistoryInstance = InstrumentHistory.get(params.id)
        if (!instrumentHistoryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), params.id])}"
            redirect(action: "list")
        }
        else {
            [instrumentHistoryInstance: instrumentHistoryInstance]
        }
    }

    def edit = {
        def instrumentHistoryInstance = InstrumentHistory.get(params.id)
        if (!instrumentHistoryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [instrumentHistoryInstance: instrumentHistoryInstance]
        }
    }

    def update = {
        def instrumentHistoryInstance = InstrumentHistory.get(params.id)
        if (instrumentHistoryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (instrumentHistoryInstance.version > version) {
                    
                    instrumentHistoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'instrumentHistory.label', default: 'InstrumentHistory')] as Object[], "Another user has updated this InstrumentHistory while you were editing")
                    render(view: "edit", model: [instrumentHistoryInstance: instrumentHistoryInstance])
                    return
                }
            }
            instrumentHistoryInstance.properties = params
            if (!instrumentHistoryInstance.hasErrors() && instrumentHistoryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), instrumentHistoryInstance.id])}"
                redirect(action: "show", id: instrumentHistoryInstance.id)
            }
            else {
                render(view: "edit", model: [instrumentHistoryInstance: instrumentHistoryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def instrumentHistoryInstance = InstrumentHistory.get(params.id)
        if (instrumentHistoryInstance) {
            try {
                instrumentHistoryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'instrumentHistory.label', default: 'InstrumentHistory'), params.id])}"
            redirect(action: "list")
        }
    }
}
