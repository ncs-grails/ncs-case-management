package edu.umn.ncs
// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class BatchCreationItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [batchCreationItemInstanceList: BatchCreationItem.list(params), batchCreationItemInstanceTotal: BatchCreationItem.count()]
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def create = {
        def batchCreationItemInstance = new BatchCreationItem()
        batchCreationItemInstance.properties = params
        return [batchCreationItemInstance: batchCreationItemInstance]
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def save = {

        def batchCreationConfig = BatchCreationConfig.get(params.batchCreationConfig.id)

        if (batchCreationConfig){
            def batchCreationItemInstance = new BatchCreationItem(params)
            if (batchCreationItemInstance.save(flush: true)) {
                flash.message = "Item id: ${batchCreationItemInstance.id} successfuly saved."
                redirect(controller: "batchCreationConfig", action: "edit", id: batchCreationConfig.id)
            }
            else {
                flash.message = "Failed to save new item."
                redirect(controller: "batchCreationConfig", action: "edit", id: batchCreationConfig.id)
            }
        } else {
            redirect(controller: "batchCreationConfig", action: "list")
        }
    }

    def show = {
        def batchCreationItemInstance = BatchCreationItem.get(params.id)
        if (!batchCreationItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationItem.label', default: 'BatchCreationItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [batchCreationItemInstance: batchCreationItemInstance]
        }
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def edit = {
        def batchCreationItemInstance = BatchCreationItem.get(params.id)
        if (!batchCreationItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationItem.label', default: 'BatchCreationItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [batchCreationItemInstance: batchCreationItemInstance]
        }
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def update = {
        //println "${params}"

        def batchCreationItemInstance = BatchCreationItem.get(params.i_id)
        if (batchCreationItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (batchCreationItemInstance.version > version) {

                    // Why did ajz removed this line in BatchCreationDocumentController update action?
                    //batchCreationItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'batchCreationItem.label', default: 'BatchCreationItem')] as Object[], "Another user has updated this BatchCreationItem while you were editing")
                    redirect(controller:"batchCreationConfig", action:"edit", id:batchCreationItemInstance.batchCreationConfig.id)
                    return
                }
            }

            batchCreationItemInstance.properties = params
            if (!batchCreationItemInstance.hasErrors() && batchCreationItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batchCreationItem.label', default: 'BatchCreationItem'), batchCreationItemInstance.id])}"
                redirect(controller:"batchCreationConfig", action: "edit", id: batchCreationItemInstance.id)
            }
            else {
                flash.message = "Errors saving item."
                redirect(controller:"batchCreationConfig", action: "edit", id:batchCreationItemInstance.batchCreationConfig.id)
            }
        }
        else {
            flash.message = "Item not found. Item id:${params.i_id}"
            redirect(controller:"batchCreationConfig", action: "list")
        }
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def delete = {

        println "${params}"

        def batchCreationItemInstance = BatchCreationItem.get(params.i_id)
        if (batchCreationItemInstance) {
            try {
                batchCreationItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'batchCreationItem.label', default: 'BatchCreationItem'), params.i_id])}"
                redirect(controller:"batchCreationConfig", action: "edit", id: batchCreationItemInstance.batchCreationConfig.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Failed to delete item. Item id: ${params.i_id}}"
                redirect(controller:"batchCreationConfig", action: "edit", id: batchCreationItemInstance.batchCreationConfig.id)
            }
        }
        else {
            flash.message = "Item id: ${params.i_id} not found."
            redirect(controller:"batchCreationConfig", action: "list")
        }
    }

	def showComment = {
        def batchCreationItemInstance = BatchCreationItem.read(params.id)
		log.debug "called showComment(${params.id})"
        if (batchCreationItemInstance) {
			log.debug "rendering batchCreationItemInstance..."
            render(template:'/batchCreationItem/comment', bean:batchCreationItemInstance )
        } else {
			log.error "Invalid BatchCreationItem.id: ${params.id}"
			render ""
        }
	}

	def editComment = {
        def batchCreationItemInstance = BatchCreationItem.read(params.id)
		log.debug "called editComment(${params.id})"

        if (batchCreationItemInstance) {
			log.debug "rendering batchCreationItemInstance..."
            render(template:'/batchCreationItem/editComment', model:[batchCreationItem:batchCreationItemInstance] )
        } else {
			render "Invalid BatchCreationItem.id: ${params.id}"
        }
	}

	def updateComment = {
		log.debug "updateComment(${params.comment})"

		def batchCreationItemInstance = BatchCreationItem.get(params.id)
        if (batchCreationItemInstance) {
			batchCreationItemInstance.comment = params.comment
			if (batchCreationItemInstance.save(flush:true)) {
				render(template:"/batchCreationItem/comment", bean:batchCreationItemInstance)
			} else {
				log.debug batchCreationItemInstance.errors
				render "Save failed."
			}
        } else {
			render "Invalid BatchCreationItem.id: ${params.id}"
        }
	}
}
