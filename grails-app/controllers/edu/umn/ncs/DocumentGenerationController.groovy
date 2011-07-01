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
        def username = authenticateService.principal().getUsername()
        def itemPassed = params.id
        def batchCreationQueueSourceInstance = null
        def batchCreationQueueInstance = null
		def bcqSource = params?.batchCreationQueueSource?.id
		def batchCreationConfigId = params?.batchCreationConfig?.id 
		def batchCreationConfigInstance = null
		
		if (batchCreationConfigId) {
			batchCreationConfigInstance = BatchCreationConfig.read(batchCreationConfigId)
		}
		
		def useParentItem = false
		
        def dwellingUnit = null
        def person = null
        def household = null
		
		def parentItemInstance = null
		def youCanDoIt = true

		if (bcqSource) {
			batchCreationQueueSourceInstance = BatchCreationQueueSource.read(bcqSource)
		}
		
		if (params.useParentItem == 'true') {
			useParentItem = true
		}

		if (useParentItem && patternParentItem.matcher(itemPassed).matches() 
				|| batchCreationQueueSourceInstance && pattern.matcher(itemPassed).matches()) {

			if (useParentItem){
					
				itemPassed = itemPassed.replace("I", "")
				
				// If the manual config uses a parent item, they MUST enter the
				// tracked item ID of the parent.
				// TODO: Make this optional so if the parent type is specified,
				// we can look it up based on a person, household, or dwelling unit ID
					parentItemInstance = TrackedItem.read(itemPassed)
					
					if (parentItemInstance) {
						if (batchCreationConfigInstance.oneBatchEventParentItem) {
							def item = documentGenerationService.getItemByParentAndConfig(parentItemInstance, batchCreationConfigInstance)
							if (item) {
								render "Failed to add item.  It's already been generated!"
								youCanDoIt = false
							}
						}
						
						// Check parent item instrument match config instrument
						if (batchCreationConfigInstance.parentInstrument) {
							if (batchCreationConfigInstance.parentInstrument != parentItemInstance?.batch?.primaryInstrument) {
								render "Failed to add item. Parent instrument must be ${batchCreationConfigInstance.parentInstrument.name}. Not ${parentItemInstance?.batch?.primaryInstrument.name}"
								youCanDoIt = false
							}
						} 
						
						if (batchCreationConfigInstance.parentResult) {
							if (parentItemInstance?.result?.result != batchCreationConfigInstance?.parentResult) {
								render "Failed to add item. It must have result: ${batchCreationConfigInstance.parentResult.name}. "
								youCanDoIt = false
							}
						}
						
						if (youCanDoIt) {
							// load the contact point
							dwellingUnit = parentItemInstance.dwellingUnit
							person = parentItemInstance.person
							household = parentItemInstance.household
							
							// look it up by parent item
							batchCreationQueueInstance = BatchCreationQueue.findByParentItemAndUsername(parentItemInstance, username)
						}
					}

			} else {
					
				if (batchCreationQueueSourceInstance.name == "dwellingUnit") {
					dwellingUnit = DwellingUnit.read(itemPassed)
					if (dwellingUnit) {
						batchCreationQueueInstance = BatchCreationQueue.findAllByDwellingUnitAndUsername(dwellingUnit, username)
					}
				} else if (batchCreationQueueSourceInstance.name == "person") {
					person = Person.read(itemPassed)
					if (person){
						batchCreationQueueInstance = BatchCreationQueue.findAllByPersonAndUsername(person, username)
					}
				} else if (batchCreationQueueSourceInstance.name == "household") {
					household = Household.read(itemPassed)
					if (household){
						batchCreationQueueInstance = BatchCreationQueue.findAllByHouseholdAndUsername(household, username)
					}
				}
			}
			
			if (!(dwellingUnit || household || person)) {
				if (youCanDoIt){
					render "Not found!"
				}
			} else {
			
				if (batchCreationQueueInstance) {
					render "Already in the queue"
				} else {
					batchCreationQueueInstance = new BatchCreationQueue(username: username,
						parentItem: parentItemInstance,
						dwellingUnit: dwellingUnit,
						person: person,
						household: household)

					if (batchCreationQueueInstance.save(flush:true)){
						render "Successfully added to the queue!"
					} else {
						batchCreationQueueInstance.errors.each{
							println "error: ${it}"
						}
					}
				}
			}
								
		}else{
			render "Invalid input!"
		}
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
