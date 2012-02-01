package edu.umn.ncs
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class InstrumentHistoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def springSecurityService
    def appName = 'ncs-case-management'

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {

        def instrumentHistoryInstanceList = InstrumentHistory.list()
        // sort it if anything is in it.
        if (instrumentHistoryInstanceList ) {
            instrumentHistoryInstanceList = instrumentHistoryInstanceList.sort{ - it.dateCreated.getDate() }
        }

		
        [instrumentHistoryInstanceList: instrumentHistoryInstanceList]
    }

    def create = {
        def instrumentHistoryInstance = new InstrumentHistory()
        instrumentHistoryInstance.properties = params
        return [instrumentHistoryInstance: instrumentHistoryInstance]
    }

    def save = {

        def instrumentHistoryInstance = new InstrumentHistory(params)

        def username = springSecurityService.principal.getUsername()
        instrumentHistoryInstance.userCreated = username
        instrumentHistoryInstance.appCreated = appName

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
}
