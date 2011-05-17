package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT'])
class MailingScheduleController {
	
    def index = { 
        redirect(action:list, params:params)
    }

    def list = {

        //println "App Name: ${grailsApplication.metadata['app.name']}"
        //println "params: ${params}"

        // pull the instrument if it was chosen from the pulldown
        def instrumentInstance = Instrument.get(params?.instrument?.id)

        // if no instrument was chosen, let's pick a random one
        if (! instrumentInstance) {
            // first we'll try to find one used by a schedule...
            def ms = MailingSchedule.findByQuotaGreaterThan(0)
            if (ms) {
                // pull the instrument from the first mailing schedule
                instrumentInstance = ms.instrument
            } else {
                // I guess there's no schedule yet.
                // grab the first instrument (with a name... yeah I know, all of them have names)
                instrumentInstance = Instrument.findByNameLike('%')
            }
        }

        // get a list of all the instruments to put on the pulldown
        def instrumentInstanceList = Instrument.list()

        // this is a blank one for filling out the "new" form at the bottom
        def mailingScheduleInstance = new MailingSchedule()

        // list only the schedule for the instrument selected
        // order by checkpointDate
        def mailingScheduleList

		if (instrumentInstance) {
			
			mailingScheduleList = MailingSchedule.findAllByInstrument(instrumentInstance).sort{ it.checkpointDate }
			
	        // figure out the max checkpoint date
	        def c = MailingSchedule.createCriteria()
	        def results = c.list{
	            instrument {
	                eq("id", instrumentInstance.id)
	            }
	            order("checkpointDate", "desc")
	        }
	
	        // set the default date to the week after the last one entered.
	        if (results) {
	            // grab the first one in the list, add 7 days.
	            mailingScheduleInstance.checkpointDate = results[0].checkpointDate + 7
	
	            // find out what the last cumulative quota was
	            def endCount = results[0].quota
	
	            // guess the number
	            if (results.size() > 1) {
	                //
	                def prevCount = results[1].quota
	
	                mailingScheduleInstance.quota = endCount + (endCount - prevCount)
	            } else {
	                // only one in the list, assume the next batch is the same size
	                mailingScheduleInstance.quota = endCount * 2
	            }
	        } else {
	
	            mailingScheduleInstance.checkpointDate = new Date() + 1
	            mailingScheduleInstance.quota = 0
	        }
		}
        
        return [mailingScheduleInstanceList: mailingScheduleList, 
            instrumentInstanceList: instrumentInstanceList,
            instrumentInstance: instrumentInstance,
            mailingScheduleInstance: mailingScheduleInstance,
            mailingScheduleInstanceTotal: mailingScheduleList?.count()]

    }

    def create = {
        def mailingScheduleInstance = new MailingSchedule()
        mailingScheduleInstance.properties = params
        [mailingScheduleInstance: mailingScheduleInstance]
    }

    def save = {
        def mailingScheduleInstance = new MailingSchedule(params)
        def instrumentInstance = Instrument.get(params?.instrument?.id)
        
        if (mailingScheduleInstance.save(flush: true)) {

            mailingScheduleInstance.refresh()

            instrumentInstance = mailingScheduleInstance.instrument
            def params = [ instrument: [ id: instrumentInstance.id ] ]
            
            flash.message = "Mailing Schedule for instrument '${mailingScheduleInstance?.instrument?.name}' created"
            redirect(action: "list", params: ['instrument.id':instrumentInstance.id] )
        } else {
            flash.message = "Could not save new mailing quota expectation.  Only one per date is allowed."
            redirect(action: "list", params: ['instrument.id':instrumentInstance.id] )
        }
    }

    def edit = {
        def mailingScheduleInstance = MailingSchedule.get(params.id)
        if (!mailingScheduleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailingSchedule.label', default: 'MailingSchedule'), params.id])}"
            redirect(action: "list")
        } else {
            [mailingScheduleInstance: mailingScheduleInstance]
        }
    }

    def update = {

        //println "params in update: ${params}"

        def instrumentInstance = Instrument.get(params?.instrument?.id)

        def mailingScheduleInstance = MailingSchedule.get(params.id)
        if (mailingScheduleInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (mailingScheduleInstance.version > params.version) {
                    redirect(controller: "mailingSchedule", action: "edit", id: mailingScheduleInstance.id)
                }
            }

            mailingScheduleInstance.properties = params
            if (!mailingScheduleInstance.hasErrors() && mailingScheduleInstance.save(flush: true)) {
                flash.message = flash.message = "Mailing Schedule for instrument '${mailingScheduleInstance?.instrument?.name}' updated."
                redirect(controller: "mailingSchedule", action: "list", params: ['instrument.id':instrumentInstance.id])
            } else {
                flash.message = "Error saving mailing schedule for instrument ${mailingScheduleInstance?.instrument?.name}."
                redirect(controller: "mailingSchedule", action: "list", params: ['instrument.id':instrumentInstance.id])
            }

        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailingSchedule.label', default: 'MailingSchedule'), params.id])}"
            redirect(controller: "mailingSchedule", action: "list")
        }
    }

    def delete = {
        def mailingScheduleInstance = MailingSchedule.get(params.id)

        if (mailingScheduleInstance) {
            try {

                def instrumentInstance = mailingScheduleInstance.instrument

                mailingScheduleInstance.delete(flush: true)
                flash.message = "Mailing Schedule for instrument '${mailingScheduleInstance?.instrument?.name}' deleted"
                redirect(controller: "mailingSchedule", action: "list", params: ['instrument.id':instrumentInstance.id])
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Failed to delete mailing schedule id: ${params.id}"
                redirect(controller: "mailingSchedule", action: "list", params: ['instrument.id':instrumentInstance.id])
            }
        } else {
            flash.message = "Mailing schedule id: ${params.id} not found."
            redirect(controller:"mailingSchedule", action: "list")
        }
    }
}
