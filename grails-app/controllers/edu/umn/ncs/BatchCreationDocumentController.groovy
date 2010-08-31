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

        def batchCreationConfig = BatchCreationConfig.get(params.batchCreationConfig.id)
        if (batchCreationConfig){

            def batchCreationDocumentInstance = new BatchCreationDocument(params)
            if (batchCreationDocumentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), batchCreationDocumentInstance.id])}"
                redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationConfig.id)
            }
            else {
                flash.message = "failed to save new document"

                redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationConfig.id)
            }
        } else {
            redirect(controller:"batchCreationConfig", action:"list")
        }
    }

    def delete = {
        def batchCreationDocumentInstance = BatchCreationDocument.get(params.id)
        if (batchCreationDocumentInstance) {
            try {
                batchCreationDocumentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
                redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationDocumentInstance.batchCreationConfig.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Failed to delete document id: ${params.id}"
                redirect(controller:"batchCreationConfig", action: "edit", id:batchCreationDocumentInstance.batchCreationConfig.id)
            }
        }
        else {
            flash.message = "Document id: ${params.id} not found."
            redirect(controller:"batchCreationConfig", action: "list")
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
        println "${params}"
        def batchCreationDocumentInstance = BatchCreationDocument.get(params.id)
        if (batchCreationDocumentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (batchCreationDocumentInstance.version > version) {
                    redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationDocumentInstance.batchCreationConfig.id)
                    return
                }
            }
            batchCreationDocumentInstance.properties = params
            if (!batchCreationDocumentInstance.hasErrors() && batchCreationDocumentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), batchCreationDocumentInstance.id])}"
                redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationDocumentInstance.batchCreationConfig.id)
            }
            else {
                flash.message = "Error saving document."
                redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationDocumentInstance.batchCreationConfig.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), params.id])}"
            redirect(controller:"batchCreationConfig", action: "list")
        }
    }

    def getData = {
        // return the data from a BatchCreationConfigDocument type based on items
        // in the user's queue

    }

}
