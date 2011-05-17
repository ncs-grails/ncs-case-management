package edu.umn.ncs

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable

@Secured(['ROLE_NCS_DOCGEN'])
class BatchController {

    private boolean debug = true

    def emailService
    def authenticateService

    def index = { 
        redirect(action:'list',params:params)
    }

    def sendNightlyReport = {

        // emailService.sendProductionReport(params)
        emailService.sendProductionReport()
		
        redirect(controller:"mainMenu", action:"index")

    }

    def nightlyReport = {
        def referenceDate = params.referenceDate
        def midnight = new LocalTime(0, 0)

        if ( ! referenceDate ) {
            referenceDate = new LocalDate()
        } else {
            referenceDate = new LocalDate(referenceDate)
        }


        def startDate = referenceDate.toDateTime(midnight).toCalendar().getTime()
        def endDate = referenceDate.plusDays(1).toDateTime(midnight).toCalendar().getTime()
		
        def c = Batch.createCriteria()

        def batchInstanceList = c.list{
            gt("dateCreated", startDate)
            lt("dateCreated", endDate)
        }


        [ referenceDate: startDate,
            batchInstanceList: batchInstanceList,
            customizable: true]
    }
	
	def sendNorcAlert = {
		emailService.sendNorcAlert()
		
		redirect(controller:"mainMenu", action:"index")
	}

	def norcAlert = {
		def referenceDate = params.referenceDate
		def midnight = new LocalTime(0, 0)

		if ( ! referenceDate ) {
			referenceDate = new LocalDate()
		} else {
			referenceDate = new LocalDate(referenceDate)
		}

		// get the time range for yesterday (or whatever reference date)
		def dateRange = getFullDayRange(params?.referenceDate)

		
		def now = new Date()
		// query the batches
		def c = Batch.createCriteria()
		// finding all mailed batches
		
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

		// pass the model to the view
		[ referenceDate: dateRange.startDate,
			batchInstanceList: batchInstanceList,
			customizable: true ]
	}

	
    def entry = {
        // reference date
        def referenceDate = params?.referenceDate

        // look for batch ID
        def batchId = params.id

        // if the batch ID was passed, and a date was passed...
        if (batchId && referenceDate) {

            if (batchId[0].matches('b') || batchId[0].matches('B')) {
                batchId = batchId.toLowerCase().replace('b','')

                try {
                    def batchInstance = Batch.get(batchId)

                    if (batchInstance) {
                        // update batch mail date
                        batchInstance.mailDate = referenceDate
                        // TODO: we should probably update the provenance fields here
                        batchInstance.save(flush:true)
                        flash.message = "${batchInstance.primaryInstrument.study} ${batchInstance.primaryInstrument} generated on ${batchInstance.dateCreated} has been updated."

                    } else {
                        flash.message = "Batch not found: ${batchId}"
                    }

                } catch (Exception) {
                    flash.message = "Invalid Batch ID: ${batchId}"
                }

            } else {
                println "fail..."
                flash.message = "Invalid Batch ID ${batchId}"
            }
        }

        // choose today if no reference date was specified
        if ( ! referenceDate ) {
            referenceDate = new Date()
            referenceDate.hours = 0
            referenceDate.minutes = 0
            referenceDate.seconds = 0
        }

        // create a criteria for querying
        def c = Batch.createCriteria()

        // all batches that have a maildate that matches today.
        def sentBatchInstanceList = c.list{
            between("mailDate", referenceDate, referenceDate + 1)
        }

        c = Batch.createCriteria()
        // all batches that don't have a mail date
        def unsentBatchInstanceList = c.list{
            and {
                isNull("mailDate")
                between("dateCreated", referenceDate - 14, referenceDate + 1)
            }
        }
        [ referenceDate: referenceDate,
            sentBatchInstanceList:sentBatchInstanceList,
            unsentBatchInstanceList:unsentBatchInstanceList ]
    }

    def list = {
        
        def username = authenticateService?.principal()?.getUsername()
        def q = params?.q
        def searchedId = 0L

       if (q) {
            if (q.isLong()) {
                searchedId = Long.parseLong(q)
            }
        }

        def batchInstanceList = []
        def batchRecentList = []
        def aboutSixMonthAgo = (new Date()) - 180

        // Find recently generated batches
        def c = Batch.createCriteria()
        batchRecentList = c.listDistinct{
            eq('batchRunBy', username)
            gt('dateCreated', aboutSixMonthAgo)
        }

        if (q){
            c = Batch.createCriteria()
            batchInstanceList = c.listDistinct{
                or {
                    eq('id', searchedId)
                    instruments {
                        and {
                            eq('isPrimary', true)
                            instrument {
                                ilike('name', "%${q}%")
                                ilike('nickName', "%${q}%")
                            }
                        }
                    }
                }
            }
        }
        [batchInstanceList:batchInstanceList,
            batchRecentList:batchRecentList]
    }
	
	def show = {
		def batchInstance = Batch.read(params.id)
		if (!batchInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
			redirect(action: "list")
		} else {
			[batchInstance: batchInstance]
		}
	}

	def edit = {

		def batchInstance = Batch.read(params.id)
		
		def dayPrior = batchInstance.dateCreated - 1
		def dayAfter = batchInstance.dateCreated + 1
		
		// List of batches to choose for master batch
		def masterBatchList = []
		def c = Batch.createCriteria()
		def batchInstanceList = c.listDistinct {
			and {
				gt("dateCreated", dayPrior)
				lt("dateCreated", dayAfter)
			}
		}
		
		batchInstanceList.each{ b ->
			def batchInfo = [
					id:b.id, 
					name:"Batch ID ${b.id}: ${b.primaryInstrument.name}"
					]
			masterBatchList.add(batchInfo)
		}
		

		
		if (!batchInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
			redirect(action: "list")
		} else {

			
			/* Check the batch items: created for dwelling Units, persons or households */
			
			def validItems = [
				dwellingUnit: batchInstance.items.find{it.dwellingUnit != null},
				person: batchInstance.items.find{it.person != null},
				household: batchInstance.items.find{it.household != null}
			]
			
			[batchInstance: batchInstance, validItems: validItems]
		}
	}

	
	def deleteItem = {
		
		def trackedItemInstance = TrackedItem.read(params.id)
		def batchInstance = null
		def validItems = null
	
		if (trackedItemInstance) {
			batchInstance = Batch.read(trackedItemInstance?.batch?.id)
			if (batchInstance){
				validItems = [
					dwellingUnit: batchInstance.items.find{it.dwellingUnit != null},
					person: batchInstance.items.find{it.person != null},
					household: batchInstance.items.find{it.household != null}
				]
			}
			
			try {
				trackedItemInstance.delete(flush: true)
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'trackedItem.label', default: 'TrackedItem'), params.id])}"
				render(view: "edit", model: [batchInstance: batchInstance, validItems: validItems])
			}catch(org.springframework.dao.DataIntegrityViolationException e){
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'trackedItem.label', default: 'TrackedItem'), params.id])}"
			}
			
			render(view: "edit", model: [batchInstance: batchInstance, validItems: validItems])
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'trackedItem.label', default: 'TrackedItem'), params.id])}"
			redirect(action: "list")
		}
	}
	
	def addItem = {
		
		def batchInstance = Batch.read(params.id)
		
		if (batchInstance) {
			
			def validItems = [
				dwellingUnit: batchInstance.items.find{it.dwellingUnit != null},
				person: batchInstance.items.find{it.person != null},
				household: batchInstance.items.find{it.household != null}
			]
			
			if (params.version){
				def version = params.version.toLong()
				if (batchInstance.version > version) {
					batchInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'batch.label', default: 'Batch')] as Object[], "Another user has updated this Batch while you were editing")
					render(view: "edit", model: [batchInstance: batchInstance])
					return
				}
			}

			
			// Checking if this is a valid Dwelling Unit , Person or Household
			def dwellingUnitId = params?.dwellingUnit?.id
			def personId = params?.person?.id
			def householdId = params?.household?.id
			
			def username = authenticateService?.principal()?.getUsername()
			
			def bcq = new BatchCreationQueue()
			
			if (dwellingUnitId) {
				def dwellingUnit = DwellingUnit.read(dwellingUnitId)
				if (dwellingUnit) {
					bcq.dwellingUnit = dwellingUnit
				}
			} else if (personId){
				def person = Person.read(personId)
				if (person){
					bcq.person = person
				}
			} else if (householdId){
				def household = Household.read(householdId)
				if (household){
					bcq.household = household
				}
			}
	
			bcq.username = username
			if (!bcq.validate()) {
				
				bcq.errors.each{ e->
					e.fieldErrors.each{fe -> println "! Rejected'${fe.rejectedValue}' for field '${fe.field}'\n"}
				}
				flash.message = "Valid DwellingUnit, Person or Household ID Required."
			} else {
				batchInstance.properties = params
			
				def trackedItemInstance = new TrackedItem(person: bcq.person,
													dwellingUnit: bcq.dwellingUnit,
													household: bcq.household)
				batchInstance.updatedBy = username
				batchInstance.addToItems(trackedItemInstance)
				
				if (!batchInstance.hasErrors() && batchInstance.save(flush:true)){
					flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batch.label', default: 'Batch'), batchInstance.id])}"
				} else {
					batchInstance.errors.each{e ->
						e.fieldErrors.each{fe -> println "! Saving Batch. Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"}
					}
				}
				render(view: "edit", model: [batchInstance: batchInstance, validItems: validItems])
			}
			render(view: "edit", model: [batchInstance: batchInstance, validItems: validItems])
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
			redirect(action: "list")
		}
	}

	
    def listByDate = {

        def referenceDate = params?.referenceDate
        def midnight = new LocalTime(0, 0)

        if (!referenceDate) {
            referenceDate = new LocalDate()
			referenceDate = referenceDate.minusDays(referenceDate.dayOfMonth - 1)
        } else {
            referenceDate = new LocalDate(referenceDate)
        }
        // convert the "cool" JodaDate to a nasty java.util.Date
        def startDate = referenceDate.toDateTime(midnight).toCalendar().getTime()
        def endDate = referenceDate.plusMonths(1).toDateTime(midnight).toCalendar().getTime()


        // create a criteria for querying
        def c = Batch.createCriteria()

        //All batches for the passed date month that do not have mail date
        def batchInstanceList = c.list{
            between("dateCreated", startDate, endDate)
        }

        [ referenceDate: startDate, 
            endDate: endDate,
            batchInstanceList: batchInstanceList]
    }

    def editDates = {
        def batchInstance = Batch.get(params.id)
        if (!batchInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
            redirect(action: "list")
        }
        else {

            def batchDate = new LocalDate(batchInstance.dateCreated)
            def referenceDateMonth = batchDate.monthOfYear
            def referenceDateYear = batchDate.year
            def yearRange = (referenceDateYear..(referenceDateYear+1))

            // println "referenceDateYear -> ${referenceDateYear}"


            return [ batchInstance: batchInstance,
                referenceDateMonth: referenceDateMonth,
                referenceDateYear: referenceDateYear,
                yearRange: yearRange]
        }
    }

    def update = {
        def batchInstance = Batch.get(params.id)
        if (batchInstance) {

            def batchDate = new LocalDate(batchInstance.dateCreated)
            def referenceDateMonth = batchDate.monthOfYear
            def referenceDateYear = batchDate.year
            def yearRange = (referenceDateYear..(referenceDateYear+1))

            // check date order here

            if (params.calledCampusCourierDate && params.calledCampusCourierDate < batchInstance.dateCreated) {
                batchInstance.errors.rejectValue("calledCampusCourierDate", "batch.calledCampusCourierDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Campus Courier Date must come after date generated")
                    render(view: "editDates", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.addressAndMailingDate && params.addressAndMailingDate < batchInstance.dateCreated){
                batchInstance.errors.rejectValue("addressAndMailingDate", "batch.addressAndMailingDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Address and Mailing Date must come after date generated")
                    render(view: "editDates", model: [batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.mailDate && params.mailDate < batchInstance.dateCreated) {
                batchInstance.errors.rejectValue("mailDate", "batch.mailDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Mailing Date must come after date generated")
                    render(view: "editDates", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }


            if (params.trackingReturnDate && params.trackingReturnDate < batchInstance.dateCreated) {
                batchInstance.errors.rejectValue("trackingReturnDate", "batch.trackingReturnDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Tracking Return Date must come after date generated")
                    render(view: "editDates", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.mailDate && params.trackingReturnDate && params.mailDate > params.trackingReturnDate) {
                batchInstance.errors.rejectValue("trackingReturnDate", "batch.trackingReturnDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Tracking Return Date must come after Mail Date")
                    render(view: "editDates", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.version) {
                def version = params.version.toLong()
                if (batchInstance.version > version) {

                    batchInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'batch.label', default: 'Batch')] as Object[], "Another user has updated this Batch while you were editing")
                    render(view: "editDates", model: [batchInstance: batchInstance])
                    return
                }
            }
            batchInstance.properties = params
            if (!batchInstance.hasErrors() && batchInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batch.label', default: 'Batch'), batchInstance.id])}"
                redirect(action: "editDates", id: batchInstance.id)
            }
            else {
                render(view: "editDates", model: [batchInstance: batchInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
            redirect(action: "list")
        }
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
