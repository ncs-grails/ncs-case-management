package edu.umn.ncs

class BatchCreationConfigController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

	def form = {}
	
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [batchCreationConfigInstanceList: BatchCreationConfig.list(params), batchCreationConfigInstanceTotal: BatchCreationConfig.count()]
    }

    def create = {
        def batchCreationConfigInstance = new BatchCreationConfig()
        batchCreationConfigInstance.properties = params
        return [batchCreationConfigInstance: batchCreationConfigInstance]
    }

    def save = {
        def batchCreationConfigInstance = new BatchCreationConfig(params)
        if (batchCreationConfigInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), batchCreationConfigInstance.id])}"
            redirect(action: "edit", id: batchCreationConfigInstance.id)
        }
        else {
            render(view: "create", model: [batchCreationConfigInstance: batchCreationConfigInstance])
        }
    }

    def edit = {
        def batchCreationConfigInstance = BatchCreationConfig.get(params.id)

        def instrumentInstanceList = Instrument.list()
        
        if (!batchCreationConfigInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [batchCreationConfigInstance: batchCreationConfigInstance,
                instrumentInstanceList:instrumentInstanceList]
        }
    }

    def update = {
        def batchCreationConfigInstance = BatchCreationConfig.get(params.id)

        if (batchCreationConfigInstance) {

			// save the old selection query information for later...
			def oldQuery = batchCreationConfigInstance.selectionQuery

            if (params.version) {
                def version = params.version.toLong()
                if (batchCreationConfigInstance.version > version) {
                    
                    batchCreationConfigInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig')] as Object[], "Another user has updated this BatchCreationConfig while you were editing")
                    render(view: "edit", model: [batchCreationConfigInstance: batchCreationConfigInstance])
                    return
                }
            }

            batchCreationConfigInstance.properties = params
			// if the old query wasn't null, and the new query is different....
			if (oldQuery != batchCreationConfigInstance.selectionQuery && oldQuery) {
				// add the query to the archived queries
				batchCreationConfigInstance.addToArchivedQueries(selectionQuery: oldQuery)
			}

            if (!batchCreationConfigInstance.hasErrors() && batchCreationConfigInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), batchCreationConfigInstance.id])}"
                redirect(action: "edit", id: batchCreationConfigInstance.id)
            }
            else {
                render(view: "edit", model: [batchCreationConfigInstance: batchCreationConfigInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), params.id])}"
            redirect(action: "list")
        }
    }
}
