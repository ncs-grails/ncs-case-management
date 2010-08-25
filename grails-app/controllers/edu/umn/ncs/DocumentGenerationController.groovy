package edu.umn.ncs

class DocumentGenerationController {
    javax.sql.DataSource dataSource

    def documentGenerationService

    def username = 'ajz'

    //
    // TODO: Show BatchQueue
    // TODO: Show/Add items per Person
    // TODO: Show/Add items per dwelling unit

    def index = { redirect(action:'generation', params:params) }
	
    // display a batch report
    def batchReport = {

        def batchInstance = Batch.get(params.id)
        if (!batchInstance) {
              batchInstance = Batch.get(params.batch?.id)
        }

        if (batchInstance) {
            println "Batch ID: ${batchInstance.id}"
        }
        [batchInstance:batchInstance]
    }

    def generationFlow = {
        // WARNING:  Any thing get gets passed as part of the model
        // needs to implement Serializable.  Otherwise everything comes
        // crumblig down.
        loadRecentBatches {
            action {

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
            on("manualGenerate").to "manualGenerate"
            on("autoGenerate").to "autoGenerate"
            on("optionalGenerate").to "optionalGenerate"
            on("reGenerate").to "printDetails"
            on("batchReport").to "showBatchReport"
        }
        manualGenerate{
            on("return").to "showConfig"
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
            on("report").to "batchReport"
            on("return").to "showConfig"
        }
        showBatchReport{
            redirect(controller:"documentGeneration",action:'batchReport', id:params.batch?.id)
        }
    }
}
