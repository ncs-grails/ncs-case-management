package edu.umn.ncs

import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS'])
class EventReportController {
	//def debug = true		
	def debug = false		
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
		def personInstance = Person.read(params?.person?.id)			
		
		[ personInstance: personInstance ]
    }

    def list = {
        def personInstance = Person.read(params?.person?.id)

		def eventReportInstanceList = null
		if (personInstance) {
			eventReportInstanceList = EventReport.findAllByPerson(personInstance)
		} else {
			flash.message "couldn't find person: ${params?.person?.id}"
		}

        [personInstance: personInstance, eventReportInstanceList: eventReportInstanceList]
    }

    def create = {
		
		def personInstance = Person.read(params?.person?.id)			

		if (personInstance) {
			def eventReportInstance = new EventReport()
			eventReportInstance.properties = params
			return [personInstance: personInstance, eventReportInstance: eventReportInstance]
		}
		else {
			flash.message "couldn't find person: ${params?.person?.id}"
		}
    }

    def save = {
		if (debug) {
			println "filledOutDate::${params.filledOutDate}"			
		}
		// Convert date strings to dates
		def filledOutDate = new Date(params.filledOutDate)
		def contactDate = new Date(params.contactDate)
		params.filledOutDate = filledOutDate
		params.contactDate = contactDate
		// Set event source other text if necessary
		def eventSourceInstance = EventSource.read(params?.eventSource?.id)
		if (eventSourceInstance?.name != 'Other') {
			params.eventSourceOther = null
		}
		
        def eventReportInstance = new EventReport(params)
        if (eventReportInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eventReport.label', default: 'EventReport'), eventReportInstance.id])}"
            redirect(action: "edit", id: eventReportInstance.id)
        }
        else {
            render(view: "create", model: [eventReportInstance: eventReportInstance])
        }
    }

    def show = {
        def eventReportInstance = EventReport.get(params.id)
        if (!eventReportInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventReport.label', default: 'EventReport'), params.id])}"
            redirect(action: "list")
        }
        else {
            [eventReportInstance: eventReportInstance]
        }
    }

    def edit = {
		def eventReportInstance = EventReport.read(params.id)
		
        if (!eventReportInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventReport.label', default: 'EventReport'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [eventReportInstance: eventReportInstance]
        }
    }

    def update = {
        def eventReportInstance = EventReport.get(params.id)
        if (eventReportInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (eventReportInstance.version > version) {
                    
                    eventReportInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'eventReport.label', default: 'EventReport')] as Object[], "Another user has updated this EventReport while you were editing")
                    render(view: "edit", model: [eventReportInstance: eventReportInstance])
                    return
                }
            }
			// Convert date strings to dates
			def filledOutDate = new Date(params.filledOutDate)
			def contactDate = new Date(params.contactDate)
			params.filledOutDate = filledOutDate
			params.contactDate = contactDate
			// Set event source other text if necessary
			def eventSourceInstance = EventSource.read(params?.eventSource?.id)
			if (eventSourceInstance?.name != 'Other') {
				params.eventSourceOther = null
			}
            eventReportInstance.properties = params
            if (!eventReportInstance.hasErrors() && eventReportInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'eventReport.label', default: 'EventReport'), eventReportInstance.id])}"
				redirect(action: "edit", id: eventReportInstance.id)
            }
            else {
                render(view: "edit", model: [eventReportInstance: eventReportInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventReport.label', default: 'EventReport'), params.id])}"
            redirect(action: "index")
        }
    }

    def delete = {
        def eventReportInstance = EventReport.get(params.id)
        if (eventReportInstance) {
            try {
                eventReportInstance.delete(flush: true)
                flash.message = "Event report deleted successfully!"
                redirect(action: "index")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'eventReport.label', default: 'EventReport'), params.id])}"
				redirect(action: "edit", id: eventReportInstance.id)
            }
        }
        else {
            flash.message = "Event report not found"
            redirect(action: "index")
        }
    }
	
}
