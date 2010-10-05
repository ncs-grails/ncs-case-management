package edu.umn.ncs

/*
 * This service will build a merge data set from a print queue based on the requested
 * criteria.
 *
 * Criteria Available (TODO)
 *   Base: sid + barcode, bid + barcode, batchDate, mailDate, peices, eventDesc,
 *     studyName, fullStudyName, isInitial, instrumentDate, studyYear,
 *     expirationDate, version, documentTitle, instrumentId, instructions,
 *     reason, comments, parentSID, parentDate, parentName, childSID (first),
 *     childData, childName, result, recievedData
 *
 *   Dwelling Unit:
 *      dewllingId, address1, address2, zipCode. cityStateZip
 *
 *   Person:
 *      personId, participantId, selectionDate, selectionGroup,
 *      fullName, salutation, title, firstName, lastName, gender, birthDate,
 *      deathDate, address1, address2, zipCode, cityStateZip, primaryPhone,
 *      primaryPhoneType, primaryEmail, primaryEmailType
 *
 *   Custom?:
 *   Appointment?:
 *
 */

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
                batchId: batchInstance?.id,
                batchDate: batchInstance?.dateCreated,
                mailDate: batchInstance?.mailDate,
                instrumentDate: batchInstance?.instrumentDate,
                pieces: batchInstance?.pieces,
                instrumentId: batchInstance?.primaryInstrument?.id,
                instrumentName: batchInstance?.primaryInstrument?.name,
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

        return dataSet

    }

    def addDwellingUnitData(dataSet) {

        dataSet.collect{ record ->
            def dwellingUnitInstance = DwellingUnit.get(record.dwellingUnitId)
            if (dwellingUnitInstance) {
                record.address1 = dwellingUnitInstance?.address?.address
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
            def personInstance = Person.get(record.personId)
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
    def addCustomData() {

    }

}
