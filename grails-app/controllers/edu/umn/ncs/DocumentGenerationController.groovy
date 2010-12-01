package edu.umn.ncs
// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
class DocumentGenerationController {
    def documentGenerationService
    def reportService
    def username = 'ajz'

     // this is for testing, TODO: DELETE ME!
    def testGenerate = {
        def batchCreationConfigInstance = BatchCreationConfig.get(1)
        def docGenParams = [manual: false,
            username:username,
            config:batchCreationConfigInstance]

        def batchInstance = documentGenerationService.generateMailing(docGenParams)
        [batchCreationConfigInstance:batchCreationConfigInstance,
            batchInstance:batchInstance]
    }

    // this sends a CSV file to the user on the other end
    def downloadDataset = {
        //println "params in downloadDataset --> ${params}"

        def batchInstance = Batch.get(params?.batch?.id)
        def batchCreationDocumentInstance = BatchCreationDocument.get(params?.batchCreationDocument?.id)

        // make sure the required params were passed
        if (batchInstance && batchCreationDocumentInstance) {

            // the file name... we should trim off the file name from the end of
            // the path.  (q:\stuff\data.csv --> data.csv)

            def filepath = batchCreationDocumentInstance.mergeSourceFile
            def mergeSourceFile = new File(filepath).name

            def mergeSourceContents = documentGenerationService.generateMergeData(batchInstance, batchCreationDocumentInstance)

            response.setHeader("Content-disposition", "attachment; filename=\"${mergeSourceFile}\"");
            render(contentType: "text/csv", text: mergeSourceContents);
            
            // Use for debugging (doesn't download)
            //render(text: mergeSourceContents);
        } else {
            flash.message = "something went horribly wrong. (We can't find the batch or the document)."
            redirect(action:"printDetails")
        }
    }

    def find = {
        flash.message = null
        def batchCreationQueue = null

        def person = null
        def household = null
        def dwellingUnit = null

        if (params.sourceId && params.sourceValue) {

            def source = BatchCreationQueueSource.get(params.sourceId)

            if (source.name == 'person' ) {
                person = Person.get(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findAllWhere(username:username, source:source, person:person)
            } else if (source.name == 'household' ) {
                household = Household.get(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findAllWhere(username:username, source:source, household:household)
            } else if (source.name == 'dwellingUnit' ) {
                // println "Creating DwellingUnit ... from Id --> ${params.sourceValue}"
                dwellingUnit = DwellingUnit.get(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findAllWhere(username:username, source:source, dwellingUnit:dwellingUnit)
            }

            if (person || household || dwellingUnit) {
                if (!batchCreationQueue) {
                    batchCreationQueue = new BatchCreationQueue(username:username, source:source, person:person, household:household, dwellingUnit:dwellingUnit)
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

        // println "This is what we got for print details: \n ${params}"
        
        def batchCreationConfigInstance = BatchCreationConfig.get(params?.batchCreationConfig?.id)
        def batchInstance = Batch.get(params?.batch?.id)

        [batchCreationConfigInstance:batchCreationConfigInstance, batchInstance:batchInstance]
    }

    // display a batch report
    def batchReport = {

        // primary batch
        def batchInstance = Batch.get(params?.id)
        // child batches
        def batchInstanceList = []

        // If we didn't find the batch, see if the param is batch.id instead of id
        if (!batchInstance) {
            batchInstance = Batch.get(params?.batch?.id)
        }

        // Hopefully we found one by now.  If so, let's fill the batch report
        // batch list.  Master first.
        if (batchInstance) {
            // Add master batch to the list
            batchInstanceList.add(batchInstance)
            // add all the batches that belong to the master
            batchInstanceList.addAll(Batch.findAllByMaster(batchInstance))
        }

        // Pack it up and ship it off to the view...
        [batchInstance:batchInstance, batchInstanceList:batchInstanceList]
    }

    def batchAnalysis = {

        def batchCreationConfigInstance = BatchCreationConfig.get(params?.batchCreationConfig?.id)

        // child batches
        def batchInstanceList = []
        def batchInstance = Batch.get(params?.batch?.id)

        def advLetterSentInstanceList = reportService.batchAnalysis(batchInstance.id)

            // get a field list
            /*def firstRow = advLetterSentInstanceList[0]
            def columnNames = firstRow.collect{ it.key }

            columnNames.each{
                println "Dataset columnNames >> ${it}"
            }
            */

        [advLetterSentInstanceList: advLetterSentInstanceList,
            advLetterSentInstanceTotal: advLetterSentInstanceList.count(),
            batchCreationConfigInstance: batchCreationConfigInstance,
            batchInstance: batchInstance]
    }


    // here is the batch generation FSM
    def generationFlow = {
        // WARNING:  Any thing get gets passed as part of the model
        // needs to implement Serializable.  Otherwise everything comes
        // crumblig down.
        loadRecentBatches {
            action {

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

                //		order('dateCreated', 'desc')



                // if a query was passed, we'll run it
                if (q) {
                    criteria = BatchCreationConfig.createCriteria()

                    batchCreationConfigInstanceList = criteria.listDistinct{
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
                def batchCreationConfigInstance = BatchCreationConfig.get(params?.id)
                def mailDate = new Date()
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
                def batchInstanceList = batchCreationConfigInstance?.batches
                    .findAll{it.master == null && it.pieces > 0 }
                    .sort{ -it?.id }

                [batchCreationConfigInstance:batchCreationConfigInstance,
                    mailDate:mailDate,
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
                def sql = "delete BatchCreationQueue bcq where bcq.username = ?"
                BatchCreationQueue.executeUpdate(sql, [username])
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

                // def batchCreationConfigInstance = BatchCreationConfig.get(params?.batchCreationConfigInstance?.id)
                // pull the creation config from the flow scope
                def batchCreationConfigInstance = flow.batchCreationConfigInstance
                def docGenParams = [manual:true, username:username]

                if (batchCreationConfigInstance) {
                    docGenParams.config = batchCreationConfigInstance

                    if (params.autoSetMailDate) {
                        docGenParams.mailDate = params.mailDate
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
                def batchCreationConfigInstance = BatchCreationConfig.get(params?.id)
                def results = null
                def docGenParams = [manual:false, username:username]

                if (batchCreationConfigInstance) {
                    docGenParams.config = batchCreationConfigInstance

                    if (params.autoSetMailDate) {
                        docGenParams.mailDate = params.mailDate
                    }
                    if (params.useMaxPieces == 'true') {
                        docGenParams.maxPieces = params.maxPieces
                    }
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
