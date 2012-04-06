package edu.umn.ncs
import org.joda.time.*

// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_PROTECTED'])
class AppointmentController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def springSecurityService

	def index = {

		log.debug "params = ${params}"

		def personInstance = Person.read(params?.person?.id)
		log.debug "personInstance = ${personInstance}"
	
		[personInstance: personInstance] 	

	}

	def listPerPerson = {
	
		//println ("APPOINTMENT > listPerPerson > params = ${params}")
			
		def personInstance = Person.read(params?.person?.id)
		//println ("=> personInstance = ${personInstance}")
                
		def appointmentInstanceList = null
                if (personInstance) {
                        appointmentInstanceList = Appointment.findAllByPerson(personInstance)
			//println "=> appointmentInstanceList = ${appointmentInstanceList}"
                } else {
                        flash.message "couldn't find person: ${params?.person?.id}"
                }

        	[personInstance: personInstance, appointmentInstanceList: appointmentInstanceList]

	}
	
	def list = {

		//println "APPOINTMENT > list > params = ${params}"

		// get date filter
		def now = new Date()
		//println "=> now = ${now}"

		// get appointment Type
		def appointmentTypeId = params?.appointmentType?.id
		//println "=> appointmentTypeId = ${appointmentTypeId}"		
		def appointmentTypeInstance = null
		if ( appointmentTypeId ) {
			appointmentTypeInstance = AppointmentType.findWhere(id:appointmentTypeId.toLong())
		}
		//println "=> appointmentTypeInstance = ${appointmentTypeInstance}"

		// create appointment: criteria for query, list, and detail list
		def ca = Appointment.createCriteria()		
		def appointmentInstanceList
		def appointmentDetailInstanceList = []
		
		// create appointment total: criteria for query, and future count
		def cat = Appointment.createCriteria()
		def pastAppointmentTotal = null
		
		if ( appointmentTypeInstance ) {

			// get appointment list
			appointmentInstanceList = ca.list {
				eq("type", appointmentTypeInstance)
				ge("startTime", now)
				order("startTime", "asc")
			}
	
			// get total number of appointments
			pastAppointmentTotal = cat.get {
				eq("type", appointmentTypeInstance)
				lt("startTime",now)
				projections {
					count("id")
				}
			}

		} else {

			// get appointment list
			appointmentInstanceList = ca.list{
				ge("startTime", now)
				order("startTime", "asc")
			}
	
			// get total number of appointments
			pastAppointmentTotal = cat.get{
				lt("startTime",now)
				projections {
					count("id")
				}
			}
		
		}			
		//println "=> appointmentInstanceList = ${appointmentInstanceList}"		
		//println "=> pastAppointmentTotal = ${pastAppointmentTotal}"
		
		// add records to appointment detailed list
		appointmentInstanceList.eachWithIndex {rs, i ->

			def record = [:]

			def personInstanceForList = Person.findWhere(id:rs.personId)
			//println "=> personInstanceForList = ${personInstanceForList}"
			
			def personLinkInstance = PersonLink.findByPerson(personInstanceForList)
			//println "=> personLinkInstance = ${personLinkInstance}"
			
			record.rowNum = i + 1
			record.personId = personInstanceForList.id				
			record.norcId = personLinkInstance.norcSuId 	
			record.lastName = personInstanceForList.lastName
			record.firstName = personInstanceForList.firstName
			record.middleName = personInstanceForList.middleName
			record.startTime = rs.startTime
			record.apptType = AppointmentType.findWhere(id:rs.typeId)			
			record.apptResult = AppointmentResult.findWhere(id:rs.resultId)
	
			//println "=> record = ${record}"
			appointmentDetailInstanceList.add(record)

		} 
		//println "=> appointmentDetailInstanceList = ${appointmentDetailInstanceList}"

		 
		[ 
			appointmentTypeInstance: appointmentTypeInstance, 
			appointmentDetailInstanceList: appointmentDetailInstanceList, 
			pastAppointmentTotal: pastAppointmentTotal
		] 
		
	} // def = list

	// TODO: This is not done, but should be finished later on
	def calendar = {

		log.debug "params: ${params}"

		// There is no "calendar.gsp", only "month.gsp, week.gsp, and day.gsp)
		def midnight = new LocalTime(0,0)
		log.debug "midnight = ${midnight}"

		def format = "month"
		if (params?.format == "week") { format = "week" }
		log.debug "format = ${format}"

		def refDate = new Date()
		log.debug "refDate = ${refDate}"
		if (params.id) {
			log.debug "if (params.id)"
			refDate = ( new LocalDate(params.id) ).toDateTime(midnight).toCalendar().time
			log.debug "=> refDate = ${refDate}"
		}

		/* monthInfo stuff
		 *   referenceDate
		 *   dayOfWeek
		 *   firstOfWeek
		 *   lastOfWeek
		 *   firstCalendarDate
		 *   firstWeekdayOfMonth
		 *   firstOfMonth
		 *   lastOfmonth
		 *   firstDayOfNextMonth
		 */
		def monthInfo = getMonthInfo(refDate) 
		def referenceDate = new LocalDate(refDate.time)
		def startDate
		def endDate
		def weeks = []
		def prevMonth = referenceDate.minusMonths(1).toDateTime(midnight).toCalendar().time
		def nextMonth = referenceDate.plusMonths(1).toDateTime(midnight).toCalendar().time
		def prevWeek = referenceDate.plusWeeks(-1).toDateTime(midnight).toCalendar().time
		def nextWeek = referenceDate.plusWeeks(1).toDateTime(midnight).toCalendar().time

		log.debug "monthInfo= ${monthInfo}"
		log.debug "referenceDate = ${referenceDate}"
		log.debug "prevMonth = ${prevMonth}"
		log.debug "nextMonth = ${nextMonth}"
		log.debug "prevWeek = ${prevWeek}"
		log.debug "nextWeek = ${nextWeek}"

		// get the range for the calendar
		if (format == "week") {
			startDate = monthInfo.firstOfWeek
			endDate = monthInfo.lastOfWeek
		} else {
			startDate = monthInfo.firstCalendarDate
			//endDate = monthInfo.lastOfMonth.plusDays(1).toDateTime(midnight).toCalendar().time
			endDate = monthInfo.firstDayOfNextMonth
		}
		log.debug "startDate = ${startDate}"
		log.debug "endDate = ${endDate}"

		// get the appointments for the calendar
		def c = Appointment.createCriteria()
		def appointmentInstanceList = c.list{
			and{
				gt("startTime", startDate)
				lt("startTime", endDate)
			}
			order("startTime")
		}
		log.debug "appointmentInstanceList = ${appointmentInstanceList}"

		def cursorDate = startDate
		log.debug "cursorDate = ${cursorDate}"

		while (cursorDate <= endDate) {
			log.debug "BEGIN while (cursorDate <= endDate)"
			// add all days to week
			def daysOfWeek = []
				(1..7).each{
					def day = [:]
					def cursorLocalDate = new LocalDate(cursorDate.time)
					def tomorrow = cursorDate + 1
					day.dayOfMonth = cursorLocalDate.dayOfMonth
					day.date = cursorDate
					day.appointments = appointmentInstanceList.findAll{ (it.startTime.compareTo(cursorDate) == 1) && ( it.startTime.compareTo(tomorrow) == -1 ) }
					log.debug "  cursorLocalDate = ${cursorLocalDate}"
					log.debug "  tomorrow = ${tomorrow}"
					log.debug "  day.dayOfMonth = ${day.dayOfMonth}"
					log.debug "  day.date = ${day.date}"
					log.debug "  day.appointments = ${day.appointments}"
					if (referenceDate.monthOfYear == cursorLocalDate.monthOfYear) {
						day.cssShadow = ""
						day.thisMonth = true
					} else {
						day.cssShadow = "shadow"
						day.thisMonth = false
					} 
					daysOfWeek.add(day)
					cursorDate++
				}
			weeks.add([days:daysOfWeek])
			log.debug "END while (cursorDate <= endDate)"
		}

		def model = [ weeks:weeks, 
		    appointmentInstanceList: appointmentInstanceList,
		    refDate: refDate,
		    nextMonth: nextMonth,
		    prevMonth: prevMonth,
		    nextWeek: nextWeek,
		    prevWeek: prevWeek ]

		render(view: format, model: model)		

	} //calendar 

	def create = {

		def user = springSecurityService.principal

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

		def user = springSecurityService.principal

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
			} else {
				render(view: "edit", model: [appointmentInstance: appointmentInstance])
			}
		} else {
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

				} catch (org.springframework.dao.DataIntegrityViolationException e) {
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

		log.debug "BEGIN private def getMonthInfo(refDate)"

		// convert java.util.Date to org.joda.time.LocalDate
		LocalDate referenceDate = new LocalDate(refDate.time)
		def dayOfWeek = referenceDate.getDayOfWeek()
		LocalDate firstOfMonth = referenceDate.withDayOfMonth(1)
		def lastOfmonth = referenceDate.plusMonths(1).withDayOfMonth(1).minusDays(1)
		def firstDayOfNextMonth = referenceDate.plusMonths(1).withDayOfMonth(1) 

		log.debug "  referenceDate = ${referenceDate}"
		log.debug "  dayOfWeek = ${dayOfWeek}"
		log.debug "  firstOfMonth = ${firstOfMonth}"
		log.debug "  lastOfmonth = ${lastOfmonth}"

		// get first weekday of the month
		def firstWeekdayOfMonth = firstOfMonth.getDayOfWeek()
		log.debug "  firstWeekdayOfMonth = ${firstWeekdayOfMonth}"

		// difference between first day on calendar and 
		//first day of month is (firstOfMonth - (7 - firstWeekdayOfMonth)
		def firstCalendarDate = firstOfMonth
		log.debug "  firstCalendarDate = ${firstCalendarDate}"
		if (firstWeekdayOfMonth != 7) {
			log.debug "  if (firstWeekdayOfMonth != 7)"
			firstCalendarDate = firstOfMonth.minusDays(firstWeekdayOfMonth)
			log.debug "  => firstCalendarDate = ${firstCalendarDate}"
		}

		def firstOfWeek = referenceDate.withDayOfWeek(1).minusDays(1)
		def lastOfWeek = referenceDate.withDayOfWeek(6)
		def midnight = new LocalTime(0,0)
		def monthInfo = [:]
		log.debug "  firstOfWeek = ${firstOfWeek}"
		log.debug "  lastOfWeek = ${lastOfWeek}"

		monthInfo.referenceDate = referenceDate.toDateTime(midnight).toCalendar().time
		monthInfo.dayOfWeek = dayOfWeek
		monthInfo.firstOfWeek = firstOfWeek.toDateTime(midnight).toCalendar().time
		monthInfo.lastOfWeek = lastOfWeek.toDateTime(midnight).toCalendar().time
		monthInfo.firstCalendarDate = firstCalendarDate.toDateTime(midnight).toCalendar().time
		monthInfo.firstWeekdayOfMonth = firstWeekdayOfMonth
		monthInfo.firstOfMonth = firstOfMonth.toDateTime(midnight).toCalendar().time
		monthInfo.lastOfMonth = lastOfmonth.toDateTime(midnight).toCalendar().time
		monthInfo.firstDayOfNextMonth = firstDayOfNextMonth.toDateTime(midnight).toCalendar().time

		log.debug "END private def getMonthInfo(refDate)"

		return monthInfo

	}

}
