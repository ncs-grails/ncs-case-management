package edu.umn.ncs

import org.joda.time.*
import grails.test.*

class BatchControllerTests extends ControllerUnitTestCase {

    def emailService = new EmailService()

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndexRedirect() {
		def bc = new BatchController()
		bc.index()
		assertEquals "list", bc.redirectArgs.action
    }

	void testSendNightlyReport() {
		// instantiate a new BatchController
		def bc = new BatchController()

		// get a "mockFor" for the EmailSevice
		def esMocker = mockFor(EmailService)
		// tell the mocker that the mocked object (EmailService)
		// should return true the first five times that
		// sendProductionReport is called.
		esMocker.demand.sendProductionReport(1..1) { -> return true }
		// set the batch controller "emailService" variable
		// to the mocked class ( EmailService)
		bc.emailService = esMocker.createMock()

		// run the production report
		bc.sendNightlyReport()

		// check our results
		assertEquals "index", bc.redirectArgs.action
		assertEquals "mainMenu", bc.redirectArgs.controller
	}

	void testNightlyReport() {
		// we don't need this because the ONLY thing
		// we use in the Batch class is createCriteria()
		//mockDomain(Batch)

		// mock up the createCriteria
		def batchCriteria = [ list: {Closure cls -> []} ]
		// get a "mockFor" for the EmailSevice
		def batchMocker = mockFor(Batch, true)
		// tell the mocker that the mocked object (EmailService)
		// should return true the first five times that
		// sendProductionReport is called.
		batchMocker.demand.static.createCriteria(1..1) { batchCriteria }
		// set the batch controller "emailService" variable
		// to the mocked class ( EmailService)
		mockDomain(Batch)

		// Create an instance of the BatchController
		def bc = new BatchController()
		// Run the nightlyReport action
		def model = bc.nightlyReport()

		// use this as the date to assert againt
        def midnight = new LocalTime(0, 0)
		// Create a JodaTime LocalDate for today (no time info)
		def referenceDate = new LocalDate()
		// Convert the LocalDate to a java.util.Date
        def startDate = referenceDate.toDateTime(midnight).toCalendar().time
		// make and empty list
		def emptyList = []

		// check the results
		assertEquals model.referenceDate, startDate
		assertEquals model.batchInstanceList, emptyList
		assert model.customizable
	}

	void testSendNorcAlert() {
		// instantiate a new BatchController
		def bc = new BatchController()

		// get a "mockFor" for the EmailSevice
		def esMocker = mockFor(EmailService)
		// tell the mocker that the mocked object (EmailService)
		// should return true the first five times that
		// sendProductionReport is called.
		esMocker.demand.sendNorcAlert(1..1) { return true }
		// set the batch controller "emailService" variable
		// to the mocked class ( EmailService)
		bc.emailService = esMocker.createMock()

		// run the production report
		bc.sendNorcAlert()

		// check our results
		assertEquals "mainMenu", bc.redirectArgs.controller
		assertEquals "index", bc.redirectArgs.action
	}

	void testNorcAlert() {
		mockDomain(BatchLink)

		// instantiate a new BatchController
		def bc = new BatchController()

		// setup the email service
		bc.emailService = emailService

		// mock up the createCriteria
		def batchCriteria = [ list: {Closure cls -> []} ]
		// get a "mockFor" for the EmailSevice
		def batchMocker = mockFor(Batch, true)
		// tell the mocker that the mocked object (EmailService)
		// should return true the first five times that
		// sendProductionReport is called.
		batchMocker.demand.static.createCriteria(1..1) { batchCriteria }
		// set the batch controller "emailService" variable
		// to the mocked class ( EmailService)
		mockDomain(Batch)

		// use this as the date to assert againt
        def midnight = new LocalTime(0, 0)
		// Create a JodaTime LocalDate for today (no time info)
		def referenceDate = new LocalDate().minusDays(1)
		// Convert the LocalDate to a java.util.Date
        def startDate = referenceDate.toDateTime(midnight).toCalendar().time
		// run the production report
		def model = bc.norcAlert()

		// make and empty list
		def emptyList = []

		// check our results
		assertEquals model.referenceDate, startDate
		assertEquals model.batchInstanceList, emptyList
		assert model.customizable
	}

	void testEntryExists() {
		def bc = new BatchController()
		assert bc.entry
	}

	void testEntryForValidBatch() {
		// make some fake batches
		def now = new Date()
		def today = now
		def fiveDaysAgo = now - 5
		today.hours = 0
		today.minutes = 0
		today.seconds = 0
		def yesterday = ( new Date() ) - 1
		def outgoing = new BatchDirection(name:"outgoing")
        def firstClass = new InstrumentFormat(name:'first class', groupName:'mail')
		def username = "unittest"
		def appName = "unit test"
		def b1 = new Batch(direction:outgoing,
			format: firstClass, 
			batchRunBy: username, 
			batchRunByWhat: appName,
			dateCreated: today)
		def b2 = new Batch(direction:outgoing,
			format: firstClass, 
			batchRunBy: username, 
			batchRunByWhat: appName,
			dateCreated: yesterday)
		b1.id = 1
		b2.id = 2

		def batchInstanceList = [ b1, b2 ]

		// mock up the createCriteria
		def batchCriteria = [ list: {Closure cls -> []} ]
		// get a "mockFor" for the EmailSevice
		def batchMocker = mockFor(Batch, true)
		// tell the mocker that the mocked object (EmailService)
		// should return true the first five times that
		// sendProductionReport is called.
		batchMocker.demand.static.createCriteria(1..6) { batchCriteria }
		// set the batch controller "emailService" variable
		// to the mocked class ( EmailService)
		mockDomain(Batch, batchInstanceList)

		// instatiate the batch controller
		def bc = new BatchController()

		//Mock up the authenticateService
		def principal = [ getUsername: { "unittest" } ]
		def authService = [ principal: { principal } ]
		bc.authenticateService = authService

		// set the params
		bc.params.id = 'B2'
		bc.params.referenceDate = fiveDaysAgo

		// execute the action and get the result
		bc.entry()
		assertEquals "Mail Date must come after date generated", bc.flash.message

		// set the params
		bc.params.id = 'B5'
		bc.entry()
		assertEquals "Batch not found: 5", bc.flash.message

		// set the params
		bc.params.id = 'NGP'
		bc.entry()
		assertEquals "Invalid Batch ID format: NGP", bc.flash.message


	}

	void testListExists() {
		def bc = new BatchController()
		assert bc.list
	}
	
	void testShowExists() {
		def bc = new BatchController()
		assert bc.show
	}

	void testShowRedirectIfNoMatch() {
		// Not mocking this because it shouldn't break any other tests...
		BatchController.metaClass.message = { LinkedHashMap arg1 -> return 'test message output'}
		mockDomain(Batch)
		def bc = new BatchController()
		bc.show()
		assertEquals "list", bc.redirectArgs.action
	}

	void testEditExists() {
		def bc = new BatchController()
		assert bc.edit
	}

	void testUpdateExists() {
		def bc = new BatchController()
		assert bc.update
	}

	void testDeleteItems() {
		def bc = new BatchController()
		assert bc.deleteItems
	}

	void testAddItem() {
		def bc = new BatchController()
		assert bc.addItem
	}

	void testAddItemRedirectIfBatchDoesntExist() {
		// Not mocking this because it shouldn't break any other tests...
		BatchController.metaClass.message = { LinkedHashMap arg1 -> return 'test message output'}
		mockDomain(Batch)
		def bc = new BatchController()
		bc.addItem()
		assertEquals "list", bc.redirectArgs.action
	}

	void testListByDate() {
		def bc = new BatchController()
    	assert bc.listByDate
	}

	void testEditDates() {
		def bc = new BatchController()
    	assert bc.editDates
	}

	void testUpdateDates() {
		def bc = new BatchController()
    	assert bc.updateDates 
	}
}
