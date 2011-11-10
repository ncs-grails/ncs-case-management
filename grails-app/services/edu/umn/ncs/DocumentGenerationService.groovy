package edu.umn.ncs

import groovy.sql.Sql
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

/* This class is based on the C# class written by Aaron J. Zirbes @ umn.edu
 * located here:
 *
 *   https://svn.cccs.umn.edu/svn/VisualStudio/2008.CS.NET/_general/DocumentGeneration/Objects/clsDocGen.cs
 *
 * NOTE, the afformentioned code is not open source, but this link is left in here
 * for historical reference */
class DocumentGenerationService {

    static transactional = true
    def dataSource
    def mergeDataBuilderService

    def appName = 'ncs-case-management'
	
	static def debug = false
	
	static def csvDateFormat = 'yyyy-MM-dd HH:mm:ss'
    
	/** This function returns the child of a tracked item that matches
	the instrument type passed. */
	def getItemByParentAndConfig(TrackedItem trackedItemInstance, Instrument instrumentInstance) {
		
		def parentTrackedItemInstance = TrackedItem.createCriteria().get{
			and {
				parentItem {
					idEq(trackedItemInstance.id)
				}
				batch {
					instruments {
						instrument {
							idEq(instrumentInstance.id)
						}
					}
				}
			}
			maxResults(1)
		}	
		
		return parentTrackedItemInstance
	}
	
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

	/** This helper method loads a manual selection dataset and returns it for DocGen to run through */
	private loadManualSelectionDataSet(BatchCreationConfig batchCreationConfigInstance, String username) {
		def results = []
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
		}
		return results
	}

	/** This helper method loads a manual selection dataset and returns it for DocGen to run through */
	private loadAutomaticSelectionDataSet(BatchCreationConfig batchCreationConfigInstance, params) {
		def results = []
		def selectionQuery = batchCreationConfigInstance.selectionQuery
		def selectionParams = [:]
		def username = params.username
		// Replace :mailDate with actual mail date
		if (selectionQuery.contains(':mailDate')) {
			selectionParams.mailDate = params.mailDate
		}

		// Replace SELECT TOP N or LIMIT 0, N with MaxPeices
		if (selectionQuery.contains(':topN')) {
			selectionParams.topN = params.maxPieces
		}

		if (debug) {println "batchCreationConfigInstance.selectionQuery --> ${batchCreationConfigInstance.selectionQuery}"}

		//      Note: Check Oracle and Postgres TOP N code

		// If it's auto selection..
		def sql = new Sql(dataSource)
		if (sql) {
			
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
						bcq.person = Person.read(row.person)
						if (debug) { println "Person found: ${bcq.person}" }
					} else if (row.containsKey('person_id')) {
						bcq.person = Person.read(row.person_id)
						if (debug) { println "Person.id found: ${bcq.person}" }
					}
					if (row.containsKey('household')) {
						bcq.household = Household.read(row.household)
					} else if (row.containsKey('household_id')) {
						bcq.household = Household.read(row.household)
					}
					if (row.containsKey('dwelling_unit')) {
						bcq.dwellingUnit = DwellingUnit.read(row.dwelling_unit)
					} else if (row.containsKey('dwelling_unit_id')) {
						bcq.dwellingUnit = DwellingUnit.read(row.dwelling_unit_id)
					}
					bcq.username = username

					if (!bcq.validate()) {
						// invalid selection list!
						println "invalid selection list row: ${row}, expected [person, household, or dwelling_unit]"
						//throw new DocumentGenerationException("invalid selection list row: ${row}, expected [person, household, or dwelling_unit]")
						throw InvalidSelectionListException
					} else {
						if (debug) { println "valid record found..." }
					}
				}
			}
		} else {
			println "Error: running selectionQuery!"
			throw DocumentGenerationCannotConnectToDatasourceException
		}

		return results
	}

	/** This helper function generates all the batches for a configuration and 
	returns a batchInfoList collection of maps to aid in generation of tracked items */
	private generateBatches(BatchCreationConfig batchCreationConfigInstance, params) {
		def username = params.username
		def batchInfoList = []
		def masterBatch = null

		///////////////////////////////////////////////////////
		// Create Primary Batch
		///////////////////////////////////////////////////////

		println "creating batch of ${batchCreationConfigInstance.instrument.name}..."

		// Create Basic Batch Info
		masterBatch = new Batch(batchRunBy:username,
			format:batchCreationConfigInstance.format,
			direction:batchCreationConfigInstance.direction,
			instrumentDate: params.instrumentDate,
			mailDate: params.mailDate,
			batchRunByWhat: appName,
			trackingDocumentSent:batchCreationConfigInstance.generateTrackingDocument,
			creationConfig:batchCreationConfigInstance)


		// Pull a version from the version history table if possible
		def instrumentHistoryInstance = InstrumentHistory.createCriteria().get{
			and{
				lt ("startDate", now)
				instrument{
					eq ("id", batchCreationConfigInstance.instrument.id)
				}
				or{
					gt ("endDate", now)
					isNull("endDate")
				}
			}
			maxResults(1)
		}

		def v = instrumentHistoryInstance.itemVersion ?: 1

		// Assign Primary Instrment Type to Batch
		masterBatch.addToInstruments(isPrimary: true,
			isResend: batchCreationConfigInstance.isResend,
			itemVersion: v,
			instrument:batchCreationConfigInstance.instrument,
			isInitial:batchCreationConfigInstance.isInitial)

		// Find attachments, and add them to the batch
		batchCreationConfigInstance.subItems
			.findAll{ it.attachmentOf == batchCreationConfigInstance.instrument }
			.each{ attachment ->

			//println "adding attachment ${attachment.instrument.name}..."

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
		}

		// save info about the batch so we can create parent item
		// relationships later
		batchInfoList.add(
			[batch:masterBatch,
				instrument:masterBatch.primaryInstrument,
				childOfInstrument:null,
				childOfBatch:null,
				optional: false,
				master:true,
				sortOrder:1])

		///////////////////////////////////////////////////////
		//   Create Child and Sister batches of Primary Batch
		///////////////////////////////////////////////////////
		batchCreationConfigInstance.subItems
			.findAll{ it.relation != attachmentOf }
			.each{ bci ->

			//println "creating sub-batch ${bci.instrument.name}..."

			// Create Sub Batch, assigning master
			def subBatch = new Batch(batchRunBy:username,
				master: masterBatch,
				format:bci.format,
				direction:bci.direction,
				instrumentDate: params.instrumentDate,
				mailDate: params.mailDate,
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
				.findAll{ it?.attachmentOf?.id == bci?.instrument?.id }
				.each{ attachment ->

				subBatch.addToInstruments(isPrimary: false,
					instrument:attachment.instrument,
					isResend: batchCreationConfigInstance.isResend,
					isInitial:batchCreationConfigInstance.isInitial)

			}

			// save batches
			if (! subBatch.save()) {
				println "\n  Failed to save batch!  \n"
				println "ERRORS:"
				subBatch.errors.each{ err ->
					println "ERROR>> ${err}"
				}
			}

			batchInfoList.add([batch:subBatch,
					instrument:bci.instrument,
					optional: bci.optional,
					childOfInstrument:bci.childOf,
					childOfBatch:null,
					master:false,
					sortOrder: 2])
		}

		// save batches as the batch is a member of the 'batches' collection in a batchCreationConfigInstance

		if (! batchCreationConfigInstance.save()) {
			println "ERRORS:"
			batchCreationConfigInstance.errors.each{ err ->
				println "ERROR>> ${err}"
			}
		} 

		return sortBatchInfoList(batchInfoList)
	}

	/** This helper function sorts the batchInfoList */
	private sortBatchInfoList(batchInfoList) {
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

					// println "My Order: ${bil.sortOrder} , my parent's order: ${parentOrder}"

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

		if (debug) {
			batchInfoList.sort{it.sortOrder}.each{ bil ->
				println "ngp debug; sortOrder: ${bil.sortOrder} "
				println "       childOfBatch: ${bil.childOfBatch} "
				println "       childOfInstrument: ${bil.childOfInstrument} "
				println "       instrument: ${bil.instrument} "
				println "       batch: ${bil.batch} "
			}
		}

		return batchInfoList
	}

	/** This generates all the tracked items for a set of batches */
	private generateTrackedItems(BatchCreationConfig batchCreationConfigInstance, batchInfoList, results) {
		// validating recordset
		results.each{ row ->

			def bcq = new BatchCreationQueue()

			if (row.containsKey('person')) {
				bcq.person = Person.read(row.person)
			} else if (row.containsKey('person_id')) {
				bcq.person = Person.read(row.person_id)
			}
			if (row.containsKey('household')) {
				bcq.household = Household.read(row.household)
			} else if (row.containsKey('household_id')) {
				bcq.household = Household.read(row.household)
			}
			if (row.containsKey('dwelling_unit')) {
				bcq.dwellingUnit = DwellingUnit.read(row.dwelling_unit)
			} else if (row.containsKey('dwelling_unit_id')) {
				bcq.dwellingUnit = DwellingUnit.read(row.dwelling_unit_id)
			}
			bcq.username = username


			def trackedItemList = []

			if (bcq.validate()) {
				// VERY IMPORTANT to sort this so the dependent batches show up first!!!
				// Should be sorted by childOfBatch

				// sort it according to parent instrument dependency
				batchInfoList = batchInfoList.sort{ it.sortOrder }

				batchInfoList.each{ b ->

					def generateItem = true

					// This is the magical branching logic that we needed!
					// if the instrument is optional...
					if (b.optional) {
						def skipColumn = "skip_${b.instrument.nickName}"
						// ... and the "skip" column exists in the recordset ...
						if ( record.keySet.contains(skipColumn) ) {
							// ... and the skip column is not null ...
							if ( record[skipColumn] ) {
								// ... then DO NOT GENERATE THIS TRACKED ITEM!!!
								generateItem = false
							}
						}
					}

					if (generateItem) {
						// Create an "unattached tracked item"
						// Note: this may be discarded if the item is optional, and 
						// the selection criteria deems that it should not be generated
						def trackedItem = new TrackedItem(person:bcq.person,
							household:bcq.household,
							dwellingUnit:bcq.dwellingUnit)

						// consitency states that comments, of all types, are only added to the master
						// item in the batch
						if (b.master) {
							if (instructions) {
								trackedItem.addToComments(subject: 'instructions', text: instructions, userCreated: username, appCreated: appName)
							}
							if (comments) {
								trackedItem.addToComments(text: comments, userCreated: username, appCreated: appName)
							}
							if (reason) {
								trackedItem.addToComments(subject: 'reason', text: reason, userCreated: username, appCreated: appName)
							}                                        
						}

						// add sister batches as well as master batches,
						// because they both become child of parent items
						// if doc gen is configured as such...
						//   master/sister batch    && useParentItem                             && parent_item was passed
						if (!b.childOfBatch && batchCreationConfigInstance.useParentItem && row.containsKey('parent_item')) {
							def parent = TrackedItem.read(row.parent_item)
							if (parent) {
								trackedItem.parentItem = parent
							} else {
								println "WARNING: Parent Item ID: ${row.parent_item} not found!"
								//throw new DocumentGenerationException("Can't find parent tracked item for master batch.")
							}
						} else if (! b.master && b.childOfBatch) {
							// This sid is the child of another sid in the same run!
							def parent = trackedItemList.find{ it.batch == b.childOfBatch }
							if (parent) {
								trackedItem.parentItem = parent
							} else {
								println "ERROR: Something went horribly wrong! (We couldn't find the parent tracked item)"
								//throw new DocumentGenerationException("Can't find parent tracked item of sister/parent/master batch.")
							}

						} else if (!b.childOfBatch && batchCreationConfigInstance.useParentItem
							&& !row.containsKey('parent_item')
							&& batchCreationConfigInstance.parentInstrument  
							&& batchCreationConfigInstance.parentDirection  
							&& batchCreationConfigInstance.parentFormat  
							&& batchCreationConfigInstance.isInitial ) {

							// WARNING!
							// This method is slow as it creates one query per record, so
							// a mailing of 2000 will run 2000 queries (or more)

							// we should hunt for the parent sid
							def cti = TrackedItem.createCriteria()
							def trackedItemInstanceList = cti.list{
								batch{
									and {
										instruments{
											and {
												instrument{
													eq("id", batchCreationConfigInstance.parentInstrument?.id)
												}
												isInitial{
													eq("id", batchCreationConfigInstance.isInitial?.id)
												}
											}
										}
										direction{
											eq("id", batchCreationConfigInstance.parentDirection?.id)
										}
										format{
											eq("id", batchCreationConfigInstance.parentFormat?.id)
										}
									}
								}
							}

							if (trackedItemInstanceList) {
								// grab the first one.
								parent = trackedItemInstanceList[0]
							}

							if (parent) {
								trackedItem.parentItem = parent
							} else {
								println "WARNING: Parent Item ID not found for: ${row}"
								//throw new DocumentGenerationException("Can't find parent tracked item using implied information.")
							}
						} else if (!b.childOfBatch && batchCreationConfigInstance.useParentItem
							&& !row.containsKey('parent_item') ) {
							println "ERROR: Someone set the batch config to require a parent_item, but didn't specify the tracked_item.id AS parent_item"
							//throw new DocumentGenerationException("Can't find parent_item column in data source even though config requires it.")
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
					} else if (debug) {
						println "this item (${bcq}) skipped due to 'skip_${b.instrument.nickName}' clause."
					}
				}
			} else {
				println "Invalid Batch Creation Queue Record"
				throw InvalidBatchCreationQueueRecordException
			}
		}

		// save all the batches in their dependent order!
		batchInfoList.sort{it.sortOrder}.each{ b ->
			b.batch.save(flush:true)
			printQueue.save(flush:true)
		}
	}

	private void runPostGenerationLogic(BatchCreationConfig batchCreationConfigInstance, Batch masterBatch) {
		// Run Post-Generation SQL
		if (batchCreationConfigInstance.postGenerationQuery) {
			def sql = new Sql(dataSource)
			if (sql) {
				try {
					
					def pgsParams = [:]
					// Replace :mailDate with actual mail date
					if (batchCreationConfigInstance.postGenerationQuery.contains(':batchId')) {
						pgsParams.batchId = masterBatch.id
					}

					if (pgsParams) {
						
						sql.execute(batchCreationConfigInstance.postGenerationQuery, pgsParams)
					} else {
						sql.execute(batchCreationConfigInstance.postGenerationQuery)
					}
					
				} catch (Exception ex) {
					// TODO: catch error and report to someone who can fix it!
					// emailService.sendErrorReport(message, ip, username)
					println "Invalid Post-Generation Query (or error somwhere along those lines...)"
					println "The query was: ${batchCreationConfigInstance.postGenerationQuery}"
					throw InvalidPostGenerationQueryException
				}
			}
		}
	}

    def generateMailing(Map params) {

        def now = new Date()
        // the master batch for the run
        Batch masterBatch = null
        /*
         * Params should contain...
         *	config		: BatchCreationConfig
         *	username        : String
         *	mailDate	: Date
         *	instrumentDate  : Date
         *	maxPieces	: Integer
         *	manual		: Boolean
         *	reason		: String
         *	instructions    : String
         *	comments	: String
         *
         **/

        // We'll use these later...
        def attachmentOf = BatchCreationItemRelation.findByName('attachment')
        def childOf = BatchCreationItemRelation.findByName('child')
        def sisterOf = BatchCreationItemRelation.findByName('sister')
        
		// default reason, instructions and comments for each item
        def reason = null
        def instructions = null
        def comments = null       
        
		// if no mail date was passed, set the default
        if (!params.mailDate) {
            params.mailDate = null
        }

        if (params.config && params.username) {

            // setup a print queue if the user doesn't have one
            def printQueue = BatchQueue.findByUsername(params.username)
            if (!printQueue) {
                printQueue = new BatchQueue(username:params.username, appCreated: appName).save(flush:true)
            }
            // clear print queue
            printQueue.items = []
            printQueue.save(flush:true)


            def batchCreationConfigInstance = BatchCreationConfig.get(params.config.id)
            def username = params.username
            // selection results
            def results

            //println "${username} is generating a new ${batchCreationConfigInstance.name} batch"
            //println "BatchCreationConfig instrument:  ${batchCreationConfigInstance.instrument.name} "

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
                //throw new DocumentGenerationException("Where did the database server go?")
            } else {
                
                if (manualSelection) {
					// Load the "to be generated" dataset
					loadManualSelectionDataSet(batchCreationConfigInstance, username)
                } else {

					// default reason, instructions and comments come from config for auto generation.
                    if (!params.reason)  {
                        reason = batchCreationConfigInstance.defaultReason
                    }
                    if (!params.instructions)  {
                        instructions = batchCreationConfigInstance.defaultInstructions
                    }
                    if (!params.comments)  {
                        comments = batchCreationConfigInstance.defaultComments
                    }

					// Load the "to be generated" dataset
                    results = loadAutomaticSelectionDataSet(batchCreationConfigInstance, params)
                }

				if (results ) {
					// generate the batches, and get back
					// the batch metadata as a collection of maps
					batchInfoList = generateBatches(batchCreationConfigInstance, params)

					// get the master batch out of the batchInfoList collection
					masterBatch = batchInfoList.find{ it.master }

					// generate all the tracked items per the selection criteria and tie
					// them to the batches that we created.
					generateTrackedItems(batchCreationConfigInstance, batchInfoList, results)
					
					// run any post-generation code that needs to be run
					runPostGenerationLogic(batchCreationConfigInstance, masterBatch)
                } else {
					println "Nothing to generate!"
				} 
            }
        }

        return masterBatch
    }

	/** This creates a merge data source for a particular batch and document combination */
    def generateMergeData(Batch batchInstance, BatchCreationDocument batchCreationDocumentInstance) {
		// build a date/time format that is MS Word "safe"
		def fmt = DateTimeFormat.forPattern(csvDateFormat)
		
		def startTime = new GregorianCalendar().time.time
		def splitTime = startTime
		
		if (debug) {
			def newSplitTime = new GregorianCalendar().time.time
			println "Begin"
			println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
			splitTime = newSplitTime 
		}
		
        // the output recordset (list of maps)
        def outputData = mergeDataBuilderService.getBaseData(batchInstance)
		if (debug) {
			def newSplitTime = new GregorianCalendar().time.time
			println "getBaseData"
			println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
			splitTime = newSplitTime
		}

		// If the doc gen setup uses people, add the person dataset
		if (batchCreationDocumentInstance.batchCreationConfig.usePerson) {
			outputData = mergeDataBuilderService.addPersonData(outputData)
			
			if (debug) {
				def newSplitTime = new GregorianCalendar().time.time
				println "addPersonData"
				println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
				splitTime = newSplitTime
			}
		} else if (batchCreationDocumentInstance.batchCreationConfig.useHousehold) {
			// If the doc gen setup uses people, add the household dataset (currently the same as dwelling unit)
			outputData = mergeDataBuilderService.addDwellingUnitData(outputData)
			
			if (debug) {
				def newSplitTime = new GregorianCalendar().time.time
				println "addDwellingUnitData"
				println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
				splitTime = newSplitTime
			}
		} else if (batchCreationDocumentInstance.batchCreationConfig.useDwellingUnit) {
			// If the doc gen setup uses dwelling units, add the dwelling unit dataset
			outputData = mergeDataBuilderService.addDwellingUnitData(outputData)
			
			if (debug) {
				def newSplitTime = new GregorianCalendar().time.time
				println "addDwellingUnitData"
				println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
				splitTime = newSplitTime
			}
		}

		// TODO: replace this looping with calls
		// can process the 'closure' or 'sqlQuery' 
		// if it is provided.
		// for now, we'll provide a fallback to the 
		// MergeDataBuilderService if the prior
		// two don't exist.
        batchCreationDocumentInstance.dataSets.each{

			if (debug) {
				println "Dataset type: ${it.code}"
			}
            if (it.code == "norc") {
			// TODO: Move this to SQL DataSetType attributes
                outputData = mergeDataBuilderService.addNORCData(outputData)
				
				if (debug) {
					def newSplitTime = new GregorianCalendar().time.time
					println "addNORCData"
					println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
					splitTime = newSplitTime
				}
            } else if (it.code == "appointment") {
			// TODO: Move this to SQL DataSetType attributes
				outputData = mergeDataBuilderService.addAppointmentData(outputData)
				
				if (debug) {
					def newSplitTime = new GregorianCalendar().time.time
					println "addAppointmentData"
					println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
					splitTime = newSplitTime
				}
            } else if (it.code == "childItems") {
			// TODO: Move this to SQL DataSetType attributes
				outputData = mergeDataBuilderService.addChildItems(outputData)
				
				if (debug) {
					def newSplitTime = new GregorianCalendar().time.time
					println "addChildItems"
					println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
					splitTime = newSplitTime
				}
			} else {
				// chain custom dataset on from closure in DataSetType
				outputData = mergeDataBuilderService.addDataSet(it, outputData)
			}
        }
		
		// TODO: add custom sorting from the batchCreationDocumentInstance.sortOrder field
        outputData = outputData.sort{ it.itemId }
		
		if (debug) {
			def newSplitTime = new GregorianCalendar().time.time
			println "Sorting."
			println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
			splitTime = newSplitTime
		}

		// Add the "recNo" column to the dataset
        //adding pieces+tracking documents
        def recNo = 0
        outputData.each{
            recNo++
            it.recordNumber = recNo
            it.piecesPlusTracking = recNo
        }

		// 
		 def batchCreationConfig = ""
         if (outputData) {
			 // convert the data set to a CSV format
			 mergeSourceContents = dataSetToCsv(outputData)
		 }
		 if (debug) {
			 def newSplitTime = new GregorianCalendar().time.time
			 println "Done."
			 println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
			 splitTime = newSplitTime
		 }
 
        return mergeSourceContents
    }


	/** This converts a dataset (colleciton of maps) to
   		a string buffer containing the CSV version of
		the data */
	def dataSetToCsv(outputData) {

		// the stuff to write to the file
		def mergeSourceContents = new StringBuffer()
	
		// get a field list
		def firstRow = outputData[0]
		def columnNames = firstRow.collect{ it.key }

		// write the header column
		//  "ID","FirstName","MiddleName","LastName","Suffix"
		columnNames.eachWithIndex{ col, i ->
			if (i > 0) {
				mergeSourceContents << ","
			}
			mergeSourceContents << ("\"" + col.replace("\"", "\"\"") + "\"")
		}
		// Using \r\n for MS Windows
		mergeSourceContents << "\r\n"

		// write the data
		outputData.each{ row ->
			columnNames.eachWithIndex{ col, i ->

				if (i > 0) {
					mergeSourceContents << ","
				}
				// default content is empty
				def columnValue = ""
				// If there's a non-null value...
				if (row[col] != null) {
					// take the content and escape the double quotes (")
					
					
					
					def columnContent = row[col].toString().replace('"', '""')
					
					if (row[col].class.toString() == 'class java.sql.Timestamp') {
						// if (debug) { println "Row Class for ${col}: " + row[col].class.toString() }
						// This is a SQL Timestamp.  We're changing the date format so that MS Word can parse it
						// (it has trouble with milliseconds)
						def sqlDate = new DateTime(row[col].time)
						columnContent = fmt.print(sqlDate)
					}
					
					// then surround it with double quotes
					columnValue = '"' + columnContent  + '"'
				}

				mergeSourceContents << columnValue
			}
			mergeSourceContents << "\r\n"
		}
		return mergeSourceContents
	}
	
    def getDocumentLocation(BatchCreationDocument batchCreationDocumentInstance) {
        // Some time in the future, this could return a URL reference
        // to something like a controller or the like that will create
        // and merge the documents into their final merged form automatically
        // for the user.
		
		// TODO:
		// The process will involve three steps
		// 1.) creating an auto-merge script.  Some kind of .vbs script or the like.
		// 2.) creating the merge file
		// 3.) zipping up the previous two files with the merge document to be downloaded
		//     by the user, so when they open it they only see the "runthis.vbs" or
		//     similar that will auto-open and merge the document with the datasource.

        return batchCreationConfigInstance.documents.collect{ it.documentLocation }
    }

}
