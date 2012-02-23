package edu.umn.ncs
// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class BatchCreationConfigController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def form = {}
	
    def list = {
        [batchCreationConfigInstanceList: BatchCreationConfig.list(params)]
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def create = {
        def batchCreationConfigInstance = new BatchCreationConfig()
        batchCreationConfigInstance.recipients = [ 0 ]
        batchCreationConfigInstance.properties = params
        return [batchCreationConfigInstance: batchCreationConfigInstance]
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def save = {

        log.debug "Save Action. batchCreationConfigInstance. params: ${params}"
        def batchCreationConfigInstance = new BatchCreationConfig(params)
        if (batchCreationConfigInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), batchCreationConfigInstance.id])}"
            redirect(action: "edit", id: batchCreationConfigInstance.id)
        }
        else {
            render(view: "create", model: [batchCreationConfigInstance: batchCreationConfigInstance])
        }
    }

	private def getEditModel(batchCreationConfigInstance) {

        // All instruments
        def instrumentInstanceList = Instrument.list()
        def instrumentFormatInstanceList = InstrumentFormat.list()
        def usedInstruments = []
        def unusedInstruments = []
        def attachableInstruments = []
        
        if (batchCreationConfigInstance) {
            // config instrument
            def bccInstruments = [ batchCreationConfigInstance.instrument ]

            def attachmentOf = BatchCreationItemRelation.findByName('attachment')
            def childOf = BatchCreationItemRelation.findByName('child')
            def sisterOf = BatchCreationItemRelation.findByName('sister')


            // items instruments
            def bciInstrumentList = batchCreationConfigInstance.subItems.collect{ it.instrument }
            // log.debug "bciInstrumentList :: ${bciInstrumentList}"
            // sister/child instruments
            def subInstrumentList = batchCreationConfigInstance.subItems.findAll{it.relation.id != attachmentOf.id}.collect{ it.instrument }
            // log.debug "subInstrumentList :: ${subInstrumentList}"

            usedInstruments = bciInstrumentList + bccInstruments
            attachableInstruments = subInstrumentList + bccInstruments
            // log.debug "usedInstruments :: ${usedInstruments}"
            // log.debug "attachableInstruments :: ${attachableInstruments}"

            def sql = "from Instrument as i where (i.id not in (:ui))"
            unusedInstruments = Instrument.findAll(sql, [ui:usedInstruments.collect{it.id}])
            // log.debug "unusedInstruments :: ${attachableInstruments}"
        }

        // Save / Delete
        // - All Instruments except those in items and the one in current
        //   batchCreationConfig plus the current item
        // - All instruments in batchCreationConfig and items except the current item

        // New
        // - All Instruments except those in items and the one in current batchCreationConfig
        // ! only show this section if the above list is not empty
        // - All instruments in batchCreationConfig and items

            return [batchCreationConfigInstance: batchCreationConfigInstance,
                instrumentInstanceList:instrumentInstanceList,
                instrumentFormatInstanceList: instrumentFormatInstanceList,
                usedInstruments:usedInstruments,
                unusedInstruments:unusedInstruments,
                attachableInstruments:attachableInstruments]

	}

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def edit = {
        def batchCreationConfigInstance = BatchCreationConfig.read(params.id)

        if (!batchCreationConfigInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), params.id])}"
            redirect(action: "list")
        }
        else {
            return getEditModel(batchCreationConfigInstance)
        }
    }

	@Secured(['ROLE_NCS_DOCGEN_MANAGE'])
    def update = {

        def batchCreationConfigInstance = BatchCreationConfig.get(params.id)
        if (batchCreationConfigInstance) {

            // save the old selection query information for later...
            def oldQuery = batchCreationConfigInstance.selectionQuery

            if (params.version) {
                def version = params.version.toLong()
                if (batchCreationConfigInstance.version > version) {
                    
                    batchCreationConfigInstance.errors.rejectValue("version", "default.optimistic.locking.failure", 
                        [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig')] as Object[],
                        "Another user has updated this BatchCreationConfig while you were editing")
                    render(view: "edit", model: getEditModel(batchCreationConfigInstance) )
                    return
                }
            }

            batchCreationConfigInstance.properties = params
            // if the old query wasn't null, and the new query is different....
            if (oldQuery != batchCreationConfigInstance.selectionQuery && oldQuery) {
                // add the query to the archived queries
                batchCreationConfigInstance.addToArchivedQueries(selectionQuery: oldQuery)
            }

            if (params?.trackingDocumentRecipient){
                batchCreationConfigInstance.recipients = []
                params.trackingDocumentRecipient.each{
                    def trackingDocumentRecipient = TrackingDocumentRecipient.get(it)
                    if (trackingDocumentRecipient){
                        batchCreationConfigInstance.addToRecipients(trackingDocumentRecipient)
                    }
                }
            }

            if (!batchCreationConfigInstance.hasErrors() && batchCreationConfigInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), batchCreationConfigInstance.id])}"
                redirect(action: "edit", id: batchCreationConfigInstance.id)
            }
            else {
			    batchCreationConfigInstance.errors.each{
					log.debug "error saving batchCreationConfigInstance:${it} "
				}
                render(view: "edit", model: getEditModel(batchCreationConfigInstance) )
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig'), params.id])}"
            redirect(action: "list")
        }
    }

	def showDescription = {
        def batchCreationConfigInstance = BatchCreationConfig.read(params.id)
		log.debug "called showDescription(${params.id})"
        if (batchCreationConfigInstance) {
			log.debug "rendering batchCreationConfigInstance..."
            render(template:'/batchCreationConfig/description', bean:batchCreationConfigInstance )
        } else {
			log.error "Invalid BatchCreationConfig.id: ${params.id}"
			render ""
        }
	}

	def editDescription = {
        def batchCreationConfigInstance = BatchCreationConfig.read(params.id)
		log.debug "called editDescription(${params.id})"

        if (batchCreationConfigInstance) {
			log.debug "rendering batchCreationConfigInstance..."
            render(template:'/batchCreationConfig/editDescription', model:[batchCreationConfig:batchCreationConfigInstance] )
        } else {
			render "Invalid BatchCreationConfig.id: ${params.id}"
        }
	}

	def updateDescription = {
		log.debug "updateDescription(${params.description})"

		def batchCreationConfigInstance = BatchCreationConfig.get(params.id)
		batchCreationConfigInstance.description = params.description
		if (batchCreationConfigInstance.save(flush:true)) {
			render(template:"/batchCreationConfig/description", bean:batchCreationConfigInstance)
		} else {
			render "Save failed."
		}
	}

}
