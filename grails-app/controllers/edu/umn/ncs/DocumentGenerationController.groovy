package edu.umn.ncs

class DocumentGenerationController {
    javax.sql.DataSource dataSource
    def documentGenerationService
    def username = 'ajz'

    //
    // TODO: Show BatchQueue
    // TODO: Show/Add items per Person
    // TODO: Show/Add items per dwelling unit

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
                batchCreationQueue = BatchCreationQueue.findWhere(username:username, source:source, person:person)
            } else if (source.name == 'household' ) {
                household = Household.get(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findWhere(username:username, source:source, household:household)
            } else if (source.name == 'dwellingUnit' ) {
                dwellingUnit = DwellingUnit.get(params.sourceValue)
                batchCreationQueue = BatchCreationQueue.findWhere(username:username, source:source, dwellingUnit:dwellingUnit)
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
        println "nBatchCreationQueue -> ${nBatchCreationQueue}"
        if (nBatchCreationQueue > 0 ) {
            batchCreationQueueList = BatchCreationQueue.list()
        }
        [batchCreationQueueList:batchCreationQueueList]
    }

    def index = { 
        redirect(action:'generation', params:params)
    }
	
    // display a batch report
    def batchReport = {

        def batchInstance = Batch.get(params.id)
		def batchInstanceList = []
		
        if (!batchInstance) {
              batchInstance = Batch.get(params.batch?.id)
        }

        if (batchInstance) {
			def batchCreationConfigInstance = batchInstance.creationConfig

			if (batchCreationConfigInstance.instrument != batchInstance.primaryInstrument) {
				// This batch is not the primary batch of the run... let's find it.
				println "This is not the primary batch!"
			}

			// Now we need to find all of the sister / child batches

            println "Batch ID: ${batchInstance.id}"
        }
        [batchInstance:batchInstance]
    }

    def testGenerate = {
        def batchCreationConfigInstance = BatchCreationConfig.get(1)
        def docGenParams = [manual: false,
            username:username,
            config:batchCreationConfigInstance]

        def batchInstance = documentGenerationService.generateMailing(docGenParams)
        [batchCreationConfigInstance:batchCreationConfigInstance,
            batchInstance:batchInstance]
    }

	// here is the batch generation FSM
    def generationFlow = {
        // WARNING:  Any thing get gets passed as part of the model
        // needs to implement Serializable.  Otherwise everything comes
        // crumblig down.
        loadRecentBatches {
            action {

            println "loadRecentBatches params: ${params}"

            def q = params.q
            println "${q}"

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
                def batchCreationConfigInstance = BatchCreationConfig.get(params.id)
                def mailDate = new Date()
                def useMaxPieces = false

                if (batchCreationConfigInstance) {
                    mailDate = mailDate + batchCreationConfigInstance.mailDateDaysShift
                    if (batchCreationConfigInstance.maxPieces > 0) {
                        useMaxPieces = true
                    }
                }
                [batchCreationConfigInstance:batchCreationConfigInstance,
                    mailDate:mailDate, useMaxPieces:useMaxPieces]
            }
            on("success").to "showConfig"
            on("error").to "loadRecentBatches"
        }
        showConfig {
            on("return").to "loadRecentBatches"
            on("manualGenerateAction").to "manualGenerateAction"
            on("autoGenerate").to "autoGenerate"
            on("optionalGenerate").to "optionalGenerate"
            on("reGenerate").to "printDetails"
            on("batchReport").to "showBatchReport"
        }
        manualGenerateAction{
            action {
                def nBatchCreationQueue = BatchCreationQueue.count()
                if (nBatchCreationQueue > 0) {
                    BatchCreationQueue.executeUpdate("delete BatchCreationQueue")
                }
            }
            on("success").to "manualGenerate"
            on("return").to "showConfig"
        }

        manualGenerate {
            println "params in manualGenerate --> ${params}"
            
            on("return").to "showConfig"
            on("error").to "errorGeneratingBatch"
        }
        optionalGenerate{
            on("return").to "showConfig"
        }
        autoGenerate{
            action {
                def batchInstance = null
                def batchCreationConfigInstance = BatchCreationConfig.get(params.id)
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
                }
                [batchCreationConfigInstance:batchCreationConfigInstance,
                    batchInstance:batchInstance]
            }
            on("success").to "printDetails"
            on("error").to "errorGeneratingBatch"
        }
        errorGeneratingBatch()
        printDetails{
            // TODO:
            //   Open Batch Report if set to true
            //   Display link to D/L merge data, and show where to save it
            //   Display link to merge documents

            on("report").to "batchReport"
            on("return").to "showConfig"
        }
        showBatchReport{
            redirect(controller:"documentGeneration",action:'batchReport', id:params.batch?.id)
        }
    }
}
