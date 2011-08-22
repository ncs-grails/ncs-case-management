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
		
		def renderView = "itemResult"

		//${record.norcProjectId}-${record.norcSuId}-${record.norcDocId}
		def norcMailingPattern = ~/[0-9]{4}-[0-9]{8,10}-[0-9]{2,3}/		

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

            //println "barcodeValue: ${barcodeValue}"
            //println "barcodeValue[1]: ${barcodeValue[0]}"

			// Check if it has I infront. If yes remove the "I" and proceed
            if (barcodeValue[0].toUpperCase() == "I") {
                // we have an item
                def id = barcodeValue.replace("I", "")

				result = _receiptItem(id, receivedDate, result)
			
            } else if (barcodeValue[0].toUpperCase() == "B") {
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

            } else if (	norcMailingPattern.matcher(barcodeValue).matches() ) {
			
				// Looks like someone scanned in a NORC barcode...
				// Hmm....
				def barcodeParts = barcodeValue.split('-')
				
				def norcProjectId = barcodeParts[0]
				def norcSuId = barcodeParts[1]
				def norcDocId = barcodeParts[2]
				
				// look up the EnHS.HS Equivalent
				def studyInstance = StudyLink.findByNorcProjectId(norcProjectId)?.study
				def dwellingUnitInstance = DwellingUnitLink.findByNorcSuId(norcSuId)?.dwellingUnit
				def personInstance = PersonLink.findByNorcSuId(norcSuId)?.person
				def instrumentInstanceList = InstrumentLink.findAllByNorcDocId(norcDocId)?.collect{ it.instrument }
				
				def trackedItemInstanceList = []
				
				// If everything went as expected...
				if (studyInstance && dwellingUnitInstance && instrumentInstanceList) {
					
					// get a list of tracked items for this dwelling unit and instrument combo
					trackedItemInstanceList = TrackedItem.createCriteria().listDistinct{
						and{
							eq('dwellingUnit', dwellingUnitInstance)
							batch{
								instruments{
									'in'("instrument", instrumentInstanceList)
								}
							}
						}
					}
				} else if (studyInstance && personInstance && instrumentInstanceList) {
					// get a list of tracked items for this dwelling unit and instrument combo
					trackedItemInstanceList = TrackedItem.createCriteria().listDistinct{
						and{
							eq('person', personInstance)
							batch{
								instruments{
									'in'("instrument", instrumentInstanceList)
								}
							}
						}
					}

				}
				
				if ( ! trackedItemInstanceList ) {
					// No can do.
					result.errorText = "No items matched that NORC Mailing ID!"
				} else {
				
					if (trackedItemInstanceList.size() == 1) {
						result = _receiptItem(trackedItemInstanceList[0]?.id, receivedDate, result)
					} else {
						renderView = 'chooseItem'
						result.trackedItemInstanceList = trackedItemInstanceList
					}
				}
			} else {
                // invalid barcode!
                result.errorText = "invalid barcode"
            }
        }

		// send the response back as an HTML snippet by rendering it from a GSP
        render(view:renderView, model:result)

    }

	def cancelItem = { }
	
	private def _receiptItem(itemId, receivedDate, result){
		
		def username = authenticateService.principal().getUsername()
		def appName = "ncs-case-management"
		
		def receivedResult = Result.findByName('received')
		
		def trackedItemInstance = TrackedItem.get(itemId)
		
		if (!trackedItemInstance) {
			result.success = false
			result.errorText = "   Item not found."
		} else {
			// get received status and received date
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
					userCreated: username,
					appCreated: appName,
					receivedDate: receivedDate)
				
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
		
		
		
		return result
	}
}
