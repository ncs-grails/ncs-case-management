package edu.umn.ncs

import grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

@Secured(['ROLE_NCS_IT','ROLE_NCS'])
class EventReportController {
	def debug = false		
	def springSecurityService
	
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
		// get the username of the logged in user
        def username = springSecurityService?.principal?.getUsername()

		def eventReportInstance = new EventReport()
		eventReportInstance.properties = params
		if ( ! eventReportInstance.filledOutBy ) {
			eventReportInstance.filledOutBy = username
		}
		eventReportInstance.person = Person.read(params?.person?.id)
		// Add NCS
		eventReportInstance.addToStudies(Study.read(1))			

		if (eventReportInstance.person) {
			return [eventReportInstance: eventReportInstance]
		}
		else {
			flash.message "couldn't find person: ${params?.person?.id}"
		}
    }

    def save = {
		
		def formatter = DateTimeFormat.forPattern('M/d/yyyy')
		def midnight = new LocalTime(0,0)
		
		if (debug) {
			println "filledOutDate::${params.filledOutDate}"			
			println "filledOutDate::${params.contactDate}"			
		}
		
		// TODO: Convert date strings to dates
		def filledOutDate = null
		def contactDate = null
		
		// first replace . and / with -
		if (params.filledOutDate) {
			params.filledOutDate = params.filledOutDate.replaceAll('-', '/')
			// parse it using the Joda Date parser, then convert back to Java Date and save
			params.filledOutDate = formatter.parseDateTime(params.filledOutDate).toLocalDate().toDateTime(midnight).toCalendar().time
		}
		if (params.contactDate) {
			params.contactDate = params.contactDate.replaceAll('-', '/')
			// parse it using the Joda Date parser, then convert back to Java Date and save
			params.contactDate = formatter.parseDateTime(params.contactDate).toLocalDate().toDateTime(midnight).toCalendar().time
		}		

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
		def personInstance = eventReportInstance.person 
        if (eventReportInstance) {
            try {
                eventReportInstance.delete(flush: true)
                flash.message = "Event report deleted successfully!"
                redirect(action: "index", params: ['person.id': personInstance.id])
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
