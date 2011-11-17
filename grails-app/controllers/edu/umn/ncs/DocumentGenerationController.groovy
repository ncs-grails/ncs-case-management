package edu.umn.ncs
// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.converters.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@Secured(['ROLE_NCS_DOCGEN'])
class DocumentGenerationController {
    def documentGenerationService
    def authenticateService
	def grailsApplication
	private boolean debug = true
	
	def downloadDocument = {
		
		def config = grailsApplication.config.ncs
		
		def batchCreationDocumentInstance = BatchCreationDocument.read(params?.id)
		
		if (batchCreationDocumentInstance) {
			// build the path
			def fileLocation = "${config.documents}/${batchCreationDocumentInstance.documentLocation}"
			
			// get the file
			def file = new File(fileLocation)
			
			if (file.exists()) {
				response.setContentType("application/octet-stream")
				response.setHeader("Content-disposition", "filename=${file.name}")
				response.outputStream << file.readBytes()
				return
			} else {
				println "Could not find file: ${fileLocation}"
				println "*************"
				println "Is 'config.documents' set properly in Config.groovy?  It is currently: ${config.documents}"
				println "Do you have the 'ncs-prod-docs repository checked out to '${config.documents}'?"
				println "*************"
				// actual file not found
				response.sendError(404)
				return
			}
			
		} else {
			println "BatchCreationDocument.id: ${params?.id} is missing!"
			response.sendError(404)
			return
		}
		 
	}
	
    // this sends a CSV file to the user on the other end
    def downloadDataset = {
        //println "params in downloadDataset --> ${params}"

        def batchInstance = Batch.read(params?.batch?.id)
        def batchCreationDocumentInstance = BatchCreationDocument.read(params?.batchCreationDocument?.id)

        // make sure the required params were passed
        if (batchInstance && batchCreationDocumentInstance) {

            // the file name... we should trim off the file name from the end of
            // the path.  (q:\stuff\data.csv --> data.csv)

            def filepath = batchCreationDocumentInstance.mergeSourceFile 
            def mergeSourceFile = new File(filepath).name

            def mergeSourceContents = documentGenerationService.generateMergeData(batchInstance, batchCreationDocumentInstance)

            response.setHeader("Content-disposition", "attachment; filename=\"${mergeSourceFile}\"");
			
            //render(contentType: "text/csv", text: mergeSourceContents)
			render(contentType: "application/octet-stream", text: mergeSourceContents)
            
            // Use for debugging (doesn't download)
            //render(text: mergeSourceContents);
        } else {
            flash.message = "something went horribly wrong. (We can't find the batch or the document)."
            redirect(action:"printDetails", params:['batch.id': batchInstance.id])
        }
    }
	
    def findItem = {

		// parentItem example: I62367
		def patternParentItem = ~/[a-zA-Z][0-9]*/
        def pattern = ~/[0-9]*/
		
		def renderView = "itemInfo"
		
		//${record.norcProjectId}-${record.norcSuId}-${record.norcDocId}
		def norcMailingPattern = ~/[0-9]{4}-[0-9]{8,10}-[0-9]{2,3}/
		
        def username = authenticateService.principal().getUsername()
        def itemPassed = params.id
        def batchCreationQueueSourceInstance = null
        def batchCreationQueueInstance = null
		def batchCreationQueueSourceId = params?.batchCreationQueueSource?.id
		def batchCreationConfigId = params?.batchCreationConfig?.id 
		
		def useParentItem = false
		
		if (batchCreationQueueSourceId) {
			batchCreationQueueSourceInstance = BatchCreationQueueSource.read(batchCreationQueueSourceId)
		}
		
		if (params.useParentItem == 'true') {
			useParentItem = true
		}
		
		def itemInfo = [
			success: false,
			itemPassed: itemPassed,
			useParentItem: useParentItem,
			batchCreationConfigId: batchCreationConfigId,
			batchCreationQueueSourceId: batchCreationQueueSourceId,
			parentItemInstance: null,
			dwellingUnit: null,
			person: null,
			household: null,
			youCanDoIt: true,
			batchCreationQueueInstance: null,
			queueId: 0,
			errorText: ""
		]
		
		// if queueId was passed let's save it to the itemInfo
		if (params?.queueId) {
			itemInfo.queueId = params.queueId
		}
		
		if (useParentItem && (patternParentItem.matcher(itemPassed).matches() || norcMailingPattern.matcher(itemPassed).matches()) 
				|| batchCreationQueueSourceInstance && pattern.matcher(itemPassed).matches()) {

			if (useParentItem){
				
				if (itemPassed[0].toUpperCase() == "I") {
					itemPassed = itemPassed.replace("I", "")
					itemInfo = _checkItem(itemPassed, itemInfo)

				} else if (norcMailingPattern.matcher(itemPassed).matches()) {
				
					// Looks like someone scanned in a NORC barcode...
					// Hmm....
					def barcodeParts = itemPassed.split('-')
					
					def norcProjectId = barcodeParts[0]
					def norcSuId = barcodeParts[1]
					def norcDocId = barcodeParts[2]
					
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
						itemInfo.errorText = "No items matched that NORC Mailing ID!"
					} else {
					
						if (trackedItemInstanceList.size() == 1) {
							// Do what you do when itemId scanned
							_checkItem(trackedItemInstanceList[0].id, itemInfo)
						} else {
							renderView = 'chooseItem'
							itemInfo.trackedItemInstanceList = trackedItemInstanceList
						}
					}
					
					
				} else {
					itemInfo.errorText = "invalid barcode"
				}

			} else {
					
				if (batchCreationQueueSourceInstance.name == "dwellingUnit") {
					itemInfo.dwellingUnit = DwellingUnit.read(itemPassed)
					if (itemInfo.dwellingUnit) {
						itemInfo.batchCreationQueueInstance = BatchCreationQueue.findAllByDwellingUnitAndUsername(itemInfo.dwellingUnit, username)
					}
				} else if (batchCreationQueueSourceInstance.name == "person") {
					itemInfo.person = Person.read(itemPassed)
					if (itemInfo.person){
						itemInfo.batchCreationQueueInstance = BatchCreationQueue.findAllByPersonAndUsername(itemInfo.person, username)
					}
				} else if (batchCreationQueueSourceInstance.name == "household") {
					itemInfo.household = Household.read(itemPassed)
					if (itemInfo.household){
						itemInfo.batchCreationQueueInstance = BatchCreationQueue.findAllByHouseholdAndUsername(itemInfo.household, username)
					}
				}
			}
			
			if (!(itemInfo.dwellingUnit || itemInfo.household || itemInfo.person)) {
				if (itemInfo.youCanDoIt){
					itemInfo.errorText = "Not found!"
				}
			} else {
			
				if (itemInfo.batchCreationQueueInstance) {
					itemInfo.errorText = "Already in the queue"
				} else {
					batchCreationQueueInstance = new BatchCreationQueue(username: username,
						parentItem: itemInfo.parentItemInstance,
						dwellingUnit: itemInfo.dwellingUnit,
						person: itemInfo.person,
						household: itemInfo.household)

					if (batchCreationQueueInstance.save(flush:true)){
						itemInfo.success = true
						itemInfo.batchCreationQueueInstance = batchCreationQueueInstance
					} else {
						batchCreationQueueInstance.errors.each{
							println "error: ${it}"
						}
					}
				}
			}
								
		}else{
			itemInfo.errorText = "Invalid input!"
		}
		
		render(view: renderView, model: itemInfo)
    }
	
	
	def cancelItem = {}
	
	private def _checkItem(itemPassed, itemInfo){
		def username = authenticateService.principal().getUsername()
		def batchCreationConfigInstance = null
		
		if (itemInfo.batchCreationConfigId) {
			batchCreationConfigInstance = BatchCreationConfig.read(itemInfo.batchCreationConfigId)
		}
	
		// If the manual config uses a parent item, they MUST enter the
		// tracked item ID of the parent.
		// TODO: Make this optional so if the parent type is specified,
		// we can look it up based on a person, household, or dwelling unit ID
			itemInfo.parentItemInstance = TrackedItem.read(itemPassed)
			def parentItemInstance = itemInfo.parentItemInstance
			
			if (parentItemInstance) {
				if (batchCreationConfigInstance.oneBatchEventParentItem) {
					def item = documentGenerationService.getItemByParentAndConfig(parentItemInstance, batchCreationConfigInstance.instrument)
					if (item) {
						itemInfo.errorText = "It's already been generated!"
						itemInfo.youCanDoIt = false
					}
				}
				
				// Check parent item instrument match config instrument
				if (batchCreationConfigInstance.parentInstrument) {
					
					def previousInstrument = parentItemInstance?.batch?.primaryInstrument?.previousInstrument

					if (batchCreationConfigInstance.parentInstrument != parentItemInstance?.batch?.primaryInstrument && previousInstrument?.id != batchCreationConfigInstance.parentInstrument?.id) {
						itemInfo.errorText = "Parent instrument must be ${batchCreationConfigInstance.parentInstrument.name}. Not ${parentItemInstance?.batch?.primaryInstrument.name}"
						itemInfo.youCanDoIt = false
					}
				}
				
				if (batchCreationConfigInstance.direction) {
					if (batchCreationConfigInstance.direction.id != parentItemInstance?.batch?.direction?.id) {
						itemInfo.errorText = "Parent direction must be: '${batchCreationConfigInstance.direction.name}'. Not '${parentItemInstance?.batch?.direction?.name}'"
						itemInfo.youCanDoIt = false
					}
				}
				
				if (batchCreationConfigInstance.parentFormat) {
					if (batchCreationConfigInstance.parentFormat.id != parentItemInstance?.batch?.format?.id) {
						itemInfo.errorText = "Parent format must be: '${batchCreationConfigInstance.parentFormat.name}'. Not '${parentItemInstance?.batch?.format?.name}'"
						itemInfo.youCanDoIt = false
					}
				}
				
				if (batchCreationConfigInstance.parentResult) {
					if (parentItemInstance?.result?.result != batchCreationConfigInstance?.parentResult) {
						itemInfo.errorText = "It must have result: ${batchCreationConfigInstance.parentResult.name}. "
						itemInfo.youCanDoIt = false
					}
				}
				
				if (itemInfo.youCanDoIt) {
					// load the contact point
					itemInfo.dwellingUnit = parentItemInstance.dwellingUnit
					itemInfo.person = parentItemInstance.person
					itemInfo.household = parentItemInstance.household
					
					// look it up by parent item
					itemInfo.batchCreationQueueInstance = BatchCreationQueue.findByParentItemAndUsername(parentItemInstance, username)
				}
			}
			
			return itemInfo
	}

	
	
	
    def find = {
        flash.message = null
        def batchCreationQueue = null
        def username = authenticateService.principal().getUsername()

        def person = null
        def household = null
        def dwellingUnit = null

        if (params.sourceId && params.sourceValue) {

            def source = BatchCreationQueueSource.read(params.sourceId)

            if (source.name == 'person' ) {
                person = Person.read(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findAllWhere(username:username, source:source, person:person)
            } else if (source.name == 'household' ) {
                household = Household.read(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findAllWhere(username:username, source:source, household:household)
            } else if (source.name == 'dwellingUnit' ) {
                // println "Creating DwellingUnit ... from Id --> ${params.sourceValue}"
                dwellingUnit = DwellingUnit.read(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findAllWhere(username:username, source:source, dwellingUnit:dwellingUnit)
            }

            if (person || household || dwellingUnit) {
                if (!batchCreationQueue) {
                    batchCreationQueue = new BatchCreationQueue(username:username, 
                                                            source:source,
                                                            person:person,
                                                            household:household,
                                                            dwellingUnit:dwellingUnit)
                    batchCreationQueue.save(flush:true)
                    
                }
            } else {
                flash.message = "Source and ID required."
            }
        }

        def batchCreationQueueList = null
        def nBatchCreationQueue = BatchCreationQueue.count()
        // println "nBatchCreationQueue -> ${nBatchCreationQueue}"
        if (nBatchCreationQueue > 0 ) {
            batchCreationQueueList = BatchCreationQueue.list()
        }
        [batchCreationQueueList:batchCreationQueueList]
    }

    def index = { 
        redirect(action:'generation', params:params)
    }

    // display details for printing the batch
    def printDetails = {
		
		def config = grailsApplication.config.ncs

        def batchInstance = Batch.read(params?.batch?.id)
			if (batchInstance) {
	        def batchCreationConfigInstance = batchInstance?.creationConfig
			if (! batchCreationConfigInstance ) {
				batchCreationConfigInstance = BatchCreationConfig.read(params?.batchCreationConfig?.id)
			}
	
			def saveLocation = config.saveLocation
			
	        [ batchCreationConfigInstance: batchCreationConfigInstance, 
				batchInstance: batchInstance, saveLocation: saveLocation ]
		} else {
			flash.message = "Unknown Batch"
			redirect(action:'index')
		}
    }

    // display a batch report
    def batchReport = {

        // primary batch
        def batchInstance = Batch.read(params?.id)

        // child batches
        def batchInstanceList = []
        def sampleDocuments = []
        def allTrackingDocuments = []

        // If we didn't find the batch, see if the param is batch.id instead of id
        if (!batchInstance) {
            batchInstance = Batch.read(params?.batch?.id)
        }

        // Hopefully we found one by now.  If so, let's fill the batch report
        // batch list.  Master first.
        if (batchInstance) {
            allTrackingDocuments = batchInstance?.creationConfig?.recipients
            batchInstance?.creationConfig?.recipients?.each{
                if (it?.person?.firstName == "Sample") {
                   sampleDocuments.add(it)
                }
            }
            // Add master batch to the list
            batchInstanceList.add(batchInstance)
            // add all the batches that belong to the master
            batchInstanceList.addAll(Batch.findAllByMaster(batchInstance))
        }

        // Pack it up and ship it off to the view...
        [batchInstance:batchInstance, 
            batchInstanceList:batchInstanceList,
            sampleDocumentsTotal: sampleDocuments.size(),
            notSampleTrackingDocumentsTotal: allTrackingDocuments.size() - sampleDocuments.size()]
    }

    // here is the batch generation FSM
    def generationFlow = {
        
        // WARNING:  Any thing get gets passed as part of the model
        // needs to implement Serializable.  Otherwise everything comes
        // crumblig down.
        loadRecentBatches {
            action {

                def username = authenticateService?.principal()?.getUsername()
                // println "loadRecentBatches params: ${params}"

                def q = params?.q
                // println "${q}"
				
                // set a default value for batchInstance
                flow.batchInstance = [id:0]

                // List of matching configs per search criteria
                def batchCreationConfigInstanceList = []
                // list of recently generated mailing types
                def batchCreationConfigRecentList = []

                def aboutSixMonthsAgo = (new Date()) - 180

                // find recently generated batch config types
                def criteria = BatchCreationConfig.createCriteria()

                batchCreationConfigRecentList = criteria.listDistinct{
                    batches {
                        eq('batchRunBy', username)
                        gt('dateCreated', aboutSixMonthsAgo)
                    }
					
                }

                //order('dateCreated', 'desc')

                // if a query was passed, we'll run it
                if (q) {
                    criteria = BatchCreationConfig.createCriteria()

                    batchCreationConfigInstanceList = criteria.listDistinct{
						and {
							eq('active', true)
							or {
								ilike('name', "%${q}%")
								instrument {
									or {
										study {
											or {
												ilike('name', "%${q}%")
												ilike('fullName', "%${q}%")
											}
										}
										ilike('name', "%${q}%")
										ilike('nickName', "%${q}%")
									}
								}
							}
						}
                    }
                }

                [batchCreationConfigInstanceList:batchCreationConfigInstanceList,
                    batchCreationConfigRecentList:batchCreationConfigRecentList]
            }
            on("success").to("listConfig")
        }
        listConfig {
            on("search").to "loadRecentBatches"
            on("load").to "loadConfig"
        }
        loadConfig {
            action {
                // return instance of config
                def batchCreationConfigInstance = BatchCreationConfig.read(params?.id)
                def mailDate = new Date()
                def instrumentDate = new Date()
                def useMaxPieces = false

                if (batchCreationConfigInstance) {
                    // Save this to the flow
                    flow.batchCreationConfigInstance = batchCreationConfigInstance

                    mailDate = mailDate + batchCreationConfigInstance.mailDateDaysShift
                    if (batchCreationConfigInstance.maxPieces > 0) {
                        useMaxPieces = true
                    }
                }

                // list the batches

                def c = Batch.createCriteria()
                def batchInstanceList = c.list{
                    and {
                        isNull("master")
                        creationConfig{
                            eq("id", batchCreationConfigInstance.id)
                        }
                    }
                    order("id", "desc")
                }
                
                    /*def batchInstanceList = batchCreationConfigInstance?.batches
                        .findAll{it.master == null }
                        .sort{ -it?.id }
                        */
                

                [batchCreationConfigInstance:batchCreationConfigInstance,
                    mailDate:mailDate,
                    instrumentDate:instrumentDate,
                    useMaxPieces:useMaxPieces,
                    batchInstanceList: batchInstanceList]
            }
            on("success").to "showConfig"
            on("error"){
                println "ERROR: couldn't run [showConfig] from [loadConfig]"
            }.to "loadRecentBatches"
        }
        showConfig {
            on("return").to "loadRecentBatches"
            on("manualGenerateAction"){
                // Clear the user's manual queue
                def username = authenticateService?.principal()?.getUsername()
                def sql = "delete BatchCreationQueue bcq where bcq.username = ?"
                BatchCreationQueue.executeUpdate(sql, [username])

                if (params?.instrumentDate) {
                    flow.instrumentDate = params.instrumentDate
                } else {
                    flow.instrumentDate = null
                }
                if (params?.autoSetMailDate) {
                    flow.autoSetMailDate = params.autoSetMailDate
                    flow.mailDate = params.mailDate
                }
            }.to "manualGenerate"
            on("autoGenerate").to "autoGenerate"
            on("optionalGenerate").to "optionalGenerate"
            on("reGenerate").to "showPrintDetails"
        }
        manualGenerate {
            on("generateDocuments").to "generateDocuments"
            on("return").to "showConfig"
            on("error").to "errorGeneratingBatch"
        }
        generateDocuments {
            action {
                // ** Manually Generate Documents **
                def batchInstance = null

                // def batchCreationConfigInstance = BatchCreationConfig.read(params?.batchCreationConfigInstance?.id)
                // pull the creation config from the flow scope
                def batchCreationConfigInstance = flow.batchCreationConfigInstance
                def username = authenticateService?.principal()?.getUsername()
                def docGenParams = [manual:true, username:username]

                if (batchCreationConfigInstance) {
                    docGenParams.config = batchCreationConfigInstance

                    if (params.autoSetMailDate) {
                        docGenParams.mailDate = params.mailDate
                    } else if (flow?.autoSetMailDate) {
                        docGenParams.mailDate = flow.mailDate
                    }

                    if (params?.instrumentDate) {
                        docGenParams.instrumentDate = params.instrumentDate
                    } else if (flow?.instrumentDate){
                        docGenParams.instrumentDate = flow.instrumentDate
                    }
                    

                    if (params.useMaxPieces == 'true') {
                        docGenParams.maxPieces = params.maxPieces
                    }

                    batchInstance = documentGenerationService.generateMailing(docGenParams)
                    
                    // save it to the flow
                    if (batchInstance?.id) {
                        flow.batchInstance = batchInstance
                    } else {
                        // error creating batch
                        flow.batchInstance = [id:0]
                        return nobatch()
                    }
                }

                [batchCreationConfigInstance:batchCreationConfigInstance,
                    batchInstance:batchInstance]
            }
            on("success").to "showPrintDetails"
            on("nobatch").to "nothingToGenerate"
            on("return").to "showConfig"
            on("error").to "errorGeneratingBatch"
            
        }
		nothingToGenerate{
            on("return").to "showConfig"
		}
        optionalGenerate{
            on("return").to "showConfig"
        }
        autoGenerate{
            action {
				
                // ** automatically generate documents **
                def batchInstance = null
                def batchCreationConfigInstance = BatchCreationConfig.read(params?.id)
                def results = null
                def username = authenticateService?.principal()?.getUsername()
                def docGenParams = [manual:false, username:username]

                if (batchCreationConfigInstance) {
                    docGenParams.config = batchCreationConfigInstance

                    if (params.autoSetMailDate) {
                        docGenParams.mailDate = params.mailDate
                    }
                    if (params.useMaxPieces == 'true') {
                        docGenParams.maxPieces = params.maxPieces
                    }

                    docGenParams.instrumentDate = params.instrumentDate
					
                    batchInstance = documentGenerationService.generateMailing(docGenParams)
                    
                    if (batchInstance) {
                        flow.batchInstance = batchInstance
                    } else {
                        // error creating batch
                        flow.batchInstance = [id:0]
                        return nobatch()
                    }

                }
                [batchCreationConfigInstance:batchCreationConfigInstance,
                    batchInstance:batchInstance]
            }
            on("success").to "showPrintDetails"
            on("nobatch").to "nothingToGenerate"
            on("error").to "errorGeneratingBatch"
        }
        errorGeneratingBatch{
            
        }
        showPrintDetails{
            redirect(controller:"documentGeneration",action:'printDetails', 
                params:['batchCreationConfig.id':flow?.batchCreationConfigInstance?.id,
                    'batch.id':flow?.batchInstance?.id])
        }
    }
}
