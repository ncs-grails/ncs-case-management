import edu.umn.ncs.*

class BootStrap {

    def init = { servletContext ->

        // Global BootStrap

        def today = new Date()
        def appName = 'ncs-case-management'

        /*** People Seciton ***/
        /* Items: AddressType, ContactRole, Country, EmailType,
         * EnrollmentType, Ethnicity, Gender, PhoneType, RelationshipType,
         * Study */

        //AddressType
        def homeAddress = AddressType.findByName('home')
        if (! homeAddress) {
            homeAddress = new AddressType(name:'home').save()
        }
        def seasonalAddress = AddressType.findByName('seasonal')
        if (! homeAddress) {
            seasonalAddress = new AddressType(name:'seasonal').save()
        }
        //ContactRoleType
        def emergencyContact = ContactRoleType.findByName('emergency')
        if (! emergencyContact) {
            emergencyContact = new ContactRoleType(name:'emergency').save()
        }
        //Country
        def us = Country.findByAbbreviation('us')
        if (! us) {
            us = new Country(name:'United States', abbreviation:'us').save()
        }
        //EmailType
        def personalEmail = EmailType.findByName('personal')
        if (! personalEmail) {
            personalEmail = new EmailType(name:'personal').save()
        }
        //EnrollmentType
        def highIntensity = EnrollmentType.findByName('high intensity')
        if (! highIntensity) {
            highIntensity = new EnrollmentType(name:'high intensity').save()
        }
        //Ethnicity
        def latino = Ethnicity.findByName('latino')
        if (! latino) {
            latino = new Ethnicity(name:'latino').save()
        }
        //Gender
        def male = Gender.findByName('male')
        if (! male) {
            male = new Gender(name:'male').save()
        }
        def female = Gender.findByName('female')
        if (! female) {
            female = new Gender(name:'female').save()
        }
        //PhoneType
        def mobilePhone = PhoneType.findByName('mobile')
        if (! mobilePhone) {
            mobilePhone = new PhoneType(name:'mobile').save()
        }
        //RelationshipType
        def mother = RelationshipType.findByName('mother')
        if (! mother) {
            mother = new RelationshipType(name:'mother').save()
        }
        //Study
        def ncs = Study.findByName("National Children's Study")
        if (! ncs) {
            ncs = new Study(name:"NCS",
                fullName:"National Children's Study",
                active:true, sponsor:'NCI/NICHD',
                coordinator:"NICHD",
                collaborator:"TBA",
                purpose:"improving the health of america's children",
                exclusionary: false)
            if (!ncs.save()) {
                ncs.errors.each{
                    println "${it}"
                }
            }
        }

        /*** Tracking Seciton ***/
        /* Items: BatchDirection, InstrumentFormat, IsInitial, Result
         */

        // BatchDirection
        def outgoing = BatchDirection.findByName('outgoing')
        if (! outgoing) {
            outgoing = new BatchDirection(name:'outgoing').save()
        }
        def incoming = BatchDirection.findByName('incoming')
        if (! incoming) {
            incoming = new BatchDirection(name:'incoming').save()
        }
        def internal = BatchDirection.findByName('internal')
        if (! internal) {
            internal = new BatchDirection(name:'internal').save()
        }

        // MergeDataSource
        def dwellingUnitData = DataSetType.findByCode("dwelling")
        if (! dwellingUnitData) {
            dwellingUnitData = new DataSetType(name:"Dwelling Unit", code:'dwelling').save()
        }

        def personData = DataSetType.findByCode("person")
        if (! personData) {
            personData = new DataSetType(name:"Person", code:'person').save()
        }

        def customData = DataSetType.findByCode("custom")
        if (! customData) {
            customData = new DataSetType(name:"Custom", code:'custom').save()
        }

        // InstrumentFormat
        def firstClassMail = InstrumentFormat.findByName('first class mail')
        if (!firstClassMail) {
            firstClassMail = new InstrumentFormat(name:'first class mail', groupName:'mail').save()
        }
        def inPerson = InstrumentFormat.findByName('in person')
        if (!inPerson) {
            inPerson = new InstrumentFormat(name:'in person', groupName:'person').save()
        }
        def onThePhone = InstrumentFormat.findByName('phone')
        if (!onThePhone) {
            onThePhone = new InstrumentFormat(name:'phone', groupName:'phone').save()
        }

        // IsInitial
        def initial = IsInitial.findByName('initial')
        if (!initial) {
            initial = new IsInitial(name:'initial').save()
        }
        def reminder = IsInitial.findByName('reminder')
        if (!reminder) {
            reminder = new IsInitial(name:'reminder').save()
        }

        // Result
        def received = Result.findByName('received')
        if (!received) {
            received = new Result(name:'received').save()
        }

        // Relationship between sub batches
        // id = 1
        def attachmentOf = BatchCreationItemRelation.findByName('attachment')
        if (!attachmentOf) {
            attachmentOf = new BatchCreationItemRelation(name:'attachment').save()
        }
        // id = 2
        def childOf = BatchCreationItemRelation.findByName('child')
        if (!childOf) {
            childOf = new BatchCreationItemRelation(name:'child').save()
        }
        // id = 3
        def sisterOf = BatchCreationItemRelation.findByName('sister')
        if (!sisterOf) {
            sisterOf = new BatchCreationItemRelation(name:'sister').save()
        }

        // Source of the Batch
        // id = 1
        def personSource = BatchCreationQueueSource.findByName("person")
        if (!personSource) {
            personSource = new BatchCreationQueueSource(name:'person').save()
        }

        // id = 2
        def householdSource = BatchCreationQueueSource.findByName("household")
        if (!householdSource) {
            householdSource = new BatchCreationQueueSource(name:'household').save()
        }

        // id = 3
        def dwellingUnitSource = BatchCreationQueueSource.findByName("dwellingUnit")
        if (!dwellingUnitSource) {
            dwellingUnitSource = new BatchCreationQueueSource(name:'dwellingUnit').save()
        }

        // Test Data
        environments {
            development {

                /*
                 * 1636 BREDA AVE            |           |          | SAINT PAUL | MN         | 55108 | 2701 |
                 * 4 WYOMING ST E            |           |          | SAINT PAUL | MN         | 55107 | 3240 |
                 * 1372 HAZEL ST N           |           |          | SAINT PAUL | MN         | 55119 | 4507 |
                 * 180 WAYZATA ST            | APT       | 114      | SAINT PAUL | MN         | 55117 | 5351 |
                 * 2122 WOODLYNN AVE         | APT       | 4        | SAINT PAUL | MN         | 55109 | 1480 |
                 * 3744 CLEVELAND AVE N      | APT       | 104      | SAINT PAUL | MN         | 55112 | 3264 |
                 * 1255 FLANDRAU ST          |           |          | SAINT PAUL | MN         | 55106 | 2302 |
                 * 4310 OLD WHITE BEAR AVE N |           |          | SAINT PAUL | MN         | 55110 | 3874 |
                 * 1131 MARION ST            |           |          | SAINT PAUL | MN         | 55117 | 4461 |
                 * 305 EDMUND AVE            |           |          | SAINT PAUL | MN         | 55103 | 1708 |
                 * 1412 COUNTY ROAD E W      |           |          | SAINT PAUL | MN         | 55112 | 3653 |
                 * 1952 OAK KNOLL DR         |           |          | SAINT PAUL | MN         | 55110 | 4263 |
                 * 480 GERANIUM AVE E        |           |          | SAINT PAUL | MN         | 55130 | 3709 |
                 * 1140 4TH ST E             | APT       | 306      | SAINT PAUL | MN         | 55106 | 5353 |
                 * 1793 MORGAN AVE           |           |          | SAINT PAUL | MN         | 55116 | 2721 |
                 * 346 CLEVELAND AVE SW      | APT       | 14       | SAINT PAUL | MN         | 55112 | 3535 |
                 * 1575 SAINT PAUL AVE       | APT       | 9        | SAINT PAUL | MN         | 55116 | 2862 |
                 * 4041 BETHEL DR            | APT       | 27       | SAINT PAUL | MN         | 55112 | 6921 |
                 * 1265 3RD ST E             |           |          | SAINT PAUL | MN         | 55106 | 5778 |
                 * 1528 BREDA AVE            |           |          | SAINT PAUL | MN         | 55108 | 2610 |
                 */

                def myAddressList = [
                    ['1636 BREDA AVE', 'SAINT PAUL', 'MN', '55108', '2701'],
                    ['WYOMING ST E', 'SAINT PAUL', 'MN', '55107', '3240'],
                    ['1372 HAZEL ST N', 'SAINT PAUL', 'MN', '55119', '4507 '],
                    ['180 WAYZATA ST APT 114', 'SAINT PAUL', 'MN', '55117', '5351'],
                    ['2122 WOODLYNN AVE APT 4', 'SAINT PAUL', 'MN', '55109', '1480'],
                    ['3744 CLEVELAND AVE N APT 104', 'SAINT PAUL', 'MN', '55112', '3264'],
                    ['1255 FLANDRAU ST', 'SAINT PAUL', 'MN', '55106', '2302'],
                    ['4310 OLD WHITE BEAR AVE N', 'SAINT PAUL', 'MN', '55110', '3874'],
                    ['1131 MARION ST', 'SAINT PAUL', 'MN', '55117', '4461'],
                    ['305 EDMUND AVE', 'SAINT PAUL', 'MN', '55103', '1708'],
                    ['1412 COUNTY ROAD E W', 'SAINT PAUL', 'MN', '55112', '3653'],
                    ['1952 OAK KNOLL DR', 'SAINT PAUL', 'MN', '55110', '4263'],
                    ['480 GERANIUM AVE E', 'SAINT PAUL', 'MN', '55130', '3709'],
                    ['1140 4TH ST E APT 306', 'SAINT PAUL', 'MN', '55106', '5353'],
                    ['1793 MORGAN AVE', 'SAINT PAUL', 'MN', '55116', '2721'],
                    ['346 CLEVELAND AVE SW APT 14', 'SAINT PAUL', 'MN', '55112', '3535'],
                    ['1575 SAINT PAUL AVE APT 9', 'SAINT PAUL', 'MN', '55116', '2862'],
                    ['4041 BETHEL DR APT 27', 'SAINT PAUL', 'MN', '55112', '6921'],
                    ['1265 3RD ST E','SAINT PAUL', 'MN', '55106', '5778'],
                    ['1528 BREDA AVE','SAINT PAUL', 'MN', '55108', '2610']
                ]

                myAddressList.each{
                    def sa = new StreetAddress(address:it[0],
                        city:it[1], state:it[2], zipCode:it[3], zip4:it[4],
                        country:us, county:'Ramsey', appCreated:'byHand').save()

                    def du = new DwellingUnit(address:sa,
                        appCreated:'byHand').save()

                     //println "Created Dwelling unit: ${du?.id}:${sa?.id}"
                }

                // throw some test data into the database
                def myAddress = new StreetAddress(address:'3323 Buchanan St NE',
                    city:'Minneapolis',
                    state:'MN', zipCode:55418, zip4:1449,
                    county:'Hennepin', country:us,
                    appCreated:'byHand').save()

                def me = new Person(title:'Mr',
                    firstName:'Aaron',
                    middleName:'James',
                    lastName:'Zirbes',
                    suffix:null,
                    birthDate:new Date(79,11,21),
                    gender:male,
                    alive:true,
                    isRecruitable:false,
                    appCreated:'byHand').save()

                def myUnit = new DwellingUnit(address:myAddress,
                    appCreated:'byHand').save()

                def hiQ = new Instrument(name:'High Intensity Questionnaire',
                    nickName:'HiQ', study:ncs, requiresPrimaryContact:true).save()

                def faq = new Instrument(name:'Frequently Asked Questions',
                    nickName:'FAQ', study:ncs, requiresPrimaryContact:false).save()

                def loQ = new Instrument(name:'Low Intensity Questionnaire',
                    nickName:'LoQ', study:ncs, requiresPrimaryContact:true).save()

                def asu = new Instrument(name:'Annual Survey Update',
                    nickName:'ASU', study:ncs, requiresPrimaryContact:true).save()

                def eoi = new Instrument(name:'Event of Interest',
                    nickName:'EOI', study:ncs, requiresPrimaryContact:false).save()

                def bcert = new Instrument(name:'Birth Certificate',
                    nickName:'B-Cert', study:ncs, requiresPrimaryContact:false).save()

                def tracingLog = new Instrument(name:'Tracing Log',
                    nickName:'T-LOG', study:ncs, requiresPrimaryContact:false).save()

                def sql = """SELECT du.id AS dwelling_unit
FROM dwelling_unit du INNER JOIN
  street_address sa ON du.address_id = sa.id""";

                def bccHiQ = new BatchCreationConfig(name:'HiQ Initial',
                    instrument:hiQ, format:firstClassMail, direction: outgoing,
                    isInitial:initial, selectionQuery:sql, active:true, manualSelection:true,
                    oneBatchEventPerson:true).save()

                // add a document
                bccHiQ.addToDocuments(
                    documentLocation:'n:/merge_documents/hiq_letter_merge.doc',
                    mergeSourceQuery:"qDefault()",
                    mergeSourceFile:'q:/merge_data/hi_q_source.txt')
                .addToDocuments(
                    documentLocation:'n:/merge_documents/frequently_asked_questions.doc')
                .save()

                // for test adding dependent before parent subitem added
                bccHiQ.addToSubItems(instrument:asu,
                    direction:outgoing,
                    format:firstClassMail,
                    relation:childOf,
                    parentInstrument:faq).save()

                // add a item
                bccHiQ.addToSubItems(instrument:faq,
                    direction:outgoing,
                    format:firstClassMail,
                    relation:childOf,
                    parentInstrument:hiQ).save()

                // Fake Mailing #1
                // generate a batch

                def batchHiQ = new Batch(instrument:hiQ, format:firstClassMail,
                    direction: outgoing, instrumentDate: today, batchRunBy:'ajz',
                    batchRunByWhat: appName, trackingDocumentSent:false,
                    creationConfig: bccHiQ)

                bccHiQ.addToBatches(batchHiQ)

                if (! bccHiQ.save() ) {
                    println "ERRORS:"
                    bccHiQ.errors.each{ error ->
                        println "ERROR>> ${error} "
                    }
                    println ""
                } else {
                    // add an instrument
                    batchHiQ.addToInstruments(instrument:hiQ, isInitial:initial).save()

                    // add items to the batch
                    // batchHiQ.addToItems().save()
                }

            }
        }

        // add 'capitalize()' function to Strings
        String.metaClass.capitalize = {->
            return delegate.tokenize().collect{ word ->
                word.substring(0,1).toUpperCase() + word.substring(1, word.size())
            }.join(' ')
        }

    }
    def destroy = {
    }
}
