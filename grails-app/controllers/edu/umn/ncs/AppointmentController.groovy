package edu.umn.ncs
import org.joda.time.*

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_PROTECTED'])
class AppointmentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def authenticateService

    def index = {
        def personInstance = Person.read(params?.person?.id)
		
		[ personInstance: personInstance ]
    }

    def list = {
		def personInstance = Person.read(params?.person?.id)
		def appointmentInstanceList = Appointment.findAllByPerson(personInstance)
		
        [appointmentInstanceList: appointmentInstanceList]
    }
	
	// TODO: This is not done, but should be finished later on
	def calendar = {
		// There is no "calendar.gsp", only "month.gsp, week.gsp, and day.gsp)
		
		
		def format = "month"
		if (params?.format == "week") { format = "week" }
		if (params?.format == "day") { format = "day" }
		
		def refDate = new Date()
		if (params.refDate) { refDate = params.refDate }

		def startDate
		def endDate 

		def now = new LocalDate(refDate.time)
		def midnight = new LocalTime(0, 0)
		def endOfDay = new LocalTime(11, 59, 59)
		
		if (format == "day") {		
			startDate = now.toDateTime(midnight).toCalendar().time
			endDate = now.toDateTime(endOfDay).toCalendar().time
		} else if (format == "week") {
			// TODO: Fix
			startDate = now.toDateTime(midnight).toCalendar().time
			endDate = now.addDays(6).toDateTime(endOfDay).toCalendar().time
		} else {
			// format == "month"
			def firstDayOfMonth = now.minusDays(now.dayOfMonth - 1)
			def endOfMonth = now.minusDays(now.dayOfMonth).plusMonths(1)
			
			startDate = firstDayOfMonth.toDateTime(midnight).toCalendar().time
			endDate = endOfMonth.toDateTime(endOfDay).toCalendar().time
		}
		
		
		def c = Appointment.createCriteria()
		def appointmentInstanceList = c.list{
			and{
				gt("startTime", startDate)
				lt("startTime", endDate)
			}
			order("startTime")
		}
		
		def model = [ appointmentInstanceList: appointmentInstanceList ]
		
		render(view: format, model: model)		
	}

    def create = {
		
		def user = authenticateService.principal()
		
		def tomorrow = new LocalDate()
		tomorrow = tomorrow.plusDays(1)
		def tenAm = new LocalTime(10, 0)
		// convert tommorrow at ten AM to a Java Date
		def startTime = tomorrow.toDateTime(tenAm).toCalendar().getTime()
		def userFullName = user.getFullName() 
		
        def appointmentInstance = new Appointment(billable:false, 
			generateLetter:true, startTime: startTime, endTime: null, 
			scheduledBy:userFullName, followUpAppointment: false)
		
		
        appointmentInstance.properties = params
		
		// This is for referencing previous appointments
		def personInstance = Person.read(params?.person?.id)
		def appointmentInstanceList = Appointment.findAllByPerson(personInstance)
		
        return [ appointmentInstance: appointmentInstance, 
			appointmentInstanceList: appointmentInstanceList ]
    }

	def reschedule = {
		def refAppointmentInstance = Appointment.read(params.id)
		
		if (refAppointmentInstance) {
			def appointmentInstanceList = Appointment.findAllById(refAppointmentInstance.id)
			
			def appointmentInstance = new Appointment()
			// Copy all the settings over
			

			appointmentInstance.startTime = refAppointmentInstance.startTime
			appointmentInstance.endTime = refAppointmentInstance.endTime
			appointmentInstance.person = refAppointmentInstance.person
			appointmentInstance.dwellingUnit = refAppointmentInstance.dwellingUnit
			appointmentInstance.study = refAppointmentInstance.study
			appointmentInstance.type = refAppointmentInstance.type
			appointmentInstance.billable = refAppointmentInstance.billable
			appointmentInstance.location = refAppointmentInstance.location
			appointmentInstance.sequenceNumber = refAppointmentInstance.sequenceNumber
			appointmentInstance.followUpAppointment = refAppointmentInstance.followUpAppointment
			appointmentInstance.generateLetter = refAppointmentInstance.generateLetter
			appointmentInstance.letter = null
			appointmentInstance.parentAppointment = refAppointmentInstance
			
			render(view: "create", model: [appointmentInstance: appointmentInstance, 
				appointmentInstanceList: appointmentInstanceList, 
				reschedule: true])
		} else {
			flash.message "Appointment Not Found."
			redirect(action: "index")
		}
		
	}
	
    def save = {
		
		def user = authenticateService.principal()
		
        def appointmentInstance = new Appointment(params)
		if ( ! appointmentInstance.followUpAppointment) {
			appointmentInstance.followUpAppointment = false
		}
		appointmentInstance.userCreated = user.username
		
        if (appointmentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'appointment.label', default: 'Appointment'), appointmentInstance.id])}"
            redirect(action: "edit", id: appointmentInstance.id)
        }
        else {
            render(view: "create", model: [appointmentInstance: appointmentInstance])
        }
    }

    def show = {
        def appointmentInstance = Appointment.get(params.id)
        if (!appointmentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "index")
        }
        else {
            [appointmentInstance: appointmentInstance]
        }
    }

    def edit = {
        def appointmentInstance = Appointment.read(params.id)
				
        if (!appointmentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "index")
        }
        else {

			def personInstance = Person.read(appointmentInstance?.person?.id)
			
			def dwellingUnitInstance = DwellingUnit.read(appointmentInstance?.dwellingUnit?.id)
			def appointmentInstanceList = []
			if (personInstance) {
				appointmentInstanceList = Appointment.findAllByPersonAndIdNot(personInstance, appointmentInstance.id)
			} else if (dwellingUnitInstance) {
				appointmentInstanceList = Appointment.findAllByDwellingUnitAndIdNot(dwellingUnitInstance, appointmentInstance.id)
			}
			
			// Find all the AppointmentTypeTypes available for this appointment type
			def c = AppointmentDetailType.createCriteria()
			def appointmentDetailTypeInstanceList = c.list{
				appointmentTypes {
					eq("id", appointmentInstance?.type?.id)
				}
			}			
			
			def incentiveTypeInstanceList = IncentiveType.list()
	
            return [appointmentInstance: appointmentInstance, 
				incentiveTypeInstanceList: incentiveTypeInstanceList,
				appointmentInstanceList: appointmentInstanceList,
				appointmentDetailTypeInstanceList: appointmentDetailTypeInstanceList ]
        }
    }
	
	def result = {
		def appointmentInstance = Appointment.read(params.id)
    	def resultList = AppointmentResult.list()
		
		if (appointmentInstance) {
			[ appointmentInstance: appointmentInstance,
				resultList: resultList ]
		} else {
			render "Invalid Appointment ID"
		}
	}

    def update = {
        def appointmentInstance = Appointment.get(params.id)
        if (appointmentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (appointmentInstance.version > version) {
                    
                    appointmentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'appointment.label', default: 'Appointment')] as Object[], "Another user has updated this Appointment while you were editing")
                    render(view: "edit", model: [appointmentInstance: appointmentInstance])
                    return
                }
            }
            appointmentInstance.properties = params
            if (!appointmentInstance.hasErrors() && appointmentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'appointment.label', default: 'Appointment'), appointmentInstance.id])}"
				
				redirect(action: "index", params: ['person.id': appointmentInstance.person.id ])
            }
            else {
                render(view: "edit", model: [appointmentInstance: appointmentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "index")
        }
    }

    def delete = {
        def appointmentInstance = Appointment.get(params.id)
        if (appointmentInstance) {
            try {
				def personInstance = appointmentInstance.person
                appointmentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
				redirect(action: "index", params: ['person.id': personInstance.id ])
                
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "index")
        }
    }
}
