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

        // Test Data
        environments {
            development {
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

                def sql = "SELECT id AS person_id FROM person WHERE (alive = 1)";

                def bccHiQ = new BatchCreationConfig(name:'HiQ Initial',
                    instrument:hiQ, format:firstClassMail, direction: outgoing,
                    isInitial:initial, selectionQuery:sql, active:true,
                    oneBatchEventPerson:true).save()

                // add a document
                bccHiQ.addToDocuments(
                        documentLocation:'n:/merge_documents/hiq_letter_merge.doc',
                        mergeSourceQuery:'SELECT * FROM something INNER JOING something ELSE WHERE (bid = 123)',
                        mergeSourceFile:'q:/merge_data/hi_q_source.txt')
                    .addToDocuments(
                        documentLocation:'n:/merge_documents/frequently_asked_questions.doc')
                    .save()

                // add a item
                bccHiQ.addToSubItems(instrument:faq,
                    direction:outgoing,
                    format:firstClassMail,
                    childOf:hiQ).save()

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
