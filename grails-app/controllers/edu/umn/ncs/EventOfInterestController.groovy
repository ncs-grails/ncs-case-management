package edu.umn.ncs

import grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT','ROLE_NCS'])
class EventOfInterestController {
	def debug = true

	def springSecurityService	
	def eventTriggerService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [eventOfInterestInstanceList: EventOfInterest.list(params), eventOfInterestInstanceTotal: EventOfInterest.count()]
    }

    def create = {
		// get the username of the logged in user
		def username = springSecurityService?.principal?.getUsername()
		
        def eventOfInterestInstance = new EventOfInterest()
        eventOfInterestInstance.properties = params
        return [eventOfInterestInstance: eventOfInterestInstance]
    }

    def save = {
        /*def eventOfInterestInstance = new EventOfInterest(params)
		
        if (eventOfInterestInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eventOfInterest.label', default: 'EventOfInterest'), eventOfInterestInstance.id])}"
            redirect(action: "show", id: eventOfInterestInstance.id)
        }
        else {
            render(view: "create", model: [eventOfInterestInstance: eventOfInterestInstance])
        } */

		if (debug) {
			println "eventReport.id::${params.eventReport.id}"
		}
		def eventReportInstance = EventReport.get(params.eventReport.id)
		
		// get the username of the logged in user
		def username = springSecurityService?.principal?.getUsername()
		
		if (eventReportInstance){
			// Set the event report to which the eoi belongs
			params.eventReport = eventReportInstance
			// Get the eoi count
			def eoiCount = params.eoiCount
			// Handle event dates
			def elementName = "eventDate${eoiCount}"
			if (debug) {
				println "elementName::${elementName}"
				println "Converting " + params."${elementName}" + " to date"
			}			
			def eventDate = params."${elementName}"
			if (eventDate){
				try {
					eventDate.replaceAll('-','/')
					// Attempt to convert string to a date
					eventDate = new Date(params."${elementName}")
					params.eventDate = eventDate
					params.datePrecision = 'day'
				}
				catch (Exception e) {
					// If not a valid date, check for date precision
					elementName = "datePrecision"
					if (params."${elementName}") {
						def datePrecision = params."${elementName}"
						if (debug) {
							println "datePrecision::${datePrecision}"
						}
						if (datePrecision == 'year') {
							if (eventDate ==~ /^(19|20)\d\d/) {
								eventDate = new Date("06/15/${eventDate}")
								params.eventDate = eventDate
							}
							else {
								flash.message = "Event Date value: ${eventDate} with year precision is invalid for event of interest. Expected format: YYYY"
								redirect(controller:"eventReport", action: "edit", id: eventReportInstance.id)
								return
							}
						}
						if (datePrecision == 'month') {
							if (eventDate ==~ /\d+\/(19|20)\d\d/) {
								eventDate = new Date("${eventDate[0..1]}/15/${eventDate[3..eventDate.size() - 1]}")
								params.eventDate = eventDate
							}
							else {
								flash.message = "Event Date value: ${eventDate} with month precision is invalid for event of interest. Expected format: MM/YYYY"
								redirect(controller:"eventReport", action: "edit", id: eventReportInstance.id)
								return
							}
						}
					}
					else {
						flash.message = "Event Date value: ${eventDate} is invalid for event of interest."
						redirect(controller:"eventReport", action: "edit", id: eventReportInstance.id)
						return
					}
				}
			}
			elementName = "dateResultEntered${eoiCount}"
			if (params."${elementName}"){
				def dateResultEntered = new Date(params."${elementName}")
				params.dateResultEntered = dateResultEntered
			}
			if (debug) {
				println "dateResultEntered::${params.dateResultEntered}"
			}
			elementName = "eventResultDate${eoiCount}"
			if (params."${elementName}"){
				def eventResultDate = new Date(params."${elementName}")
				params.eventResultDate = eventResultDate
			}
			
			if (debug) {
				println "eventOfInterestInstance.params::${params}"
			}
			
			params.userCreated = username
			params.userUpdated = username
			
			def eventOfInterestInstance = new EventOfInterest(params)

			if (debug) {
				println "eventOfInterestInstance::${eventOfInterestInstance.properties}"
			}

			if (eventOfInterestInstance.save(flush: true)) {
				eventReportInstance.addToEvents(eventOfInterestInstance).save()
				
				// process any triggers to run on event creation
				eventTriggerService.processEventTriggers(username)
				
				flash.message = "Event of Interest id: ${eventOfInterestInstance.id} successfuly saved."
				redirect(controller: "eventReport", action: "edit", id: eventReportInstance.id)
			}
			else {
				flash.message = "Failed to save new event of interest."
				redirect(controller: "eventReport", action: "edit", id: eventReportInstance.id)
			}
		} else {
			redirect(controller: "eventReport", action: "list", id: eventReportInstance?.person.id)
		}

    }

    def show = {
        def eventOfInterestInstance = EventOfInterest.get(params.id)
        if (!eventOfInterestInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventOfInterest.label', default: 'EventOfInterest'), params.id])}"
            redirect(action: "list")
        }
        else {
            [eventOfInterestInstance: eventOfInterestInstance]
        }
    }

    def edit = {
        def eventOfInterestInstance = EventOfInterest.get(params.id)
        if (!eventOfInterestInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eventOfInterest.label', default: 'EventOfInterest'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [eventOfInterestInstance: eventOfInterestInstance]
        }
    }

    def update = {
		
		// get the username of the logged in user
		def username = springSecurityService?.principal?.getUsername()

        def eventOfInterestInstance = EventOfInterest.get(params.eventOfInterestInstance_id)
		def eventReportInstance = EventReport.read(eventOfInterestInstance.eventReport.id)
        if (eventOfInterestInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (eventOfInterestInstance.version > version) {
                    
                    eventOfInterestInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'eventOfInterest.label', default: 'EventOfInterest')] as Object[], "Another user has updated this EventOfInterest while you were editing")
					redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
                    return
                }
            }
			// Get the eoi count
			def eoiCount = params."eoiId${eventOfInterestInstance?.id}"
			// Handle event dates
			def elementName = "eventDate${eoiCount}"
			def eventDate = params."${elementName}"
			if (params."${elementName}"){				
				try {
					eventDate.replaceAll('-','/')
					// Attempt to convert string to a date
					eventDate = new Date(params."${elementName}")
					params.eventDate = eventDate
					params.datePrecision = 'day'
				}
				catch (Exception e) {
					// If not a valid date, check for date precision
					elementName = "datePrecision"
					if (params."${elementName}"){
						def datePrecision = params."${elementName}"
						if (datePrecision == 'year') {
							if (eventDate ==~ /^(19|20)\d\d/) {
								eventDate = new Date("06/15/${eventDate}")
								params.eventDate = eventDate
							}
							else {
								flash.message = "Event Date value: ${eventDate} with year precision is invalid for event of interest: ${eventOfInterestInstance?.eventType}. Expected format: YYYY"
								redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
								return
							}
						}
						if (datePrecision == 'month') {
							if (eventDate ==~ /\d+\/(19|20)\d\d/) {
								eventDate = new Date("${eventDate[0..1]}/15/${eventDate[3..eventDate.size() - 1]}")
								params.eventDate = eventDate
							}
							else {
								flash.message = "Event Date value: ${eventDate} with month precision is invalid for event of interest: ${eventOfInterestInstance?.eventType}. Expected format: MM/YYYY"
								redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
								return
							}
						}
					}
					else {
						flash.message = "Event Date value: ${eventDate} is invalid for event of interest: ${eventOfInterestInstance?.eventType}"
						redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
						return
					}
				}
			}
			else {
				params.eventDate = null				
			}
			elementName = "dateResultEntered${eoiCount}"
			if (params."${elementName}"){
				def dateResultEntered = new Date(params."${elementName}")
				params.dateResultEntered = dateResultEntered
			}
			else {
				params.dateResultEntered = null				
			}
			elementName = "eventResultDate${eoiCount}"
			if (params."${elementName}"){
				def eventResultDate = new Date(params."${elementName}")
				params.eventResultDate = eventResultDate
			}
			else {
				params.eventResultDate = null				
			}
            eventOfInterestInstance.properties = params
            if (!eventOfInterestInstance.hasErrors() && eventOfInterestInstance.save(flush: true)) {
				
				// process any triggers to run on event update
				eventTriggerService.processEventTriggers(username)

                flash.message = "Event of interest updated successfully!"
				redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
            }
            else {
				flash.message = "Errors saving event of interest."
				redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
            }
        }
        else {
            flash.message = "Item not found. Event of interest id:${params.eventOfInterestInstance_id}"
            redirect(controller:"eventReport", action: "list")
        }
    }

    def delete = {
        def eventOfInterestInstance = EventOfInterest.get(params.eventOfInterestInstance_id)
        if (eventOfInterestInstance) {
            try {
                eventOfInterestInstance.delete(flush: true)
                flash.message = "Event of interest deleted"
				redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Failed to delete event of interest. EOI id: ${params.eventOfInterestInstance_id}"
				redirect(controller:"eventReport", action: "edit", id: eventOfInterestInstance.eventReport.id)
            }
        }
        else {
            flash.message = "Event of interest id: ${params.eventOfInterestInstance_id} not found."
            redirect(controller:"eventReport", action: "list")
        }
    }
}
