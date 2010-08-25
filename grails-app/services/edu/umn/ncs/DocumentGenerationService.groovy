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

    def grailsApplication

    def generateMailing(Map params) {

        def validSelectionList = true
        def emptySelectionList = false
        def appName = grailsApplication?.config?.appName
        Batch primaryBatch = null
        /*
         *
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


        if (params.config && params.username) {

            def batchCreationConfigInstance = params.config
            def username = params.username

            println "${username} is generating a new ${batchCreationConfigInstance.name} batch"
            println "BatchCreationConfig instrument:  ${batchCreationConfigInstance.instrument.name} "

            println "params in service: ${params}"


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
                // TODO: Verify Selection Criteria Columns
                if (manualSelection) {
                    // We can assume all of the columns are there
                    // because we'll put them in a table that will have
                    // the correct columns!

                    // ... we could do some null checks in the table though...

                } else {

                    def selectionQuery = batchCreationConfigInstance.selectionQuery
                    def selectionParams = [:]
                    // TODO: Replace SQL Variables with Data

                    if (selectionQuery.contains(':mailDate')) {
                        selectionParams.mailDate = mailDate
                    }

                    // TODO: Replace SELECT TOP N or LIMIT 0, N with MaxPeices

                    if (selectionQuery.contains(':topN')) {
                        selectionParams.topN = maxPieces
                    }
                    //      Note: Check Oracle and Postgres TOP N code

                    // If it's auto selection..
                    def sql = new Sql(dataSource)
                    if (sql) {
                        println "${new Date()}"
                        def results
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
            }

            def batchList = []
            def batchChildOf = []

            // Create Primary Batch
            //def batchInstance = new Batch(all:the, batch:parameters, that:we, need:'.')

            def batchInstance = new Batch(batchRunBy:username,
                format:batchCreationConfigInstance.format,
                direction:batchCreationConfigInstance.direction,
                instrumentDate:new Date(),
                instrument:batchCreationConfigInstance.instrument,
                batchRunByWhat: 'ncs-case-management',
                trackingDocumentSent:false,
                creationConfig:batchCreationConfigInstance)

            batchInstance.addToInstruments(isPrimary: true,
                isResend: batchCreationConfigInstance.isResend,
                instrument:batchCreationConfigInstance.instrument,
                isInitial:batchCreationConfigInstance.isInitial)

            // Find attachments, and add them to the batch
            // batchCreationConfigInstance.subItems.find{ it.attachmentOf.id == batchCreationConfigInstance.instrument.id }
            batchCreationConfigInstance.subItems
                    .find{ it.attachmentOf == batchCreationConfigInstance.instrument }
                    .each{ attachment ->

                batchInstance.addToInstruments(isPrimary: false,
                    instrument:attachment.instrument,
                    isResend: batchCreationConfigInstance.isResend,
                    isInitial:batchCreationConfigInstance.isInitial)

            }

            batchCreationConfigInstance.addToBatches(batchInstance)

            if (! batchCreationConfigInstance.save()) {
                println "ERRORS:"
                batchCreationConfigInstance.errors.each{ err ->
                    println "ERROR>> ${err}"
                }
            } else {
                println "batchCreationConfigInstance and batchInstance saved!"
            }

            primaryBatch = batchInstance

/*

            //   Create Attachments of Primary Batch

            // WARNING! Pseudo-code...
            batchCreationConfigInstance.items.find{ attachmentOf == batchInstance.Instrument }.each{ bci ->
                batchInstance.addToInstruments(new BatchInstrument(stuff:here))
            }

            batchList.add(batchInstance)
            batchChildOf.add(null)

            // Create Sister and Child Batches
            batchCreationConfigInstance.items.find{ attachmentOf == null }.each{ bci ->
                def subBatchInstance = new Batch(all:the, batch:parameters, that:we, need:'.')
                
                //   Create Attachments of Sister and Child Batches
                // WARNING! Pseudo-code...
                batchCreationConfigInstance.items.find{ attachmentOf == subBatchInstance.Instrument }.each{ bci ->
                    subBatchInstance.addToInstruments(new BatchInstrument(stuff:here))
                }

                //   Note Child's Parent Batch.id
                batchList.add(subBatchInstance)
                batchChildOf.add(bci.childOf)

            }

*/




    
            // Create TrackedItems (sid)
            //   If Primary Item
            //     ParentItem = parent_item from recordset
            //   Else
            //     ParentItem = id Of Parent in current batch
            //   ...if OCS, put in call system (later...)

            // Run Post-Generation SQL

            // Done!
            // TODO: for controller
            //   Open Batch Report if set to true
            //   Display link to D/L merge data, and show where to save it
            //   Display link to merge documents

        }
        return primaryBatch
    }

    def generateMergeData() {

    }
	
    def getDocumentLocation() {

    }

}
