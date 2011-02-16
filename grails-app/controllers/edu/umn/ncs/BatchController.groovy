package edu.umn.ncs

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class BatchController {

    def emailService

    def index = { 
        redirect(action:'listByDate',params:params)
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
        redirect(action:'listByDate',params:params)
    }

    def listByDate = {

        //println "Batch:listByDate:params::${params}"

        def referenceDate = params?.referenceDate
        def midnight = new LocalTime(0, 0)

        if (!referenceDate) {
            referenceDate = new LocalDate()
        } else {
            referenceDate = new LocalDate(referenceDate)
        }
        // convert the "cool" JodaDate to a nasty java.util.Date
        def startDate = referenceDate.toDateTime(midnight).toCalendar().getTime()
        def endDate = referenceDate.plusMonths(1).toDateTime(midnight).toCalendar().getTime()


        // create a criteria for querying
        def c = Batch.createCriteria()

        //All batches for the passed date month that do not have mail date
        def unsentBatchInstanceList = c.list{
            and {
                //isNull("mailDate")
                between("dateCreated", startDate, endDate)
            }
        }
		//println "Returning ${unsentBatchInstanceList}"
        [ referenceDate: startDate, 
            endDate: endDate,
            unsentBatchInstanceList: unsentBatchInstanceList]
    }

    def edit = {
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

            if (params.printingServicesDate && params.printingServicesDate < batchInstance.dateCreated) {
                batchInstance.errors.rejectValue("printingServicesDate", "batch.printingServicesDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Printing Services Date must come after date generated")
                    render(view: "edit", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.addressAndMailingDate && params.addressAndMailingDate < batchInstance.dateCreated){
                batchInstance.errors.rejectValue("addressAndMailingDate", "batch.addressAndMailingDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Address and Mailing Date must come after date generated")
                    render(view: "edit", model: [batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.mailDate && params.mailDate < batchInstance.dateCreated) {
                batchInstance.errors.rejectValue("mailDate", "batch.mailDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Mailing Date must come after date generated")
                    render(view: "edit", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }


            if (params.trackingReturnDate && params.trackingReturnDate < batchInstance.dateCreated) {
                batchInstance.errors.rejectValue("trackingReturnDate", "batch.trackingReturnDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Tracking Return Date must come after date generated")
                    render(view: "edit", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.mailDate && params.trackingReturnDate && params.mailDate > params.trackingReturnDate) {
                batchInstance.errors.rejectValue("trackingReturnDate", "batch.printingServicesDate.dateToEarly", [message(code: 'batch.label', default: 'Batch')] as Object[], "Tracking Return Date must come after Mail Date")
                    render(view: "edit", model: [ batchInstance: batchInstance,
                        referenceDateMonth: referenceDateMonth,
                        referenceDateYear: referenceDateYear,
                        yearRange: yearRange])
                    return
            }

            if (params.version) {
                def version = params.version.toLong()
                if (batchInstance.version > version) {

                    batchInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'batch.label', default: 'Batch')] as Object[], "Another user has updated this Batch while you were editing")
                    render(view: "edit", model: [batchInstance: batchInstance])
                    return
                }
            }
            batchInstance.properties = params
            if (!batchInstance.hasErrors() && batchInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'batch.label', default: 'Batch'), batchInstance.id])}"
                redirect(action: "edit", id: batchInstance.id)
            }
            else {
                render(view: "edit", model: [batchInstance: batchInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'batch.label', default: 'Batch'), params.id])}"
            redirect(action: "list")
        }
    }


}
