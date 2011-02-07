package edu.umn.ncs

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class EmailService {

    static transactional = true
	
	def mailService

	def sendProductionReport = { params ->

		// println "sendProductionReport:params::${params}"

		def recipients = [ 'ajz@cccs.umn.edu'
			, 'dmd@cccs.umn.edu'
			, 'front_dis@cccs.umn.edu'
			, 'sup_dis@cccs.umn.edu' ]


		def dateRange = getFullDayRange(params?.referenceDate)

		if ( ! verifyDataSource() ) {
			mailService.sendMail {
				to "ajz@cccs.umn.edu", "ngp@cccs.umn.edu"
				from "info@ncs.umn.edu"
				subject "FAILED! NCS Production Report for ${referenceDate}"
				body "Failed to send NCS Production Report.  Doh!"
			}
		} else {
			// query the batches
			def c = Batch.createCriteria()
			def batchInstanceList = c.list{
				gt("dateCreated", dateRange.startDate)
				lt("dateCreated", dateRange.endDate)
			}

			//println "sendNightlyReport:startDate::${startDate}"
			//println "sendNightlyReport:endDate::${endDate}"

			if (batchInstanceList) {
				mailService.sendMail {
					to recipients.toArray()
					from "info@ncs.umn.edu"
					subject "NCS Production Report for ${referenceDate}"
					body( view:"/batch/nightlyReport",
						model:[ referenceDate: dateRange.startDate,
							batchInstanceList: batchInstanceList])
				}
			}
		}
	}

	def sendNorcAlert = { params ->

		// who gets the email...
		def recipients = [ 'ajz@cccs.umn.edu'
			, 'msg@cccs.umn.edu'
			, 'Barron-Martin@norc.org'
			, 'Sokolowski-John@norc.uchicago.edu' ]

		// get the time range for yesterday (or whatever reference date)
		def dateRange = getFullDayRange(params?.referenceDate)

		// If we can't talk to the DB server
		if ( ! verifyDataSource() ) {
			mailService.sendMail {
				to "ajz@cccs.umn.edu", "ngp@cccs.umn.edu"
				from "info@ncs.umn.edu"
				subject "FAILED! Alert to NORC of Data Posting"
				body "Failed to send Alert to NORC of Data Posting.  Doh!"
			}
			// disable it for now
			// disable it for now
			// disable it for now
			// disable it for now
			// disable it for now
		} else if (false) {

			def now = new Date()
			// query the batches
			def c = Batch.createCriteria()
			// finding all mailed batches
			def batchInstanceList = c.list{
				or {
					isNotNull("mailDate")
					isNotNull("addressAndMailingDate")
					lt("instrumentDate", dateRange.endDate)
				}
			}

			if (batchInstanceList) {
				mailService.sendMail {
					to recipients.toArray()
					from "info@ncs.umn.edu"
					subject "Alert to NORC of Data Posting"
					body( view:"/batch/norcAlert",
						model:[ batchInstanceList: batchInstanceList])
				}
			}
		}
		
	}

	// used to kick-start the MySQL connection if it timed out
	private def verifyDataSource = {
		// kick start the data source if it's sleeping
		def keepTrying = true
		def attempt = 0
		def connected = false

		while ( keepTrying && (attempt < 5) ) {
			try {
				def b = Batch.read(1)
				if ( b || !b ) {
					keepTrying = false
					connected = true
				}
			} catch (Exception e) {
				Thread.sleep(3000)
				attempt++
			}
		}

		return connected
	}

	// used to get the full time range for a day when a java.util.Date is
	// passed.  Example:
	// Jan 3rd, 2011 2:34 PM ->
	//     [startDate: "Jan 3rd, 2011 12:00 AM", endDate: "Jan 4th, 2011 12:00 AM" ]
	private def getFullDayRange = { referenceDate ->

		// default day is "Yesterday"

		// Some start/end date voodoo

		if ( referenceDate ) {
			referenceDate = new LocalDate(referenceDate)
		} else {
			referenceDate = new LocalDate()
			referenceDate = referenceDate.minusDays(1)
		}

		// we need a midnight time (for later on)
		def midnight = new LocalTime(0, 0)

		// get the midnight "LocalDate" and change it to a java.util.Date
		def startDate = referenceDate.toDateTime(midnight).toCalendar().getTime()
		// get the midnight + 1 "LocalDate" and change it to a java.util.Date
		def endDate = referenceDate.plusDays(1).toDateTime(midnight).toCalendar().getTime()

		// return the start and end Dates as a map.
		return [ startDate: startDate
			, endDate: endDate ]
	}

}
