package edu.umn.ncs

import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
class DataSetTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [dataSetTypeInstanceList: DataSetType.list(params), dataSetTypeInstanceTotal: DataSetType.count()]
    }

    def create = {
        def dataSetTypeInstance = new DataSetType()
        dataSetTypeInstance.properties = params
        return [dataSetTypeInstance: dataSetTypeInstance]
    }

    def save = {
        def dataSetTypeInstance = new DataSetType(params)
        if (dataSetTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), dataSetTypeInstance.id])}"
            redirect(action: "show", id: dataSetTypeInstance.id)
        }
        else {
            render(view: "create", model: [dataSetTypeInstance: dataSetTypeInstance])
        }
    }

    def show = {
        def dataSetTypeInstance = DataSetType.get(params.id)
        if (!dataSetTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [dataSetTypeInstance: dataSetTypeInstance]
        }
    }

    def edit = {
        def dataSetTypeInstance = DataSetType.get(params.id)
        if (!dataSetTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [dataSetTypeInstance: dataSetTypeInstance]
        }
    }

    def update = {
        def dataSetTypeInstance = DataSetType.get(params.id)
        if (dataSetTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (dataSetTypeInstance.version > version) {
                    
                    dataSetTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'dataSetType.label', default: 'DataSetType')] as Object[], "Another user has updated this DataSetType while you were editing")
                    render(view: "edit", model: [dataSetTypeInstance: dataSetTypeInstance])
                    return
                }
            }
            dataSetTypeInstance.properties = params
            if (!dataSetTypeInstance.hasErrors() && dataSetTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), dataSetTypeInstance.id])}"
                redirect(action: "show", id: dataSetTypeInstance.id)
            }
            else {
                render(view: "edit", model: [dataSetTypeInstance: dataSetTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def dataSetTypeInstance = DataSetType.get(params.id)
        if (dataSetTypeInstance) {
            try {
                dataSetTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataSetType.label', default: 'DataSetType'), params.id])}"
            redirect(action: "list")
        }
    }
}
