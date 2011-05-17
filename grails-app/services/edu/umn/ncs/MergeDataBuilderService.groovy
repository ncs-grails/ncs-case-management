package edu.umn.ncs

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

    def getBaseData(Batch batchInstance) {
        // this will be a list of map items.
        def dataSet = []

		if (debug) { println "MergeDataBuilderService:getBaseData:: called." }
		
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
                childItemId: TrackedItem.findByParentItem(item)?.id,
                resultName: item?.result?.result?.name,
                resultDate: item?.result?.receivedDate
            ]
			
			if (debug) { println "MergeDataBuilderService:getBaseData::record.itemId = ${record.itemId}" }

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

            def comments = TrackedItemComment.findByItemAndSubject(item, 'general')
            if (comments) {
                record.comments = comments.text
            }

            def instructions = TrackedItemComment.findByItemAndSubject(item, 'instructions')
            if (instructions) {
                record.instructions = instructions.text
            }

            def reason = TrackedItemComment.findByItemAndSubject(item, 'reason')
            if (reason) {
                record.reason = reason.text
            }
            dataSet.add(record)
        }


        // Add Tracking Recipient info (if it exists...)

        def trackingDocumentRecipients = batchInstance?.creationConfig?.recipients

        trackingDocumentRecipients.each {
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

    def addPersonData(dataSet) {
        
		Study studyInstance = null
		
		if (dataSet) {
			studyInstance = Study.read(dataSet[0]?.studyId)
		}
		
        dataSet.collect{ record ->
            def personInstance = Person.read(record.personId)
            if (personInstance) {
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
    def addNORCData(dataSet) {
        dataSet.collect{record ->

            // Look up the NORC id varients for our different domain id types
            def instrumentInstance = Instrument.read(record.instrumentId)
            if (instrumentInstance) {
                def instrumentLinkInstance = InstrumentLink.findByInstrument(instrumentInstance)
                if (instrumentLinkInstance) {
                    record.norcDocId = instrumentLinkInstance?.norcDocId
                }
            } else {
                record.norcDocId = ""
            }

            def studyInstance = Study.read(record.studyId)
            if (studyInstance) {
                def studyLinkInstance = StudyLink.findByStudy(studyInstance)
                if (studyLinkInstance) {
                    record.norcProjectId = studyLinkInstance?.norcProjectId
                }
            } else {
                record.norcProjectId = ""
            }

            def dwellingUnitInstance = DwellingUnit.read(record.dwellingUnitId)
            if (dwellingUnitInstance) {
                def dwellingUnitLinkInstance = DwellingUnitLink.findByDwellingUnit(dwellingUnitInstance)
                if (dwellingUnitLinkInstance) {
                    record.norcSuId = dwellingUnitLinkInstance?.norcSuId
                } 
            } else {
                record.norcSuId = ""
            }
			
			def personInstance = Person.read(record.personId)
			if (personInstance) {
				def personLinkInstance = PersonLink.findByPerson(personInstance)
				if (personLinkInstance) {
					record.norcSuId = personLinkInstance?.norcSuId
				}
			} else {
				record.norcSuId = ""
			}

			if (record.norcDocId && record.norcProjectId && record.norcSuId) {
				record.norcMailingId = "${record.norcProjectId}-${record.norcSuId}-${record.norcDocId}"
				record.norcMailingBarcode = "*${record.norcProjectId}-${record.norcSuId}-${record.norcDocId}*"
			} else {
				record.norcMailingId = ""
				record.norcMailingBarcode = ""
			}
        }
        dataSet
    }
	
	def addAppointmentData(dataSet) {
		dataSet.collect{record ->
			def trackedItemInstance = TrackedItem.read(record.itemId)
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
			}
		}
		dataSet
	}
	
    def addCustomData(dataSet) {
		dataSet.collect{record ->
			def trackedItemInstance = TrackedItem.read(record.itemId)
			def personInstance = trackedItemInstance.person
		}
		dataSet
    }

}
