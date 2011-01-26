package edu.umn.ncs

class MergeDataBuilderService {

    static transactional = true

    def getBaseData(Batch batchInstance) {
        // this will be a list of map items.
        def dataSet = []

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
                record.salutation = (record.lastName == null) ? "To whom it may concern" : (record.title == null) ? (record.gender?.id == 1) ? "Dear Mr. " + record.lastName : (record.gender?.id == 2) ? "Dear Ms. " + record.lastName : "To whom it may concern" : (record.title.size() < 4) ? "Dear " + record.title + ". " + record.lastName : "Dear " + record.title + " " + record.lastName
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
                cityStateZip: it.address?.cityStateZip

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
            }
        }

        dataSet
    }

    // Note: selectionGroup --> enrollmentType
    //  Example: address1 --> "Mc Namara" (building name), address2 --> "200 Oak St. SE Minneapolis MN 55455"

    def addPersonData(dataSet) {
        
        dataSet.collect{ record ->
            def personInstance = Person.read(record.personId)
            if (personInstance) {
                record.subjectId = Subject.findByPerson(personInstance)?.subjectId
                record.selectionDate = Subject.findByPerson(personInstance)?.selectionDate
                record.fullName = personInstance?.fullName
                record.title = personInstance?.title
                record.firstName = personInstance?.firstName
                record.lastName = personInstance?.lastName
                record.gender = personInstance?.gender?.name
                record.salutation = (record.lastName == null) ? "To whom it may concern" : (record.title == null) ? (record.gender == "male") ? "Dear Mr. " + record.lastName : (record.gender == "female") ? "Dear Ms. " + record.lastName : "To whom it may concern" : (record.title.size() < 4) ? "Dear " + record.title + ". " + record.lastName : "Dear " + record.title + " " + record.lastName
                record.address = personInstance?.bestAddress?.address
                record.address2 = personInstance?.bestAddress?.address2
                record.zipCode =  personInstance?.bestAddress?.zipCode
                record.country = personInstance?.bestAddress?.country?.name
                record.cityStateZip = personInstance?.bestAddress?.cityStateZip
                record.primaryPhone = personInstance?.primaryPhone?.phoneNumber?.prettyPhone
                record.primaryPhoneType = personInstance?.primaryPhone?.phoneType?.name
                record.primaryEmail = personInstance?.primaryEmail?.emailAddress?.emailAddress
                record.primaryEmailType = personInstance?.primaryEmail?.emailType?.name
                record.birthDate = personInstance?.birthDate
                record.deathDate = personInstance?.deathDate
                record.enrollmentType = Subject.findByPerson(personInstance?.enrollment?.name)
                
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
        }
        dataSet
    }
    def addCustomData() {

    }

}
