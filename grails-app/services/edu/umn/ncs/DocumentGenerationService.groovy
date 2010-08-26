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
		def now = new Date()
		// the master batch for the run
        Batch masterBatch = null
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
			// selection results
			def results

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

			println "creating batch of ${batchCreationConfigInstance.instrument.name}..."

            masterBatch = new Batch(batchRunBy:username,
                format:batchCreationConfigInstance.format,
                direction:batchCreationConfigInstance.direction,
                instrumentDate:now,
                instrument:batchCreationConfigInstance.instrument,
                batchRunByWhat: 'ncs-case-management',
                trackingDocumentSent:batchCreationConfigInstance.generateTrackingDocument,
                creationConfig:batchCreationConfigInstance)

            masterBatch.addToInstruments(isPrimary: true,
                isResend: batchCreationConfigInstance.isResend,
                instrument:batchCreationConfigInstance.instrument,
                isInitial:batchCreationConfigInstance.isInitial)

            // Find attachments, and add them to the batch
            // batchCreationConfigInstance.subItems.find{ it.attachmentOf.id == batchCreationConfigInstance.instrument.id }
            batchCreationConfigInstance.subItems
			.find{ it.attachmentOf == batchCreationConfigInstance.instrument }
			.each{ attachment ->

				println "adding attachment ${attachment.instrument.name}..."

                masterBatch.addToInstruments(isPrimary: false,
                    instrument:attachment.instrument,
                    isResend: batchCreationConfigInstance.isResend,
                    isInitial:batchCreationConfigInstance.isInitial)

            }

			// save batches
			if (! masterBatch.save()) {
				println "ERRORS:"
				masterBatch.errors.each{ err ->
					println "ERROR>> ${err}"
				}
			} else {
				println "subBatch saved!"
			}


            batchCreationConfigInstance.addToBatches(masterBatch)

			batchList.add(masterBatch)

            //   Create Child and Sister items of Primary Batch
            batchCreationConfigInstance.subItems
			.find{ it.childOf != null || it.sisterOf != null }
			.each{ inst ->

				println "creating sub-batch ${inst.instrument.name}..."

				// Create Batch, assigning master
				def subBatch = new Batch(batchRunBy:username,
					master: masterBatch,
					format:inst.format,
					direction:inst.direction,
					instrumentDate:now,
					instrument:inst.instrument,
					batchRunByWhat: 'ncs-case-management',
					trackingDocumentSent:batchCreationConfigInstance.generateTrackingDocument,
					creationConfig:batchCreationConfigInstance)

				// Add attachments to batch
				batchCreationConfigInstance.subItems
				.find{ it.attachmentOf == inst.instrument }
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
					println "ERRORS:"
					subBatch.errors.each{ err ->
						println "ERROR>> ${err}"
					}
				} else {
					println "subBatch saved!"
				}

				// add batch to list
				batchList.add(subBatch)
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

			// loop through created batches and mark the childOf batches
			batchList.each{
				it.refresh()
				println "created batch # ${it.id}"
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

					if (bcq.validate()) {
						batchList.each{ b ->
							def trackedItem = new TrackedItem(person:bcq.person,
								household:bcq.household,
								dwellingUnit:bcq.dwellingUnit)

							// TODO: Add StudyYear
							//       Add Expiration
							//		 Add Parent

							b.addToItems(trackedItem)
						}
					}
				}
			} else {
				println "Error: no results returned!"
				emptySelectionList = true
			}

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
        return masterBatch
    }

    def generateMergeData() {

    }
	
    def getDocumentLocation() {

    }

}
