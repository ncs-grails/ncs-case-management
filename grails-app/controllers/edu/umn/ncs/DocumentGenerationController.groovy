package edu.umn.ncs

class DocumentGenerationController {
    javax.sql.DataSource dataSource

    def documentGenerationService

    def username = 'ajz'

        //
	// TODO: Show BatchQueue
	// TODO: Show/Add items per Person
	// TODO: Show/Add items per dwelling unit

    def index = { redirect(action:'list', params:params) }
	
    // find a mailing type
    def list = {

        def q = params.q
        // List of matching configs per search criteria

        def batchCreationConfigInstanceList = []
        // list of recently generated mailing types
        def batchCreationConfigRecentList = []

        def aboutSixMonthsAgo = (new Date()) - 180

        // find recently generated batch config types
        def criteria = BatchCreationConfig.createCriteria()

        batchCreationConfigRecentList = criteria.list{
            batches {
                eq('batchRunBy', username)
                gt('dateCreated', aboutSixMonthsAgo)
            }
        }

        //		order('dateCreated', 'desc')



        // if a query was passed, we'll run it
        if (q) {
            criteria = BatchCreationConfig.createCriteria()

            batchCreationConfigInstanceList = criteria.list{
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

    // show a mailing type
    def show = {
        // return instance of config
        def batchCreationConfigInstance = BatchCreationConfig.get(params.id)

        def mailDate = new Date()
        mailDate = mailDate + batchCreationConfigInstance.mailDateDaysShift

        def useMaxPieces = false

        if (batchCreationConfigInstance.maxPieces > 0) {
            useMaxPieces = true
        }

        // return list of recently run batches
        // TODO: this will need to be sorted (by date, descending)!!!
        //def batchInstanceList = Batch.findAllByCreationConfig(batchCreationConfigInstance)
        def batchInstanceList = Batch.list()

        [batchCreationConfigInstance:batchCreationConfigInstance,
            batchInstanceList:batchInstanceList,
            mailDate:mailDate, useMaxPieces:useMaxPieces]

    }

    // reprint a previously generated mailing
    def reGenerate = {}

    // generate a new mailing automatically
    def autoGenerate = {

        def batchCreationConfigInstance = BatchCreationConfig.get(params.id)
        def results = null

        def docGenParams = [manual:false, username:username]

        if (batchCreationConfigInstance) {
            docGenParams.config = batchCreationConfigInstance

            println "Generate the batch: ${batchCreationConfigInstance.name}"
            if (params.autoSetMailDate) {
                docGenParams.mailDate = params.mailDate
            }
            if (params.useMaxPieces == 'true') {
                docGenParams.maxPieces = params.maxPieces
            }

            documentGenerationService.generateMailing(docGenParams)

        }

        [batchCreationConfigInstance:batchCreationConfigInstance]
    }

    // manually generate a mailing
    def manualGenerate = {}

    // display a batch report
    def batchReport = {

		def batchInstance = Batch.get(params.batch?.id)

        if (batchInstance) {
            println "Batch ID: ${batchInstance.id}"
        }
		[batchInstance:batchInstance]
    }

}
