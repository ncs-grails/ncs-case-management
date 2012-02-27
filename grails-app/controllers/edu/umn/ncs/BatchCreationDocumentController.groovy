package edu.umn.ncs
// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class BatchCreationDocumentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [batchCreationDocumentInstanceList: BatchCreationDocument.list(params), batchCreationDocumentInstanceTotal: BatchCreationDocument.count()]
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def create = {
        def batchCreationDocumentInstance = new BatchCreationDocument()
        batchCreationDocumentInstance.properties = params
        return [batchCreationDocumentInstance: batchCreationDocumentInstance]
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def save = {

        def batchCreationConfig = BatchCreationConfig.get(params.batchCreationConfig.id)
        if (batchCreationConfig){
            
            if (params?.documentLocation) {
                def batchCreationDocumentInstance = new BatchCreationDocument(params)

                params?.dataSets?.id?.each{
                    def dataSet = DataSetType.get(it)
                    if (dataSet) {
                        batchCreationDocumentInstance.addToDataSets(dataSet).save()
                    }
                }

                if (batchCreationDocumentInstance.save(flush: true)) {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument'), batchCreationDocumentInstance.id])}"
                    redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationConfig.id)
                } else {
                    flash.message = "failed to save new document"
                    redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationConfig.id)
                }
            } else {
                flash.message = "Document location is required. New document was not saved."
                redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationConfig.id)
            }
        } else {
            redirect(controller:"batchCreationConfig", action:"list")
        }
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
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

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
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

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def update = {
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

            // ngp; save dataSets to the batchCreationDocumentInstance
            batchCreationDocumentInstance.dataSets = []
            params?.dataSets?.id?.each{
                def dataSet = DataSetType.get(it)
                if (dataSet) {
                    batchCreationDocumentInstance.addToDataSets(dataSet).save()
                }
            }

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

	def showComment = {
        def batchCreationDocumentInstance = BatchCreationDocument.read(params.id)
		log.debug "called showComment(${params.id})"
        if (batchCreationDocumentInstance) {
			log.debug "rendering batchCreationDocumentInstance..."
            render(template:'/batchCreationDocument/comment', bean:batchCreationDocumentInstance )
        } else {
			log.error "Invalid BatchCreationDocument.id: ${params.id}"
			render ""
        }
	}

	def editComment = {
        def batchCreationDocumentInstance = BatchCreationDocument.read(params.id)
		log.debug "called editComment(${params.id})"

        if (batchCreationDocumentInstance) {
			log.debug "rendering batchCreationDocumentInstance..."
            render(template:'/batchCreationDocument/editComment', model:[batchCreationDocument:batchCreationDocumentInstance] )
        } else {
			render "Invalid BatchCreationDocument.id: ${params.id}"
        }
	}

	def updateComment = {
		log.debug "updateComment(${params.comment})"

		def batchCreationDocumentInstance = BatchCreationDocument.get(params.id)
        if (batchCreationDocumentInstance) {
			batchCreationDocumentInstance.comment = params.comment
			if (batchCreationDocumentInstance.save(flush:true)) {
				render(template:"/batchCreationDocument/comment", bean:batchCreationDocumentInstance)
			} else {
				log.debug batchCreationDocumentInstance.errors
				render "Save failed."
			}
        } else {
			render "Invalid BatchCreationDocument.id: ${params.id}"
        }
	}
}
