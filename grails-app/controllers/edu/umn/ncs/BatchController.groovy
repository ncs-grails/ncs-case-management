package edu.umn.ncs

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable

@Secured(['ROLE_NCS'])
class BatchController {

    private boolean debug = true

    def emailService
    def authenticateService

    def index = { 
        redirect(action:'list',params:params)
    }

	@Secured(['ROLE_NCS_IT'])
    def sendNightlyReport = {

        // emailService.sendProductionReport(params)
        emailService.sendProductionReport()
		
        redirect(controller:"mainMenu", action:"index")

    }

	@Secured(['ROLE_NCS_IT'])
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
		def batchInstanceList = c?.list{
            gt("dateCreated", startDate)
            lt("dateCreated", endDate)
        }

        [ referenceDate: startDate,
            batchInstanceList: batchInstanceList,
            customizable: true]
    }
	
	@Secured(['ROLE_NCS_IT'])
	def sendNorcAlert = {
		emailService.sendNorcAlert()
		
		redirect(controller:"mainMenu", action:"index")
	}

	@Secured(['ROLE_NCS_IT'])
	def norcAlert = {

		// get the time range for yesterday (or whatever reference date)
		def dateRange = emailService.getFullDayRange(params?.referenceDate)

		
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
				}
			}
		}

		// pass the model to the view
		[ referenceDate: dateRange.startDate,
			batchInstanceList: batchInstanceList,
			customizable: true ]
	}

	
	// This logs mail dates for batches that have been generated
	@Secured(['ROLE_NCS_RECEIPT'])
    def entry = {
        // reference date
        def referenceDate = params?.referenceDate
		def username = authenticateService?.principal()?.getUsername()
		

        // look for batch ID
        def batchId = params.id

        // if the batch ID was passed, and a date was passed...
        if (batchId && referenceDate) {

            if (batchId[0].matches('b') || batchId[0].matches('B')) {
                batchId = batchId.toLowerCase().replace('b','')

                try {
                    def batchInstance = Batch.get(batchId)

                    if (batchInstance) {
						
						def genDate = new LocalDate(batchInstance.dateCreated)
						def mailDate = new LocalDate(referenceDate)
						
						if (mailDate.isBefore(genDate) && !mailDate.isEqual(genDate)) {
							flash.message = "Mail Date must come after date generated"
						} else {
							// update batch mail date
							batchInstance.mailDate = referenceDate
							batchInstance.lastUpdated = new Date()
							batchInstance.updatedBy = username
							batchInstance.save(flush:true)
							flash.message = "${batchInstance.primaryInstrument?.study} ${batchInstance.primaryInstrument} generated on ${batchInstance.dateCreated} has been updated as mailed on ${mailDate}."

						}
						
                    } else {
                        flash.message = "Batch not found: ${batchId}"
                    }

                } catch (Exception ex) {
                    flash.message = "Invalid Batch ID: ${batchId}\n"
					flash.exception = ex
                }

            } else {
                flash.message = "Invalid Batch ID format: ${batchId}"
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
		// outgoing
		// first class mail
        def unsentBatchInstanceList = c.list{
            and {
				direction{
					idEq(1)
				}
				format{
					idEq(1)
				}
                isNull("mailDate")
                between("dateCreated", referenceDate - 14, referenceDate + 1)
            }
        }
        [ referenceDate: referenceDate,
            sentBatchInstanceList:sentBatchInstanceList,
            unsentBatchInstanceList:unsentBatchInstanceList ]
    }

	@Secured(['ROLE_NCS_IT'])
    def list = {
        
        def username = authenticateService?.principal()?.getUsername()
        def q = params?.q
        def searchedId = 0L
		def itemId = 0L

		// instrument descriptions as well as IDs
		// TODO: search when term contains a "-"
		if (q) {
            if (q.isLong()) {
                searchedId = Long.parseLong(q)
            }
			if (q[0] == "I") {
				q = q.replace("I", "")
				if (q.isLong()) {
	                // we have an item
	                itemId =  Long.parseLong(q)
				}
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
			order('dateCreated', 'desc')
        }

        if (q){

			// See lookup for an example.
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
					items {
						eq('id', itemId)
					}
                }
				order('dateCreated', 'desc')
            }
			
        }
        [batchInstanceList:batchInstanceList,
            batchRecentList:batchRecentList]
    }
	
	@Secured(['ROLE_NCS_IT'])
	def show = {
		def batchInstance = Batch.read(params.id)
		if (!batchInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
			redirect(action: "list")
		} else {
			[batchInstance: batchInstance]
		}
	}

	@Secured(['ROLE_NCS_IT'])
	def edit = {
		def batchInstance = Batch.read(params.id)
		if (!batchInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
			redirect(action: "list")
		} else {
			
			def batchDate = new LocalDate(batchInstance.dateCreated)
			def referenceDateYear = batchDate.year
			def yearRange = (referenceDateYear..(referenceDateYear+1))

			/* Check the batch items: created for dwelling Units, persons or households */
			[batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange]
		}
	}
	
	@Secured(['ROLE_NCS_IT'])
	def delete = {
		def batchInstance = Batch.get(params.id)
		
		if (batchInstance) {
			
			def batchDate = new LocalDate(batchInstance.dateCreated)
			def referenceDateYear = batchDate.year
			def yearRange = (referenceDateYear..(referenceDateYear+1))
			
			if (batchInstance.items.size() == 0) {
				try {
					batchInstance.delete(flush: true)
					flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
					redirect(action: "list")
				}
				catch (org.springframework.dao.DataIntegrityViolationException e) {
					flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
					redirect(action: "edit", id: batchInstance.id)
				}
			} else {
				batchInstance.errors.rejectValue("id", "id.NotEmpty", [message(code: 'id.label', default: 'Batch')] as Object[], "Batch: ${batchInstance.id} not deleted. All tracked items must be deleted.")
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
					return
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
			redirect(action: "list")
		}
	}
	
	@Secured(['ROLE_NCS_IT'])
	def update = {
		
		def batchInstance = Batch.read(params.id)
		if (batchInstance) {
			// validators should be moved to domain class.  See TODO in tracking plugin.

			def dateCreated = new LocalDate(batchInstance.dateCreated)
			def mailDate = new LocalDate(params.mailDate)
			def calledCampusCourierDate = new LocalDate(params.calledCampusCourierDate)
			def printingServicesDate = new LocalDate(params.printingServicesDate)
			def addressAndMailingDate = new LocalDate(params.addressAndMailingDate)
			def trackingReturnDate = new LocalDate(params.trackingReturnDate)
			def instrumentDate = new LocalDate(params.instrumentDate)

			def batchDate = new LocalDate(batchInstance.dateCreated)
			def referenceDateMonth = batchDate.monthOfYear
			def referenceDateYear = batchDate.year
			def yearRange = (referenceDateYear..(referenceDateYear+1))
			
            // check date order here

			// TODO: Add messages to messages.properties in grails-app/i18n/

            if (calledCampusCourierDate && calledCampusCourierDate.isBefore(dateCreated) && !calledCampusCourierDate.isEqual(dateCreated)) {
                batchInstance.errors.rejectValue("calledCampusCourierDate", "batch.calledCampusCourierDate.dateToEarly")
                    render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
                    return
            }

			if (instrumentDate && instrumentDate.isBefore(dateCreated) && !instrumentDate.isEqual(dateCreated)) {
				batchInstance.errors.rejectValue("instrumentDate", "batch.instrumentDate.dateToEarly")
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
					return
			}
						
			if (addressAndMailingDate && addressAndMailingDate.isBefore(dateCreated) && !addressAndMailingDate.isEqual(dateCreated)){
				batchInstance.errors.rejectValue("addressAndMailingDate", "batch.addressAndMailingDate.dateToEarly")
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
					return
			}
			
			if (mailDate && mailDate.isBefore(dateCreated) && !mailDate.isEqual(dateCreated)) {
				batchInstance.errors.rejectValue("mailDate", "batch.mailDate.dateToEarly")
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
					return
			}


			if (trackingReturnDate && trackingReturnDate.isBefore(dateCreated) && !trackingReturnDate.isEqual(dateCreated)) {
				batchInstance.errors.rejectValue("trackingReturnDate", "batch.trackingReturnDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Tracking Return Date must come after date generated")
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
					return
			}

			if (mailDate && trackingReturnDate && mailDate.isBefore(trackingReturnDate) && mailDate.isEqual(trackingReturnDate)) {
				batchInstance.errors.rejectValue("trackingReturnDate", "batch.trackingReturnDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Tracking Return Date must come after Mail Date")
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
					return
			}

			if (params.version) {
				def version = params.version.toLong()
				if (batchInstance.version > version) {

					batchInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'batch.label', default: 'Batch')] as Object[], "Another user has updated this Batch while you were editing")
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
					return
				}
			}
			batchInstance.properties = params
			if (!batchInstance.hasErrors() && batchInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batch.label', default: 'Batch'), batchInstance.id])}"
				redirect(action: "edit", id: batchInstance.id)
			}
			else {
				render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), yearRange: yearRange])
			}
			
		} else {
		    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
            redirect(action: "list")
        }
		
	}

	// TODO: Errors out if tracked item has a child item.
	// TODO: Errors out if tracked item is in the user queue (edu.umn.ncs.phone.UserQueue)
	// TODO: check for any other kinds of errors that may cause the item to fail on deletion
	//		 then report what table caused the failure
	//
	// TODO: shouldn't this be in TrackedItemController.deleteItem(Integer item) ?
	private def deleteItem(Integer item) {
		
		def trackedItemInstance = null
		def r = [batchId: 0,
			message: "",
			err: ""]
		
		trackedItemInstance = TrackedItem.read(item)
		if (debug) {
			println "********************* before if (trackedItemInstance"
		}
		if (trackedItemInstance) {
			r.batchId = trackedItemInstance.batchId
			
			if (debug) {
				println "trackedItemInstance.batchId: ${trackedItemInstance.batchId}"
			}
			
			// Delete BatchQueue parent records
			def BatchQueueInstanceList = BatchQueue.createCriteria().list{
				items {
					eq('id', trackedItemInstance.id)
				}
			}
			BatchQueueInstanceList.each{batchQueueInstance
				try {
					batchQueueInstance.delete()
				}catch(org.springframework.dao.DataIntegrityViolationException e){
					r.err += "${message(code: 'default.not.deleted.message', args: [message(code: 'batchQueue.label', default: 'Batch Queue'), ${item}])}"
				}
			}
			
			// Delete Item
			try {
				trackedItemInstance.delete()
				r.message += "${item}, "
			}catch(org.springframework.dao.DataIntegrityViolationException e){
				r.err += "${message(code: 'default.not.deleted.message', args: [message(code: 'trackedItem.label', default: 'TrackedItem'), ${item}])}"
			}
		}
		return r
	}

	// TODO: shouldn't this be in TrackedItemController.delete() ?
	@Secured(['ROLE_NCS_IT'])
	def deleteItems = {
		def message = ""
		def err = ""
		def batchId = null
		def r = [:]
		
		message = "Deleted Items: "
		params?.item?.id?.each { 
			def itemId = Integer.parseInt(it)
			
			if (itemId) {
				r = deleteItem(itemId)
	
				message += r?.message
				err += r?.err
				batchId = r?.batchId
			}
		}

		if (batchId) {
			flash.message = message + err
			flash.errorMessage = err
			redirect(action: "edit", id: batchId)
		} else {
			redirect(action: "list")
		}
	}
		
	// TODO: shouldn't this be in TrackedItemController.create() ?
	@Secured(['ROLE_NCS_IT'])
	def addItem = {
		if (debug) {
			println "******************  NGP debug; I am in additem"
		}
		def batchInstance = Batch.read(params.id)
		def trackedItemInstance = null
		
		if (batchInstance) {
			
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
					trackedItemInstance = batchInstance.items.find{it.dwellingUnitId == dwellingUnit.id}
				}
			} else if (personId){
				def person = Person.read(personId)
				if (person){
					bcq.person = person
					trackedItemInstance = batchInstance.items.find{it.personId == person.id}
				}
			} else if (householdId){
				def household = Household.read(householdId)
				if (household){
					bcq.household = household
					trackedItemInstance = batchInstance.items.find{it.householdId == household.id}
				}
			}

            def message = ''
			bcq.username = username
			if (!bcq.validate()) {
				
				bcq.errors.each{ e->
					e.fieldErrors.each{fe -> println "! Rejected'${fe.rejectedValue}' for field '${fe.field}'\n"}
				}
				message = "Valid DwellingUnit, Person or Household ID Required."
			} else {
				
				if (!trackedItemInstance) {

					batchInstance.properties = params
										
					trackedItemInstance = new TrackedItem(person: bcq.person,
						dwellingUnit: bcq.dwellingUnit,
						household: bcq.household)
					
					batchInstance.updatedBy = username
					batchInstance.addToItems(trackedItemInstance)
					
					if (!batchInstance.hasErrors() && batchInstance.save(flush:true)){
						message = "Batch ${batchInstance.id} successfully updated!"
					} else {
						batchInstance.errors.each{e ->
							e.fieldErrors.each{fe -> println "! Saving Batch. Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"}
						}
					}
					
					render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id), message: message])
					return
					
				} else {
					render(view: "edit", model: [ batchInstance: batchInstance, validItems: getValidItems(params.id), message: "Item already in batch. Item ID: ${trackedItemInstance?.id}"])
					return
				}
			}
			render(view: "edit", model: [batchInstance: batchInstance, validItems: getValidItems(params.id)])
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
			redirect(action: "list")
		}
	}
	
	// TODO: shouldn't this be in TrackedItemController?
	private def getValidItems(batchId) {
		def validItems = [:]
		
		def batchInstance = Batch.read(batchId)
		
		if (batchInstance) {
			validItems.dwellingUnit = batchInstance.items.find { it.dwellingUnit != null}
			validItems.person = batchInstance.items.find{it.person != null}
			validItems.household = batchInstance.items.find{it.household != null}
		}
		return validItems
	}

	
	// Data entry screen for batch dates
	// "Recent Batches Status"
	@Secured(['ROLE_NCS_RECEIPT'])
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

	// used by listByDate
	@Secured(['ROLE_NCS_RECEIPT'])
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

	// used by editDates
	@Secured(['ROLE_NCS_RECEIPT'])
    def updateDates = {
        def batchInstance = Batch.get(params.id)
        if (batchInstance) {
			
			def dateCreated = new LocalDate(batchInstance.dateCreated)
			def mailDate = new LocalDate(params.mailDate)
			def calledCampusCourierDate = new LocalDate(params.calledCampusCourierDate)
			def addressAndMailingDate = new LocalDate(params.addressAndMailingDate)
			def trackingReturnDate = new LocalDate(params.trackingReturnDate)

            def batchDate = new LocalDate(batchInstance.dateCreated)
            def referenceDateMonth = batchDate.monthOfYear
            def referenceDateYear = batchDate.year
            def yearRange = (referenceDateYear..(referenceDateYear+1))

            // check date order here

			// This should be done via contstraints in the domain class.  DRY!  =)
            if (calledCampusCourierDate && calledCampusCourierDate.isBefore(dateCreated) && !calledCampusCourierDate.isEqual(dateCreated)) {
                batchInstance.errors.rejectValue("calledCampusCourierDate", "batch.calledCampusCourierDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Campus Courier Date must come after date generated")
                    render(view: "editDates", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (addressAndMailingDate && addressAndMailingDate.isBefore(dateCreated) && !addressAndMailingDate.isEqual(dateCreated)){
                batchInstance.errors.rejectValue("addressAndMailingDate", "batch.addressAndMailingDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Address and Mailing Date must come after date generated")
                    render(view: "editDates", model: [batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }
			
            if (mailDate && mailDate.isBefore(dateCreated) && !mailDate.isEqual(dateCreated)) {
                batchInstance.errors.rejectValue("mailDate", "batch.mailDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Mailing Date must come after date generated")
                    render(view: "editDates", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }


            if (trackingReturnDate && trackingReturnDate.isBefore(dateCreated) && !trackingReturnDate.isEqual(dateCreated)) {
                batchInstance.errors.rejectValue("trackingReturnDate", "batch.trackingReturnDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Tracking Return Date must come after date generated")
                    render(view: "editDates", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (mailDate && trackingReturnDate && mailDate.isBefore(trackingReturnDate) && mailDate.isEqual(trackingReturnDate)) {
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
}
