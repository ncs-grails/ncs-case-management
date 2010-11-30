package edu.umn.ncs

import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS'])
class ReceiptItemsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def authenticateService

    def rand = new Random()

    def index = {
        redirect(action: "receipt", params: params)
    }

    def receipt = { }

    def receiptItem = {

        // Check if it has I infront. If yes remove the "I" and proceed
        def username = authenticateService.principal().getUsername()
        def appName = "ncs-case-management"
        
        println "receiptItems params --> ${params}"
        def trackedItemInstance = null

        // Delay Code, used to test out of sequence responses
        /*
        def sleepTime = rand.nextInt(3000)
        print "Waiting...${sleepTime}"
        sleep(sleepTime)
        println "...Done."
         */

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

            println "barcodeValue: ${barcodeValue}"
            println "barcodeValue[1]: ${barcodeValue[0]}"

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
                        result.errorText = "Item already receipted."

                    } else {
                        
                        // tie the result back to the item
                        trackedItemInstance.result = new ItemResult(result:receivedResult, userCreated: username, appCreated: appName)
                        if ( trackedItemInstance.save(flush:true) ) {
                            result.success = true
                            result.resultDate = trackedItemInstance.result.receivedDate
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
            } else {
                // invalid barcode!
                result.errorText = "invalid barcode"
            }
        }

        render result as JSON

    }


}
