package edu.umn.ncs

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

/** Sends out Email Alerts */
class EmailService {

    static transactional = true
	
	def mailService

	def sendProductionReport = { params ->

		// println "sendProductionReport:params::${params}"
		def referenceDate = new Date()
		
		def recipients = [ 'ajz@umn.edu'
			, 'dmd@cccs.umn.edu'
			, 'jaf@umn.edu'
			, 'front_dis@cccs.umn.edu'
			, 'sup_dis@cccs.umn.edu' ]

		def dateRange = getFullDayRange(params?.referenceDate)

		try {
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
					from "help@ncs.umn.edu"
					subject "NCS Production Report for ${referenceDate}"
					body( view:"/batch/nightlyReport",
						model:[ referenceDate: dateRange.startDate,
							batchInstanceList: batchInstanceList])
				}
			} else {
				mailService.sendMail {
					to "ajz@umn.edu", "ngp@umn.edu"
					from "help@ncs.umn.edu"
					subject "NCS Production Report for ${referenceDate}"
					body "No batches generated, nothing to send!"
				}
			}
		} catch ( Exception ex ) {
			mailService.sendMail {
				to "ajz@umn.edu", "ngp@umn.edu"
				from "help@ncs.umn.edu"
				subject "FAILED! NCS Production Report for ${referenceDate}"
				body "Failed to send NCS Production Report.  Doh!"
			}
		}
	}

	def sendNorcAlert = { params ->

		// who gets the email...
		def recipients = [ 'ajz@umn.edu'
			, 'msg@cccs.umn.edu'
			, 'Barron-Martin@norc.org'
			, 'Sokolowski-John@norc.uchicago.edu' ]

		//recipients = [ 'ajz@cccs.umn.edu' ]

		// get the time range for yesterday (or whatever reference date)
		def dateRange = getFullDayRange(params?.referenceDate)
		
		// If we can't talk to the DB server
		try {
			// query the batches
			def c = Batch.createCriteria()
			// finding all mailed batches
			def now = new Date()
			def ninetyDaysAgo = now - 90
			
			// Find all batches generated in the last 90 days that
			// are ready to ship to NORC
			def batchInstancePotentialList = c.list{
				and {
					gt("dateCreated", ninetyDaysAgo)
					or {
						isNotNull("mailDate")
						isNotNull("addressAndMailingDate")
						lt("instrumentDate", dateRange.endDate)
					}
				}
			}
	
			// remove the ones we already told them about
			def batchInstanceList = []
			
			if (batchInstancePotentialList) {
				batchInstancePotentialList.each{ b ->
					def batchLinkInstance = BatchLink.findByBatch(b)
					if (batchLinkInstance && ! batchLinkInstance.dateNorcNotified) {
						// removing batch...
						batchInstanceList.add(b)
					} else {
					
					}
				}
			}
			
			if (batchInstanceList) {
				
				// Mark the batches as "notified"
				batchInstanceList.each{ b ->
					def batchLinkInstance = BatchLink.findByBatch(b)
					if ( ! batchLinkInstance ) {
						// println "Creating Batch Link for BID: ${b.id}"
						batchLinkInstance = new BatchLink(batch: b)
					} else {
						// println "Batch Link found for BID: ${b.id}"
						batchLinkInstance = BatchLink.get(batchLinkInstance.id)
					}
					batchLinkInstance.dateNorcNotified = now
					batchLinkInstance.save(flush:true)
				}
				
				mailService.sendMail {
					to recipients.toArray()
					from "help@ncs.umn.edu"
					subject "Alert to NORC of Data Posting"
					body( view:"/batch/norcAlert",
						model:[ batchInstanceList: batchInstanceList])
				}
			}
		} catch (Exception ex) {
			mailService.sendMail {
				to "ajz@umn.edu", "ngp@umn.edu"
				from "help@ncs.umn.edu"
				subject "FAILED! Alert to NORC of Data Posting"
				body "Failed to send Alert to NORC of Data Posting.  Doh!\n${ex}"
			}
		}
	}

	def sendErrorReport = { errorMessage, remoteAddr, username ->
		def recipients = ['help@ncs.umn.edu']
		def now = new Date()
						
		mailService.sendMail {
			to recipients.toArray()
			from "help@ncs.umn.edu"
			subject "Error Report "
			body( view:"/errorReport",
				model:[ errorMessage: errorMessage, now: now, 
					remoteAddr: remoteAddr, username: username])
		}
	}
	
	
	// used to get the full time range for a day when a java.util.Date is
	// passed.  Example:
	// Jan 3rd, 2011 2:34 PM ->
	//     [startDate: "Jan 3rd, 2011 12:00 AM", endDate: "Jan 4th, 2011 12:00 AM" ]
	def getFullDayRange = { referenceDate ->

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
