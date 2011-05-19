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
		def appointmentInstanceList = null
		if (personInstance) {
			appointmentInstanceList = Appointment.findAllByPerson(personInstance)
		} else {
			flash.message "couldn't find person: ${params?.person?.id}"
		}

        [personInstance: personInstance, appointmentInstanceList: appointmentInstanceList]
    }
	
	// TODO: This is not done, but should be finished later on
	def calendar = {
		// There is no "calendar.gsp", only "month.gsp, week.gsp, and day.gsp)
		def format = "month"
		if (params?.format == "week") { format = "week" }
		if (params?.format == "day") { format = "day" }
		
		def refDate = new Date()
		if (params.refDate) { refDate = params.refDate }

		/* monthInfo stuff
		 *   referenceDate
		 *   dayOfWeek
		 *   firstOfWeek
		 *   lastOfWeek
		 *   firstCalendarDate
		 *   firstWeekdayOfMonth
		 *   firstOfMonth
		 *   lastOfmonth
		 */
		def monthInfo = getMonthInfo(refDate) 
		
		def startDate
		def endDate
		def weeks = []
		
		if (format == "week") {
			startDate = monthInfo.firstOfWeek
			endDate = monthInfo.lastOfWeek
		} else {
			startDate = monthInfo.firstCalendarDate
			endDate = monthInfo.lastOfMonth
		}
		
		def cursorDate = startDate
		
		/*
		println "cursorDate: ${cursorDate}"
		println "startDate: ${startDate}"
		println "endDate: ${endDate}"
		*/
		
		while (cursorDate <= endDate) {
			// add all days to week
			def daysOfWeek = []
			(1..7).each{
				def day = [:]
				def cursorLocalDate = new LocalDate(cursorDate.time)
				day.dayOfMonth = cursorLocalDate.dayOfMonth
				day.date = cursorDate
				day.appointments = []
				daysOfWeek.add(day)
				
				cursorDate++
			}
			weeks.add([days:daysOfWeek])
		}
		
		def c = Appointment.createCriteria()
		def appointmentInstanceList = c.list{
			and{
				gt("startTime", startDate)
				lt("startTime", endDate)
			}
			order("startTime")
		}
		
		def model = [ weeks:weeks, 
			appointmentInstanceList: appointmentInstanceList,
			refDate: refDate ]
		
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
	
	/* this is to help out with the calendar
	 *
	 * returns a map containing the following attributes:
	 *   referenceDate
	 *   dayOfWeek
	 *   firstOfWeek
	 *   lastOfWeek
	 *   firstCalendarDate
	 *   firstWeekdayOfMonth
	 *   firstOfMonth
	 *   lastOfmonth
	 */
	private def getMonthInfo(refDate) {
		
		// convert java.util.Date to org.joda.time.LocalDate
		LocalDate referenceDate = new LocalDate(refDate.time)

		def dayOfWeek = referenceDate.getDayOfWeek()
		
		LocalDate firstOfMonth = referenceDate.withDayOfMonth(1)
		
		def lastOfmonth = referenceDate.plusMonths(1).withDayOfMonth(1).minusDays(1)

		// get first weekday of the month
		def firstWeekdayOfMonth = firstOfMonth.getDayOfWeek()

		// difference between first day on calendar and first
		// day of month is (firstOfMonth - (7 - firstWeekdayOfMonth)
		def firstCalendarDate = firstOfMonth
		if (firstWeekdayOfMonth != 7) {
			firstCalendarDate = firstOfMonth.minusDays(firstWeekdayOfMonth)
		}
		
		def firstOfWeek = referenceDate.withDayOfWeek(1).minusDays(1)
		def lastOfWeek = referenceDate.withDayOfWeek(6)

		def midnight = new LocalTime(0,0)
		
		def monthInfo = [:]
		
		monthInfo.referenceDate = referenceDate.toDateTime(midnight).toCalendar().time
		monthInfo.dayOfWeek = dayOfWeek
		monthInfo.firstOfWeek = firstOfWeek.toDateTime(midnight).toCalendar().time
		monthInfo.lastOfWeek = lastOfWeek.toDateTime(midnight).toCalendar().time
		monthInfo.firstCalendarDate = firstCalendarDate.toDateTime(midnight).toCalendar().time
		monthInfo.firstWeekdayOfMonth = firstWeekdayOfMonth
		monthInfo.firstOfMonth = firstOfMonth.toDateTime(midnight).toCalendar().time
		monthInfo.lastOfMonth = lastOfmonth.toDateTime(midnight).toCalendar().time
		
		return monthInfo
	}
}
