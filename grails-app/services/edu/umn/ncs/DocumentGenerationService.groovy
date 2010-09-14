package edu.umn.ncs
import javax.sql.DataSource
import groovy.sql.Sql

/* This class is based on the C# class written by Aaron J. Zirbes @ umn.edu
 * located here:
 *
 *   https://svn.cccs.umn.edu/svn/VisualStudio/2008.CS.NET/_general/DocumentGeneration/Objects/clsDocGen.cs
 *
 * NOTE, the aformationed code is not open source, but this link is left in here
 * for historical reference */
class DocumentGenerationService {

    static transactional = true
    DataSource dataSource

    def appName = 'ncs-case-management'

    // Loads an existing mailing into the print queue
    def reQueueMailing(Batch batchInstance, String username) {

        if (batchInstance  && username) {
            // find the root of the batch run (master batch)
            while (batchInstance.master) {
                batchInstance = batchInstance.master
            }

            // setup a print queue if the user doesn't have one
            def printQueue = BatchQueue.findByUsername(params.username)
            if (!printQueue) {
                printQueue = new BatchQueue(username:params.username, appCreated: appName).save(flush:true)
            }
            // clear print queue
            printQueue.items = []
            printQueue.save(flush:true)


            batchInstance.items.each{
                printQueue.addToItems(it)
            }

            subBatches = Batch.findAllByMaster(batchInstance).each{ subBatch ->
                subBatch.items.each{
                    printQueue.addToItems(it)
                }
            }

            printQueue.save(flush:true)
            
        }
        return batchInstance
    }

    def generateMailing(Map params) {

        def validSelectionList = true
        def emptySelectionList = false
        def now = new Date()
        // the master batch for the run
        Batch masterBatch = null
        /*
         * Params should contain...
         *	config		: BatchCreationConfig
         *	username        : String
         *	mailDate	: Date
         *	maxPieces	: Integer
         *	manual		: Boolean
         *	reason		: String
         *	instructions    : String
         *	comments	: String
         *
         **/

        //println "${params}"

        // We'll use these later...
        def attachmentOf = BatchCreationItemRelation.findByName('attachment')
        def childOf = BatchCreationItemRelation.findByName('child')
        def sisterOf = BatchCreationItemRelation.findByName('sister')


        if (params.config && params.username) {


            // setup a print queue if the user doesn't have one
            def printQueue = BatchQueue.findByUsername(params.username)
            if (!printQueue) {
                printQueue = new BatchQueue(username:params.username, appCreated: appName).save(flush:true)
            }
            // clear print queue
            printQueue.items = []
            printQueue.save(flush:true)


            def batchCreationConfigInstance = params.config
            def username = params.username
            // selection results
            def results

            println "${username} is generating a new ${batchCreationConfigInstance.name} batch"
            println "BatchCreationConfig instrument:  ${batchCreationConfigInstance.instrument.name} "

            def manualSelection = false
            def automaticSelection = false

            if (batchCreationConfigInstance.manualSelection && params.manual) {
                manualSelection = true
                automaticSelection = false
            } else if (batchCreationConfigInstance.automaticSelection && !params.manual) {
                manualSelection = false
                automaticSelection = true
            }

            if (!dataSource) {
                println "Error: connecting to dataSource in DocumentGenerationService"
            } else {
                
                if (manualSelection) {
                    def sql = new Sql(dataSource)
                    if (sql) {

                        def selectionQuery = """SELECT dwelling_unit_id AS dwelling_unit,
                                            household_id AS household,
                                            person_id AS person,
                                            expire_date,
                                            parent_item_id AS parent_item,
                                            study_year
                                            FROM batch_creation_queue
                                            WHERE (username = ?)"""
                        results = sql.rows(selectionQuery, [params.username])

                        // TODO:
                        // We can assume all of the columns are there
                        // because we'll put them in a table that will have
                        // the correct columns!

                        // ... we could do some null checks in the table though...
                        // The class is: BatchCreationQueue (table = batch_creation_queue)

                        // BatchCreationQueue needs to go into (results)
                    }
                } else {

                    def selectionQuery = batchCreationConfigInstance.selectionQuery
                    def selectionParams = [:]
                    // Replace :mailDate with actual mail date
                    if (selectionQuery.contains(':mailDate')) {
                        selectionParams.mailDate = params.mailDate
                    }

                    // Replace SELECT TOP N or LIMIT 0, N with MaxPeices
                    if (selectionQuery.contains(':topN')) {
                        selectionParams.topN = params.maxPieces
                    }
                    //      Note: Check Oracle and Postgres TOP N code

                    // If it's auto selection..
                    def sql = new Sql(dataSource)
                    if (sql) {
                        println "${new Date()}"
                        if (selectionParams) {
                            results = sql.rows(selectionQuery, selectionParams)
                        } else {
                            results = sql.rows(selectionQuery)
                        }
                        if (results) {
                            // validating recordset
                            results.each{ row ->
                                def bcq = new BatchCreationQueue()

                                if (row.containsKey('person')) {
                                    bcq.person = Person.get(row.person)
                                }
                                if (row.containsKey('household')) {
                                    bcq.household = Household.get(row.household)
                                }
                                if (row.containsKey('dwelling_unit')) {
                                    bcq.dwellingUnit = DwellingUnit.get(row.dwelling_unit)
                                }
                                bcq.username = username

                                if (!bcq.validate()) {
                                    // invalid selection list!
                                    println "invalid selection list row: ${row}"
                                    validSelectionList = false
                                }
                            }
                        } else {
                            println "Error: no results returned!"
                            emptySelectionList = true
                        }
                    } else {
                        println "Error: running selectionQuery!"
                        validSelectionList = false
                    }
                }

                if (validSelectionList && !emptySelectionList) {

                    def batchInfoList = []

                    ///////////////////////////////////////////////////////
                    // Create Primary Batch
                    ///////////////////////////////////////////////////////

                    println "creating batch of ${batchCreationConfigInstance.instrument.name}..."

                    // Create Basic Batch Info
                    masterBatch = new Batch(batchRunBy:username,
                        format:batchCreationConfigInstance.format,
                        direction:batchCreationConfigInstance.direction,
                        instrumentDate:now,
                        batchRunByWhat: appName,
                        trackingDocumentSent:batchCreationConfigInstance.generateTrackingDocument,
                        creationConfig:batchCreationConfigInstance)

                    // Assign Primary Instrment Type to Batch
                    masterBatch.addToInstruments(isPrimary: true,
                        isResend: batchCreationConfigInstance.isResend,
                        instrument:batchCreationConfigInstance.instrument,
                        isInitial:batchCreationConfigInstance.isInitial)

                    // Find attachments, and add them to the batch
                    batchCreationConfigInstance.subItems

                    .findAll{ it.attachmentOf == batchCreationConfigInstance.instrument }
                    .each{ attachment ->

                        println "adding attachment ${attachment.instrument.name}..."

                        masterBatch.addToInstruments(isPrimary: false,
                            instrument:attachment.instrument,
                            isResend: batchCreationConfigInstance.isResend,
                            isInitial:batchCreationConfigInstance.isInitial)

                    }

                    // save batch
                    if (! masterBatch.save()) {
                        println "ERRORS:"
                        masterBatch.errors.each{ err ->
                            println "ERROR>> ${err}"
                        }
                    } else {
                        println "masterBatch saved!"
                    }

                    // save info about the batch so we can create parent item
                    // relationships later
                    batchInfoList.add(
                        [batch:masterBatch,
                            instrument:masterBatch.primaryInstrument,
                            childOfInstrument:null,
                            childOfBatch:null,
                            master:true,
                            sortOrder:1])

                    ///////////////////////////////////////////////////////
                    //   Create Child and Sister items of Primary Batch
                    ///////////////////////////////////////////////////////
                    batchCreationConfigInstance.subItems
                    .findAll{ it.relation != attachmentOf }
                    .each{ bci ->

                        println "creating sub-batch ${bci.instrument.name}..."

                        // Create Sub Batch, assigning master
                        def subBatch = new Batch(batchRunBy:username,
                            master: masterBatch,
                            format:bci.format,
                            direction:bci.direction,
                            instrumentDate:now,
                            batchRunByWhat: appName,
                            trackingDocumentSent:batchCreationConfigInstance.generateTrackingDocument,
                            creationConfig:batchCreationConfigInstance)

                        // Assign Primary Instrument type to sub-batch
                        subBatch.addToInstruments(isPrimary: true,
                            instrument:bci.instrument,
                            isResend: batchCreationConfigInstance.isResend,
                            isInitial:batchCreationConfigInstance.isInitial)


                        // Add attachments to sub-batch
                        batchCreationConfigInstance.subItems
                        .findAll{ it.attachmentOf == bci.instrument }
                        .each{ attachment ->

                            println "adding attachment ${attachment.instrument.name}..."

                            subBatch.addToInstruments(isPrimary: false,
                                instrument:attachment.instrument,
                                isResend: batchCreationConfigInstance.isResend,
                                isInitial:batchCreationConfigInstance.isInitial)

                        }

                        //batchCreationConfigInstance.addToBatches(subBatch)

                        // save batches
                        if (! subBatch.save()) {
                            println "\n  Failed to save batch!  \n"
                            println "ERRORS:"
                            subBatch.errors.each{ err ->
                                println "ERROR>> ${err}"
                            }
                        } else {
                            println "subBatch saved!"
                        }

                        batchInfoList.add([batch:subBatch,
                                instrument:bci.instrument,
                                childOfInstrument:bci.childOf,
                                childOfBatch:null,
                                master:false,
                                sortOrder: 2])
                    }

                    // save batches
                    if (! batchCreationConfigInstance.save()) {
                        println "ERRORS:"
                        batchCreationConfigInstance.errors.each{ err ->
                            println "ERROR>> ${err}"
                        }
                    } else {
                        println "batchCreationConfigInstance and batches saved!"
                    }

                    // fill the batchInfoList.childOfBatch fields
                    // set the ordering based on parent-child relationships

                    // set the "yes we should re-order" flag
                    def reOrder = true
                    while (reOrder) {
                        // disable "yes we should re-order"
                        reOrder = false

                        // for each batch generated, look at the info
                        batchInfoList.each{ bil ->
                            // if the batch is the child of something
                            if (bil.childOfInstrument) {
                                // save the reference to the child batch in the table
                                bil.childOfBatch = batchInfoList.find{it.instrument.id == bil.childOfInstrument.id}?.batch
                                // see what the creation order of the parent batch is

                                def parentOrder = batchInfoList.find{
                                    it.instrument.id == bil.childOfInstrument.id
                                }?.sortOrder

                                println "My Order: ${bil.sortOrder} , my parent's order: ${parentOrder}"
                                // if the parent batch is slated to be created after
                                // this one (or presumably at the same time), change
                                // this order of this batch to ensure we don't get
                                // created until after the parent item is created
                                if (parentOrder && parentOrder >= bil.sortOrder) {

                                    bil.sortOrder = parentOrder + 1
                                    // because we tweaked the order settings, we
                                    // should reset the "yes we should re-order" flag
                                    // just to make sure that nothing was missed
                                    // due to a later step undo-ing an earlier step
                                    reOrder = true
                                }
                            }
                        }
                    }

                    // Uncomment this if you wish to see the batchInfoList in order
                    //batchInfoList.sort{it.sortOrder}.each{ bil ->
                    //    println "ngp debug; sortOrder: ${bil.sortOrder} "
                    //    println "       childOfBatch: ${bil.childOfBatch} "
                    //    println "       childOfInstrument: ${bil.childOfInstrument} "
                    //    println "       instrument: ${bil.instrument} "
                    //    println "       batch: ${bil.batch} "
                    //}


                    if (results) {
                        // validating recordset
                        results.each{ row ->

                        def bcq = new BatchCreationQueue()

                            if (row.containsKey('person')) {
                                bcq.person = Person.get(row.person)
                            }
                            if (row.containsKey('household')) {
                                bcq.household = Household.get(row.household)
                            }
                            if (row.containsKey('dwelling_unit')) {
                                bcq.dwellingUnit = DwellingUnit.get(row.dwelling_unit)
                            }
                            bcq.username = username


                            def trackedItemList = []

                            if (bcq.validate()) {
                                // VERY IMPORTANT to sort this so the dependent batches show up first!!!
                                // Should be sorted by childOfBatch

                                batchInfoList.sort{
                                    it.sortList
                                }

                                batchInfoList.sort{it.sortOrder}.each{ b ->

                                    def trackedItem = new TrackedItem(person:bcq.person,
                                        household:bcq.household,
                                        dwellingUnit:bcq.dwellingUnit)

                                    // add sister batches as well as master batches,
                                    // because they both become child of parent items
                                    // if doc gen is configured as such...
                                    if (!b.childOfBatch && batchCreationConfigInstance.useParentItem && row.containsKey('parent_item')) {
                                        def parent = TrackedItem.get(row.parent_item)
                                        if (parent) {
                                            trackedItem.parentItem = parent
                                        } else {
                                            println "WARNING: Parent Item ID: ${row.parent_item} not found!"
                                        }
                                    } else if (! b.master && b.childOfBatch) {
                                        // This sid is the child of another sid in the same run!
                                        def parent = trackedItemList.find{ it.batch == b.childOfBatch }
                                        if (parent) {
                                            trackedItem.parentItem = parent
                                        } else {
                                            println "ERROR: Something went horribly wrong! (We couldn't find the parent tracked item)"
                                        }

                                    }

                                    if (batchCreationConfigInstance.useExpiration && row.containsKey('expire_date')) {
                                        def expireDate = row.expire_date
                                        if (expireDate) {
                                            trackedItem.expiration = expireDate
                                        } else {
                                            println "NOTE: No expiration date found for item: ${trackedItem}!"
                                        }
                                    }

                                    if (batchCreationConfigInstance.useStudyYear && row.containsKey('study_year')) {
                                        def studyYear = row.study_year
                                        if (studyYear) {
                                            trackedItem.studyYear = studyYear
                                        } else {
                                            println "NOTE: No study year found for item: ${trackedItem}!"
                                        }
                                    }

                                    // TODO: If row.containsKey('calling_item') Then someService.putInCallSystem()

                                    b.batch.addToItems(trackedItem)
                                    printQueue.addToItems(trackedItem)
                                    // Removed this in leu of a faster version below
                                    // that writes the sids to the DB in one fell swoop
                                    //b.batch.save(flush:true)
                                    //trackedItem.save(flush:true)
                                    //println " + Created SID: ${trackedItem.id} (v2)"

                                    trackedItemList.add(trackedItem)
                                }
                            }
                        }

                        batchInfoList.sort{it.sortOrder}.each{ b ->
                            b.batch.save(flush:true)
                            printQueue.save(flush:true)
                            b.batch.items.each{
                                println " + Created SID: ${it.id} (v3)"
                            }
                        }


                    } else {
                        println "Error: no results returned!"
                        emptySelectionList = true
                    }

                    // Run Post-Generation SQL
                    if (batchCreationConfigInstance.postGenerationQuery) {
                        def sql = new Sql(dataSource)
                        if (sql) {
                            try {
                                sql.execute(batchCreationConfigInstance.postGenerationQuery)
                            } catch (Exception ex) {
                                // TODO: catch error and report to someone who can fix it!
                                println "Invalid Post-Generation Query (or error somwhere along those lines...)"
                                println "The query was: ${batchCreationConfigInstance.postGenerationQuery}"
                            }
                        }
                    }
                } //  if (validSelectionList && !emptySelectionList)
            } // if (dataSource)
        }

        return masterBatch
    }

    def generateMergeData(BatchCreationDocument batchCreationDocumentInstance) {
        // This needs to generate the data source based on the merge_source_query
        // in the BatchCreationConfig.document object
        if (batchCreationDocumentInstance.mergeSourceQuery) {
            // run the query, save the data to the "merge data location" if possible?

            // print out a list of?

        }
    }
	
    def getDocumentLocation(BatchCreationConfig batchCreationConfigInstance) {
        // Some time in the future, this could return a URL reference
        // to something like a controller or the like that will create
        // and merge the documents into their final form automatically
        // for the user.

        return batchCreationConfigInstance.documents.collect{ it.documentLocation }
    }

}
