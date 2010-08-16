package edu.umn.ncs

class BatchCreationDocumentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [batchCreationDocumentInstanceList: BatchCreationDocument.list(params), batchCreationDocumentInstanceTotal: BatchCreationDocument.count()]
    }

    def create = {
        def batchCreationDocumentInstance = new BatchCreationDocument()
        batchCreationDocumentInstance.properties = params
        return [batchCreationDocumentInstance: batchCreationDocumentInstance]
    }

    def save = {
        def batchCreationDocumentInstance = new BatchCreationDocument(params)
        if (batchCreationDocumentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), batchCreationDocumentInstance.id])}"
            redirect(action: "show", id: batchCreationDocumentInstance.id)
        }
        else {
            render(view: "create", model: [batchCreationDocumentInstance: batchCreationDocumentInstance])
        }
    }

    def show = {
        def batchCreationDocumentInstance = BatchCreationDocument.get(params.id)
        if (!batchCreationDocumentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
            redirect(action: "list")
        }
        else {
            [batchCreationDocumentInstance: batchCreationDocumentInstance]
        }
    }

    def edit = {
        def batchCreationDocumentInstance = BatchCreationDocument.get(params.id)
        if (!batchCreationDocumentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [batchCreationDocumentInstance: batchCreationDocumentInstance]
        }
    }

    def update = {
        def batchCreationDocumentInstance = BatchCreationDocument.get(params.id)
        if (batchCreationDocumentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (batchCreationDocumentInstance.version > version) {
                    
                    batchCreationDocumentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument')] as Object[], "Another user has updated this BatchCreationDocument while you were editing")
                    render(view: "edit", model: [batchCreationDocumentInstance: batchCreationDocumentInstance])
                    return
                }
            }
            batchCreationDocumentInstance.properties = params
            if (!batchCreationDocumentInstance.hasErrors() && batchCreationDocumentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), batchCreationDocumentInstance.id])}"
                redirect(action: "show", id: batchCreationDocumentInstance.id)
            }
            else {
                render(view: "edit", model: [batchCreationDocumentInstance: batchCreationDocumentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def batchCreationDocumentInstance = BatchCreationDocument.get(params.id)
        if (batchCreationDocumentInstance) {
            try {
                batchCreationDocumentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
            redirect(action: "list")
        }
    }
}
