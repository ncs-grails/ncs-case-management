package edu.umn.ncs

import grails.converters.*
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_RECEIPT'])
class ReceiptItemsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def authenticateService

    def rand = new Random()

    def index = {
        redirect(action: "receipt", params: params)
    }

    def receipt = {
		def receiptDate = params.receiptDate

		if ( ! receiptDate ) {
			receiptDate = new Date()
		}
		[ receiptDate: receiptDate ]
	}

    def receiptItem = {

        // Check if it has I infront. If yes remove the "I" and proceed
        def username = authenticateService.principal().getUsername()
        def appName = "ncs-case-management"
        
        //println "receiptItems params --> ${params}"
        def trackedItemInstance = null

        // Delay Code, used to test out of sequence responses
        /*
        def sleepTime = rand.nextInt(3000)
        print "Waiting...${sleepTime}"
        sleep(sleepTime)
        println "...Done."
         */

		def receivedDate = params.receiptDateInstance
		if ( ! receivedDate ) {
			receivedDate = new Date()
		} else {
                    // Mon Jan 31 13:38:42 CST 2011
                    receivedDate = Date.parse('EEE MMM d HH:mm:ss z yyyy', receivedDate)
                }
		//println "receiptItems:receivedDate::${receivedDate}"

        // prep all the things we'll need to send back
        def result = [
            success: false,
            trackedItemId: "",
            divId: 0,
            instrumentName: "",
            studyName: "",
            resultName: "",
            resultDate: null,
            trackingDocument: false,
            errorText: ""
        ]

        // if a div ID was passed, let's save it to the result set
        if (params?.divId) {
            result.divId = params.divId
        }

        if (params?.id){
            def barcodeValue = params?.id

            def receivedResult = Result.findByName('received')

            //println "barcodeValue: ${barcodeValue}"
            //println "barcodeValue[1]: ${barcodeValue[0]}"

            if (barcodeValue[0] == "I") {
                // we have an item
                def id = barcodeValue.replace("I", "")

                trackedItemInstance = TrackedItem.get(id)

                // get received status and received date
                if (trackedItemInstance) {
                    result.trackedItemId = trackedItemInstance.id
                    result.instrumentName = trackedItemInstance.batch.primaryInstrument.toString()
                    result.studyName =  trackedItemInstance.batch.primaryInstrument.study.toString()

                    if (trackedItemInstance.result) {
                        // item has been recepted already.  See what it was
                        result.resultDate = trackedItemInstance.result.receivedDate
                        result.resultName = trackedItemInstance.result.result.name

                        result.success = false
                        result.errorText = "Item already receipted ${result.resultDate}."

                    } else {
                        
                        // tie the result back to the item
                        trackedItemInstance.result = new ItemResult(result:receivedResult, 
							userCreated: username, appCreated: appName, receivedDate: receivedDate)
                        if ( trackedItemInstance.save(flush:true) ) {
                            result.success = true
                            result.resultDate = new LocalDate(trackedItemInstance.result.receivedDate).toString('MM-dd-yyyy')
                            result.resultName = trackedItemInstance.result.result.name
                        } else {

                            trackedItemInstance.errors.each{
                                println "   error: ${it}"
                            }
                            result.success = false
                            result.errorText = "unable to save result"
                        }
                    }
                }

            } else if (barcodeValue[0] == "B") {
                // we have a batch id
                result.trackingDocument = true

                try {
                    def batchId = Integer.parseInt(barcodeValue.replace("B", ""))

                    def batchInstance = Batch.get(batchId)
                    if (batchInstance) {
                        if (!batchInstance.trackingReturnDate) {

                            batchInstance.trackingReturnDate = receivedDate
                            batchInstance.save(flush:true)
                            result.success = true

                        result.trackedItemId = "Tracking Document for Batch # ${batchInstance.id}"
                        result.instrumentName = batchInstance.primaryInstrument.toString()
                        result.studyName =  batchInstance.primaryInstrument.study.toString()
                        result.resultDate = batchInstance.trackingReturnDate
                        result.resultName = "Received"

                        } else {
                            result.errorText = "Already Receipted on ${batchInstance.trackingReturnDate}"
                        }
                    } else {
                        result.errorText = "Batch does not exist!"
                    }

                } catch (Exception e) {
                    println "${e}"
                    result.errorText = "Invalid Batch id."
                }

            } else {
                // invalid barcode!
                result.errorText = "invalid barcode"
            }
        }

        render result as JSON

    }


}
