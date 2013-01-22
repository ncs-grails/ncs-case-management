package edu.umn.ncs

import grails.converters.*
import org.joda.time.*
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_RECEIPT'])
class ReceiptItemsController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def springSecurityService
	def receiptItemsService

	def rand = new Random()
	def debug = true

	def index = {
		
		if (debug) { 
			log.debug ""
			log.debug "RECEIPT-ITEMS CONTROLLER > INDEX --------------------------------"
			log.debug "=> params: ${params}" 
		}
		redirect(action: "receipt", params: params)

	}

	def receipt = {

		if (debug) { 
			log.debug ""
			log.debug "RECEIPT-ITEMS CONTROLLER > RECEIPT ------------------------------"
			log.debug "=> params: ${params}" 
	   	}

		def receiptDate = params.receiptDate
		if (debug && receiptDate) { log.debug "=> receiptDate (based on params.receiptDate): ${receiptDate}" }
		if ( ! receiptDate ) {
			receiptDate = new Date()
			if (debug) { log.debug "=> receiptDate (new date b/c params.receiptDate is null): ${receiptDate}" }
		}

		[ receiptDate: receiptDate ]

	}

	def receiptItem = {
		
		if (debug) { 
			log.debug ""
			log.debug "RECEIPT-ITEMS CONTROLLER > RECEIPT-ITEM --------------------------"
			log.debug "=> params: ${params}" 
		}

		def renderView = "itemResult"
		if (debug) { log.debug "=> renderView: ${renderView}" }

		//${record.norcProjectId}-${record.norcSuId}-${record.norcDocId}
		def norcMailingPattern = ~/[0-9]{4}-[0-9]{8,10}-[0-9]{2,3}/		
		//if (debug) { log.debug "=> norcMailingPattern: ${norcMailingPattern}" }

		/*
		// Delay Code, used to test out sequence responses       
		def sleepTime = rand.nextInt(3000)
		print "Waiting...${sleepTime}"
		sleep(sleepTime)
		println "...Done."
		*/

		def receivedDate = params.receiptDateInstance
		if ( !receivedDate ) {
			receivedDate = new Date()
			if (debug) { log.debug "=> receivedDate (new date because params.receiptDateInstance is null): ${receivedDate}" }
		} else {
			receivedDate = Date.parse('EEE MMM d HH:mm:ss z yyyy', receivedDate)
			if (debug) { log.debug "=> receivedDate (based on params.receiptDateInstance): ${receivedDate}" }
		}

		// prep data send back
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
		if (debug) { log.debug "=> result: ${result}" }

		// if a div ID was passed, save it to the result set
		if (debug) { log.debug "=> params?divId: ${params.divId}" }
		if (params?.divId) {
			result.divId = params.divId
			if (debug) { log.debug "=> result.divId (based on params?divId): ${result.divId}" }
		}

		// Examine barcode entered, and then process it
		if (params?.id) {

			if (debug) { log.debug "=> if (params?.id) = TRUE" }
			
			def barcodeValue = params?.id

			if (debug) { log.debug "=> barcodeValue (based on params?.id): ${barcodeValue}" }
			
			// Barcode has prefix 'I' for tracked item -- remove prefix
			if (barcodeValue[0].toUpperCase() == "I") {

				if (debug) { log.debug "=> barcode is Tracking Item Id" }

				def id = barcodeValue.replace("I", "")

				result = receiptItemsService.receiptItem(id, receivedDate, result)
				if (debug) { log.debug "=> result: ${result}" }
			

			// Barcode has prefix 'B' for batch
			} else if (barcodeValue[0].toUpperCase() == "B") {

				if (debug) { log.debug "=> barcode is Batch Id" }

				result.trackingDocument = true

				try {

					def batchId = Integer.parseInt(barcodeValue.replace("B", ""))
					def batchInstance = Batch.get(batchId)
					if (debug) { log.debug "=> batchId: ${batchId}" }

					if (batchInstance) {

						if (debug) { log.debug "=> batchInstance (based on batchId): ${batchInstance}" }

						if (!batchInstance.trackingReturnDate) {

							if (debug) { log.debug "=> batch not receipted yet (b/c batchInstance.trackingReturnDate is null)" }
                            
							batchInstance.trackingReturnDate = receivedDate
							batchInstance.save(flush:true)
							result.success = true

							result.instrumentName = batchInstance.primaryInstrument.toString()
							result.studyName =  batchInstance.primaryInstrument.study.toString()
							result.resultDate = batchInstance.trackingReturnDate
						
							result.resultName = "Received"

							if (debug) { 
								log.debug "=> batchInstance.trackingReturnDate (based on receiveDate): ${batchInstance.trackingReturnDate}" 
								log.debug "=> result.instrumentName (based in primaryInstrument): ${result.instrumentName}" 
								log.debug "=> result.studyName (based in primaryInstrument.study): ${result.studyName}" 
								log.debug "=> result.resultDate (based ontrackingReturnDate): ${result.resultDate}" 
								log.debug "=> result.resultName: ${result.resultName}" 
							}

						} else {

							if (debug) { log.debug "=> batch already receipted (based on batchInstance.trackingReturnDate): ${batchInstance.trackingReturnDate}" }
							result.errorText = "Batch already receipted on ${batchInstance.trackingReturnDate}"

						}

					} else {

						if (debug) { log.debug "=> cannot create batchInstance b/c batchId ${batchId} does not exist" }
						result.errorText = "Batch does not exist!"
					}

				} catch (Exception e) {
					log.debug e
					result.errorText = "Invalid Batch Id."
				}

			// Barcode is a NORC barcode
			} else if ( norcMailingPattern.matcher(barcodeValue).matches() ) {
			
				// Looks like someone scanned in a NORC barcode...Hmm....
				if (debug) { log.debug "=> barcode is a NORC Id" }

				def barcodeParts = barcodeValue.split('-')
				def norcProjectId = barcodeParts[0]
				def norcSuId = barcodeParts[1]
				def norcDocId = barcodeParts[2]
				if (debug) { 
					log.debug "=> barcodeParts: ${barcodeParts}" 
					log.debug "=> norcProjectId: ${norcProjectId}" 
					log.debug "=> norcSuId: ${norcSuId}" 
					log.debug "=> norcDocId: ${norcDocId}" 
				}

				// Rerouting 119 to 122
				// Reason: 3500 EQs went out with norcDocId: 119 instead of 122 (JS late update)
				if (norcDocId == "119") { 
					norcDocId = "122" 
				}
				
				// look up the EnHS.HS Equivalent
				def studyInstance = StudyLink.findByNorcProjectId(norcProjectId)?.study
				def dwellingUnitInstance = DwellingUnitLink.findByNorcSuId(norcSuId)?.dwellingUnit
				def personInstance = PersonLink.findByNorcSuId(norcSuId)?.person
				def instrumentInstanceList = InstrumentLink.findAllByNorcDocId(norcDocId)?.collect{ it.instrument }
				if (debug) { 
					log.debug "=> studyInstance: ${studyInstance}" 
					log.debug "=> dwellingUnitInstance: ${dwellingUnitInstance}" 
					log.debug "=> personInstance: ${personInstance}" 
					log.debug "=> instrumentInstanceList: ${instrumentInstanceList}" 
				}
				
				def trackedItemInstanceList = []
				
				// Get Tracking Item
				if (studyInstance && dwellingUnitInstance && instrumentInstanceList) {
					
					// get a list of tracked items for this dwelling unit and instrument combo
					if (debug) { log.debug "=> get Tracked Item, if (studyInstance && dwellingUnitInstance && instrumentInstanceList)" }
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
					
					// get a list of tracked items for this person and instrument combo
					if (debug) { log.debug "=> get Tracked Item, if (studyInstance && personInstance && instrumentInstanceList)" }
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

					if (debug) { log.debug "=> no tracked item found" }
					result.errorText = "No items matched that NORC Mailing ID!"

				} else {
				
					if (trackedItemInstanceList.size() == 1) {

						//result = receiptItemsService.receiptItem(trackedItemInstanceList[0]?.id, receivedDate, result)
						if (debug) { 
							log.debug "=> one tracked item found" 
							log.debug "=> result: ${result}" 
						}
					
					} else {

						renderView = 'chooseItem'
						result.trackedItemInstanceList = trackedItemInstanceList
						if (debug) { 
							log.debug "=> multiple tracked items found" 
							log.debug "=> result.trackedItemInstanceList: ${result.trackedItemInstanceList}" 
							log.debug "=> renderView set to: ${renderView}"
						}
					
					}
				
				}

			// invalid barcode emtry
			} else {

				if (debug) { log.debug "=> invalid barcode entry" }
				result.errorText = "Invalid Barcode!!"

			}

		} // if (params?.id)

		// send the response back as an HTML snippet by rendering it from a GSP
		render(view:renderView, model:result)

	} // def receiptItem 

	def cancelItem = { }
	
}
