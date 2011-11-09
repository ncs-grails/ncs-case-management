package edu.umn.ncs

/**
This class will create a merge data source for a particular
batch that was created.  It will look up the document generation
configuration (bundle) that was associated with the batch
and figure out what data source details are needed in order
to complete the data set.  It appends columns
as each set is built.  New data set options need to be added
to this service.  There's probably a way to get this to 
load dataset options from a user defined query, but we haven't gotten
that far yet.
*/
class MergeDataBuilderService {

	/*
	 * 
	 * NOTE: when you add a new service method here, you'll need to:
	 *    add a new "else if" to DocumentGenerationService.addAppointmentData()
	 *    add a new DataSetType to the grails-app\conf\BootStrap.groovy code.
	 *    
	 */
	
    static transactional = true
	
	def debug = false

	/**
	This returns the minimum base data set required to do a mailing.
	It included batch generation dates, tracked items IDs, primary
	instrument information, parentItems, etc..
	*/
    def getBaseData(Batch batchInstance) {
        // this will be a list of map items.
        def dataSet = []
		
		def statistics = [:]
		

		if (debug) { println "MergeDataBuilderService:getBaseData:: called." }
		
		// create a cache
		def childItemCache = [:]
		
		// Restricting this to NORC Transmittals (at least for now...)
		if (batchInstance.primaryInstrument.id == 19) {
			// find all child items for this batch
			def childItems = TrackedItem.executeQuery("\
				select cti.id as childItemId, pti.id as trackedItemId \
					from TrackedItem as cti inner join \
					cti.parentItem as pti inner join \
					pti.batch as b \
				where b.id = ?", [batchInstance.id])

			childItems.each{ row ->
				def childItemId = row[0]
				def trackedItemId = row[1]
				childItemCache[trackedItemId] = childItemId 
			}
		}
		
        // loop through the items in the batch
        // Not sure if I mapped right: eventDesc, childSID
        // Did not map: documentTitle, instructions, reason, comments
        batchInstance.items.each{ item ->

            def record = [
                itemId: item.id,
                itemIdBarcode: "*I${item.id}*",
                batchId: batchInstance?.id,
                batchDate: batchInstance?.dateCreated,
                mailDate: batchInstance?.mailDate,
                instrumentDate: batchInstance?.instrumentDate,
                pieces: batchInstance?.pieces,
                instrumentId: batchInstance?.primaryInstrument?.id,
                instrumentName: batchInstance?.primaryInstrument?.name,
                studyId: batchInstance?.primaryInstrument?.study?.id,
                studyName: batchInstance?.primaryInstrument?.study?.name,
                documentTitle: ((item?.studyYear != null) ? "T" + item?.studyYear + ' ' : '') + batchInstance?.primaryInstrument?.study?.name + ' ' + batchInstance?.primaryInstrument?.name,
                fullStudyName: batchInstance?.primaryInstrument?.study?.fullName,
                isInitial: batchInstance?.primaryBatchInstrument?.isInitial,
                studyYear: item?.studyYear,
                expirationDate: item?.expiration,
                instrumentVersion: batchInstance?.primaryBatchInstrument?.itemVersion,
                parentItemId: item?.parentItem?.id,
                parentDate: item?.parentItem?.batch?.dateCreated,
                parentName: item?.parentItem?.batch?.primaryInstrument?.name,
				parentInstrumentId: item?.parentItem?.batch?.primaryInstrument?.id,
                childItemId: childItemCache[item.id],
                resultName: item?.result?.result?.name,
                resultDate: item?.result?.receivedDate,
				instructions: "",
				comments: "",
				reason: ""
            ]
			
			if (debug) { 
				println "MergeDataBuilderService:getBaseData::record.itemId = ${record.itemId}" 
			}

            if (item.dwellingUnit) {
                record.dwellingUnitId = item.dwellingUnit.id
            }
            if (item.household) {
                record.householdId = item.household.id
            }
            if (item.person) {
                record.personId = item.person.id
                record.fullName = item.person?.fullName
                record.title = item.person?.title
                record.firstName = item.person?.firstName
                record.lastName = item.person?.lastName
                record.gender = item.person?.gender?.name
				if (record.lastName == null) {
					record.salutation ="To whom it may concern"
				} else if (record.title == null && item.person?.gender?.id == 1) {
					record.salutation ="Dear Mr. ${record.lastName}"
				} else if (record.title == null && item.person?.gender?.id == 2) {
					record.salutation ="Dear Ms. ${record.lastName}"
				} else if (record.title == null) {
					record.salutation ="To whom it may concern"
				} else if (record.title.size() < 4) {
					record.salutation ="Dear ${record.title}. ${record.lastName}"
				} else {
					record.salutation ="Dear ${record.title} ${record.lastName}"
				}
            }

			// look up any comments that may exist
			item.comments.each{ c ->
				if (c.subject == 'general') {
					record.comments = c.text
				} else if (c.subject == 'instructions') {
					record.instructions = c.text
				} else if (c.subject == 'reason') {
					record.reason = c.text
				}
			}
			
            dataSet.add(record)
        }

        // Add Tracking Recipient info (if it exists...)

        def trackingDocumentRecipients = batchInstance?.creationConfig?.recipients

        trackingDocumentRecipients.sort{it.id}.each {
            def record = [
                itemId: 0,
                itemIdBarcode: "*B${batchInstance?.id}*",
                batchId: batchInstance?.id,
                batchDate: batchInstance?.dateCreated,
                mailDate: batchInstance?.mailDate,
                instrumentDate: batchInstance?.instrumentDate,
                pieces: batchInstance?.pieces,
                instrumentId: batchInstance?.primaryInstrument?.id,
                instrumentName: batchInstance?.primaryInstrument?.name,
                studyId: batchInstance?.primaryInstrument?.study?.id,
                studyName: batchInstance?.primaryInstrument?.study?.name,
                documentTitle: batchInstance?.primaryInstrument?.study?.name + ' ' + batchInstance?.primaryInstrument?.name,
                fullStudyName: batchInstance?.primaryInstrument?.study?.fullName,
                isInitial: batchInstance?.primaryBatchInstrument?.isInitial,
                studyYear: null,
                expirationDate: null,
                instrumentVersion: batchInstance?.primaryBatchInstrument?.itemVersion,
                parentItemId: null,
                parentDate: null,
                parentName: null,
                childItemId: null,
                resultName: null,
                resultDate: null,
                dwellingUnitId: 0,
                householdId: 0,
                personId: 0,
				instructions: null,
				comments: "tracking document",
				reason: null,
                address: it.address?.address,
                address2: it.address?.address2,
                zipCode: it.address?.zipCode,
                country: it.address?.country?.name,
                cityStateZip: it.address?.cityStateZip,
				city: it.address?.city,
				state: it.address?.state,
				zipCode: it.address?.zipCode,
				zip4: it.address?.zip4

            ]

            if (it?.person) {
                record.personId = it.person.id
                record.fullName = it.person?.fullName
                record.title = it.person?.title
                record.firstName = it.person?.firstName
                record.lastName = it.person?.lastName
                record.gender = it.person?.gender?.name
                record.salutation = (record.lastName == null) ? "To whom it may concern" : (record.title == null) ? (record.gender?.id == 1) ? "Dear Mr. " + record.lastName : (record.gender?.id == 2) ? "Dear Ms. " + record.lastName : "To whom it may concern" : (record.title.size() < 4) ? "Dear " + record.title + ". " + record.lastName : "Dear " + record.title + " " + record.lastName
            }

             dataSet.add(record)

        }

        return dataSet
    }

	// TODO: Implement
	/** This method adds arbitrary data to a dataset based on a 
	closure or SQL Query that is stored in a DataSetType instance.
	 */
	def addDataSet(DataSetType dataSetTypeInstance, dataSet) {
		def returnDataSet = dataSet
		// This runs a groovy closure and appends data
		if (dataSetTypeInstance.closure) {
			// define a groovy shell
			def groovyShellInstance = new GroovyShell()
			try {
				// create the closure object
				def dataSetTypeClosure = groovyShellInstance.evaluate(dataSetTypeInstance.closure)
				// run the closure and get the new data
				returnDataSet = dataSetTypeClosure(returnDataSet)
			} catch (Exeception ex) {
				throw InvalidDataSetTypeClosureException(returnDataSet)
			}
		}

		if (dataSetTypeInstance.sqlQuery) {
			throw NotImplementedException
		}
		return returnDataSet
	}

	/**
	This method adds dwelling unit data
	to a data set if the dwelling unit is tied 
	to a tracked item.  This data set includes
	street address information.  If
	the person data is added, it will OVERWRITE
	this dataset, so DO NOT use both.
	*/
    def addDwellingUnitData(dataSet) {

        dataSet.collect{ record ->

            def dwellingUnitInstance = DwellingUnit.read(record.dwellingUnitId)
            if (dwellingUnitInstance) {
                record.salutation = "Neighbor"
                record.address = dwellingUnitInstance?.address?.address
                record.address2 = dwellingUnitInstance?.address?.address2
                record.zipCode =  dwellingUnitInstance?.address?.zipCode
                record.country = dwellingUnitInstance?.address?.country?.name
                record.cityStateZip =  dwellingUnitInstance?.address?.cityStateZip
				record.city = dwellingUnitInstance?.address?.city
				record.state = dwellingUnitInstance?.address?.state
				record.zipCode = dwellingUnitInstance?.address?.zipCode
				record.zip4 = dwellingUnitInstance?.address?.zip4
            }
        }

        dataSet
    }

    // Note: selectionGroup --> enrollmentType
    //  Example: address1 --> "Mc Namara" (building name), address2 --> "200 Oak St. SE Minneapolis MN 55455"

	/**
	This method adds dwelling unit data
	to a data set if a person is tied 
	to a tracked item.  This data set includes
	street address information.  If
	the dwelling unit data is added, it will OVERWRITE
	the salutation and address, so DO NOT use both.
	*/
    def addPersonData(dataSet) {
        
		Study studyInstance = null
		
		if (dataSet) {
			studyInstance = Study.read(dataSet[0]?.studyId)
		}
		
        dataSet.collect{ record ->
            def personInstance = Person.read(record.personId)
            if (personInstance) {
				
				// TODO: Speed this up with some HQL pre-fetching to a cache map
				def subjectInstance = Subject.findByPersonAndStudy(personInstance, studyInstance)
				
                record.subjectId = subjectInstance?.subjectId
                record.selectionDate = subjectInstance?.selectionDate
                record.fullName = personInstance?.fullName
                record.title = personInstance?.title
                record.firstName = personInstance?.firstName
                record.lastName = personInstance?.lastName
                record.gender = personInstance?.gender?.name
                record.address = personInstance?.bestAddress?.streetAddress?.address
                record.address2 = personInstance?.bestAddress?.streetAddress?.address2
                record.cityStateZip = personInstance?.bestAddress?.streetAddress?.cityStateZip
				record.city = personInstance?.bestAddress?.streetAddress?.city
				record.state = personInstance?.bestAddress?.streetAddress?.state
                record.zipCode =  personInstance?.bestAddress?.streetAddress?.zipCode
				record.zip4 = personInstance?.bestAddress?.streetAddress?.zip4
                record.country = personInstance?.bestAddress?.streetAddress?.country?.name
                record.primaryPhone = personInstance?.primaryPhone?.phoneNumber?.prettyPhone
                record.primaryPhoneType = personInstance?.primaryPhone?.phoneType?.name
                record.primaryEmail = personInstance?.primaryEmail?.emailAddress?.emailAddress
                record.primaryEmailType = personInstance?.primaryEmail?.emailType?.name
                record.birthDate = personInstance?.birthDate
                record.deathDate = personInstance?.deathDate
                record.enrollmentType = subjectInstance?.enrollment
                
            }
        }
        dataSet
    }

	/**
	This adds NORC specific IDs to the data set.
	*/
    def addNORCData(dataSet) {
		
		def instrumentLinkCache = [:]
		def studyLinkCache = [:]
		
		def batchId = dataSet[0].batchId
		
		// we want to get all of the NORC SU_IDs in a single query.
		// ... so how do we do this? Let's try HQL (test batch: 340)
		// Baseline time = 45s for 2000 records
		
		// Let's cache the NORC SU_IDs.
		def norcSuIdCache = [:]
		def query = ""
			
		// First we'll load the Dwelling Unit SU_ID records
		query = "\
		select ti.id as trackedItemId, dul.norcSuId \
		from DwellingUnitLink as dul inner join \
			dul.dwellingUnit as du, \
			TrackedItem as ti inner join \
			ti.batch as b \
		where ti.dwellingUnit = du \
			and b.id = ?"
	
		// Fingers crossed!!!
		def dwellingUnitNorcSuId = DwellingUnitLink.executeQuery(query, [batchId])
		dwellingUnitNorcSuId.each{ row ->
			def trackedItemId = row[0]
			def norcSuId = row[1]
			// saving the NORC SU_ID to the cache
			norcSuIdCache[trackedItemId] = norcSuId
		}
	
		// Next we load the Person SU_ID records
		query = "\
		select ti.id as trackedItemId, pl.norcSuId \
		from PersonLink as pl inner join \
			pl.person as p, \
			TrackedItem as ti inner join \
			ti.batch as b \
		where ti.person = p \
			and b.id = ?"
	
		// Fingers crossed!!!
		def personNorcSuId = PersonLink.executeQuery(query, [batchId])
		personNorcSuId.each{ row ->
			def trackedItemId = row[0]
			def norcSuId = row[1]
			// saving the NORC SU_ID to the cache
			norcSuIdCache[trackedItemId] = norcSuId
		}

		// OK, we should have the person and dwelling unit SUIDs cached.
		
        dataSet.collect{record ->

            // Look up the NORC id varients for our different domain id types
			def instrumentInstance = null
			def instrumentLinkInstance = null
			// add the default values for the columns we are appending
			record.norcProjectId = ""
			record.norcDocId = ""
			record.norcSuId = ""
			record.norcMailingId = ""
			record.norcMailingBarcode = ""

			// see if we've cache'd "docId" data		
			if (instrumentLinkCache[record.instrumentId]) {
				// if so, load the instrumentLinkInstance
				record.norcDocId = instrumentLinkCache[record.instrumentId]
			} else {
				// otherwise pull up the instrument
				instrumentInstance = Instrument.read(record.instrumentId)
				// make sure we found one (shouldn't fail really)
				if (instrumentInstance) {
					// find the instrumnet link associated with this instrument
					instrumentLinkInstance = InstrumentLink.findByInstrument(instrumentInstance)
					
					// If we didn't find it, then...
					if (!instrumentLinkInstance) {
						// then we'll lookup it up by the parent id 
						// Do we have norcDocId for the parent?
						
						instrumentInstance = Instrument.read(record.parentInstrumentId)
						if (instrumentInstance) {
							// success!
							instrumentLinkInstance = InstrumentLink.findByInstrument(instrumentInstance)
						}
					}
					
					if (instrumentLinkInstance) {
						record.norcDocId = instrumentLinkInstance?.norcDocId
						instrumentLinkCache[record.instrumentId] = record.norcDocId
					}
				}
			}			

			// try to use cache'd "projectId" data
			if (studyLinkCache[record.studyId]) {
				record.norcProjectId = studyLinkCache[record.studyId]
			} else {
				def studyInstance = Study.read(record.studyId)
				if (studyInstance) {
					def studyLinkInstance = StudyLink.findByStudy(studyInstance)
					if (studyLinkInstance) {
						record.norcProjectId = studyLinkInstance?.norcProjectId
						// save the result to the cache
						studyLinkCache[record.studyId] = studyLinkInstance?.norcProjectId
					}
				} else {
					record.norcProjectId = ""
				}

			}
									
			// try to find the SU_ID in cache
			if (norcSuIdCache[record.itemId]) {
				// we found it in the cache, so use that one
				record.norcSuId = norcSuIdCache[record.itemId]
			} else if (record.itemId) {
				// Otherwise, for some reason ???, we need to look it up.
				println "Warning!  Missed HQL Cache of NORC SU_ID for tracked item: ${record.trackedItemId}"
	            def dwellingUnitInstance = DwellingUnit.read(record.dwellingUnitId)
	            if (dwellingUnitInstance) {
	                def dwellingUnitLinkInstance = DwellingUnitLink.findByDwellingUnit(dwellingUnitInstance)
	                if (dwellingUnitLinkInstance) {
	                    record.norcSuId = dwellingUnitLinkInstance?.norcSuId
	                } 
	            }
				
				def personInstance = Person.read(record.personId)
				if (personInstance) {
					def personLinkInstance = PersonLink.findByPerson(personInstance)
					if (personLinkInstance) {
						record.norcSuId = personLinkInstance?.norcSuId
					}
	        	}
	
	        }
			
			// build the NORC Mailing ID and bar code
			if (record.norcDocId && record.norcProjectId && record.norcSuId) {
				record.norcMailingId = "${record.norcProjectId}-${record.norcSuId}-${record.norcDocId}"
				record.norcMailingBarcode = "*${record.norcProjectId}-${record.norcSuId}-${record.norcDocId}*"
			}
        }
		
        dataSet
    }
	

	/**
	This adds child instruments tracked item IDs to the dataset.
	*/
	def addChildItems(dataSet) {

        debug = true

		// create a recursive closure
		def addChildItemsToRecord

		/**
		This adds child items recursively to a record.
		@param trackedItemInstance		This is the trackedItemInstance and it's children you want added
		@param addedTrackedItemIdSet	This is the set of tracked item IDs already added
		@return the record with the column and data added to it
		*/
		addChildItemsToRecord = { trackedItemInstance, addedTrackedItemIdSet ->
			// If we've been here before, then let's call it quits
			if ( addedTrackedItemIdSet.contains(trackedItemInstance.id) ) {
				return [:]
			} else {
				// Add this trackeitem
				def columnName = "child_${trackedItemInstance.batch.primaryInstrument.nickName}"
				def newMap = [:]
				// add this tracked item's ID to the hash map that will be returned
				newMap[columnName] = trackedItemInstance.id
				// add this tracked item's ID to the set of "already seen this" tracked items
				// in this particular branch that this recursive call is walking down.
				addedTrackedItemIdSet.add(trackedItemInstance.id)

				// look up all the child items
			    def childTrackedItemInstanceList = TrackedItem.findAllByParentItem(trackedItemInstance)
				// add all the child items (and their children) to the newMap
				childTrackedItemInstanceList.each{ cti ->


					// make sure we don't traverse out side of the doc gen batch bundle
					// This tracked item's batch must be the master batch for the child tracked item
					// ...or
					//      This child tracked item must have a master batch associated with it
					//		AND the master batch amongst all the children are the same
					if (trackedItemInstance.batch.id == cti.batch.master?.id 
						|| ( trackedItemInstance.batch.master?.id == cti.batch.master?.id 
							&& cti.batch.master )) {
						// add to the newMap anything returned by the recursive call
						newMap.putAll( addChildItemsToRecord(cti, addedTrackedItemIdSet) )
					}
				}
				// return the map (containing the item ID of this tracked item and all of it's children's
				// tracked item IDs from the doc gen bundle.
				return newMap
			}
		}

		// update the recordset
		dataSet.collect{record ->
			// Lookup the tracked item
			def trackedItemInstance = TrackedItem.read(record.itemId)

			// make sure it exists
			if ( trackedItemInstance ) {
				// Get the batch
				def masterBatchInstance = trackedItemInstance.batch
				// find all the child batches
				def childBatchInstanceList = Batch.findAllByMaster(masterBatchInstance)
				// Get the child instrument nickNames and add empty columns in the record
				// incase some of the data is blank
				def newColumnNames = childBatchInstanceList.each { b ->
					record["child_${b.primaryInstrument.nickName}"] = ''
				}

				// create an empty collection
				def itemSet = [] as Set
				// add the columns using the helper 
				record.putAll( addChildItemsToRecord(trackedItemInstance, itemSet) )
					
			} else {
				println "Couldn't find trackedItem ${record.itemId}."
			}
		}
		dataSet
	}


	/**
	This adds appointment information such
	as appointment type, time, date, location
	to the dataset for confirmation letters.
	*/
	def addAppointmentData(dataSet) {
		dataSet.collect{record ->
			def trackedItemInstance = TrackedItem.read(record.itemId)
			// TODO: Speed this up by pre-fetching Appointments by tracked item to a cache map.
			// Note, this is probably OK as the number of records it'll see is minimal.
			def appointmentInstance = Appointment.findByLetter(trackedItemInstance)
			if ( appointmentInstance ) {
				if (debug) { println "Found Appointment." }
				record.appointmentDate = appointmentInstance.startTime
				record.appointmentType = appointmentInstance.type
				record.appointmentResult = appointmentInstance.result
				record.appointmentLocation = appointmentInstance.location
				record.appointmentBillable = appointmentInstance.billable
				record.appointmentParentAppointmentTime = appointmentInstance.parentAppointment?.startTime
				record.appointmentScheduledBy = appointmentInstance.scheduledBy
				// TODO: Add Incentive Information
			} else {
				println "Couldn't find appointment using letter ID ${record.itemId}."
				record.appointmentDate = ""
				record.appointmentType = ""
				record.appointmentResult = ""
				record.appointmentLocation = ""
				record.appointmentBillable = ""
				record.appointmentParentAppointmentTime = ""
				record.appointmentScheduledBy = ""
			}
		}
		dataSet
	}
	
	/**
	This is the beginning of a custom dataset
	that could be built.
	Copy this to create your own.  Don't forget to
	rename yours so it doesn't have the same name!
	*/
    def addCustomData(dataSet) {
		dataSet.collect{record ->
			def trackedItemInstance = TrackedItem.read(record.itemId)
			def personInstance = trackedItemInstance.person
		}
		dataSet
    }
}
