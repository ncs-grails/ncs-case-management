package edu.umn.ncs.instruments

import grails.test.*
import edu.umn.ncs.TrackedItem
import edu.umn.ncs.Country

class EligibilityQuestionnaireControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()

		mockController(EligibilityQuestionnaireController)
		mockDomain(EligibilityQuestionnaire)
		mockDomain(TrackedItem)
		mockDomain(Country)

		// mocking the i18n message function
		EligibilityQuestionnaireController.metaClass.message = { LinkedHashMap arg1 -> return 'test message output'}
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testActionsExist() {
		def eqc = new EligibilityQuestionnaireController()
		assert eqc.index

		assert eqc.show
		assert eqc.list

		assert eqc.edit
		assert eqc.save

		assert eqc.create
		assert eqc.update
    }

	void testIndexRedirect(){
		def eqc = new EligibilityQuestionnaireController()
		eqc.index()
		assertEquals "list", eqc.redirectArgs.action
	}

	void testCreateNoTrackedItemRedirect(){
		def eqc = new EligibilityQuestionnaireController()
		eqc.create()
		assertEquals "dataEntry", eqc.redirectArgs.controller
		assertEquals "index", eqc.redirectArgs.action
	}

}
