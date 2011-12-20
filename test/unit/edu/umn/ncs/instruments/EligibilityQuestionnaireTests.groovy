package edu.umn.ncs.instruments

import grails.test.*
import edu.umn.ncs.Gender
import edu.umn.ncs.Country
import edu.umn.ncs.StreetAddress
import edu.umn.ncs.TrackedItem

class EligibilityQuestionnaireTests extends GrailsUnitTestCase {

	Country countryInstance
	TrackedItem trackedItemInstance
	Gender genderMale
	Gender genderFemale
	StreetAddress streetAddressInstance

    protected void setUp() {
        super.setUp()

		mockDomain(Gender)
		mockDomain(Country)
		mockDomain(StreetAddress)
		mockDomain(TrackedItem)
		mockDomain(EligibilityQuestionnaire)

		countryInstance = new Country(name:'United States', abbreviation:'us')
		countryInstance.id = 42
		trackedItemInstance = new TrackedItem()
		trackedItemInstance.id = 9999
		genderMale = new Gender(name:'male')
		genderMale.id = 1
		genderFemale = new Gender(name:'female')
		genderFemale.id = 2
		streetAddressInstance = new StreetAddress(address: '200 Oak St SE', city:'Minneapolis')
		streetAddressInstance.id = 55455
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testInstantiation() {
		def eq = new EligibilityQuestionnaire()
		def now = new Date()

		assert eq.dateCreated <= now
		assertFalse eq.standardized
		assertTrue eq.uspsDeliverable

    }

	/** This test assigns the minimum attributes required for a successful save. */
	void testMinimumAttributesToSave() {
		def eq = new EligibilityQuestionnaire()

		// should not validate if provenance fields are missing
		assertFalse eq.validate()

		eq.userCreated = 'fake user'
		eq.appCreated = 'fake app'
		assertTrue eq.validate()
		def eqSaved = eq.save()
		assert eqSaved.id > 0
	}

	void testTitleConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.title = null
		assertTrue eq.validate()
		eq.title = '1234567890'
		assertTrue eq.validate()
		eq.title = '1234567890+'
		assertFalse eq.validate()
	}

	void testFirstNameConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.firstName = null
		assertTrue eq.validate()
		eq.firstName = '123456789012345678901234567890'
		assertTrue eq.validate()
		eq.firstName = '123456789012345678901234567890+'
		assertFalse eq.validate()
	}

	void testLastNameConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.lastName = null
		assertTrue eq.validate()
		eq.lastName = '123456789012345678901234567890'
		assertTrue eq.validate()
		eq.lastName = '123456789012345678901234567890+'
		assertFalse eq.validate()
	}

	void testMiddleNameConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.middleName = null
		assertTrue eq.validate()
		eq.middleName = '12345678901234567890'
		assertTrue eq.validate()
		eq.middleName = '12345678901234567890+'
		assertFalse eq.validate()
	}

	void testSuffixConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.title = null
		assertTrue eq.validate()
		eq.title = '1234567890'
		assertTrue eq.validate()
		eq.title = '1234567890+'
		assertFalse eq.validate()
	}

	void testAddressConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.address = null
		assertTrue eq.validate()
		eq.address = '1234567890123456789012345678901234567890123456789012345678901234'
		assertTrue eq.validate()
		eq.address = '1234567890123456789012345678901234567890123456789012345678901234+'
		assertFalse eq.validate()
	}

	void testAddress2Constraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.address2 = null
		assertTrue eq.validate()
		eq.address2 = '1234567890123456789012345678901234567890123456789012345678901234'
		assertTrue eq.validate()
		eq.address2 = '1234567890123456789012345678901234567890123456789012345678901234+'
		assertFalse eq.validate()
	}

	void testCityConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.city = null
		assertTrue eq.validate()
		eq.city = '123456789012345678901234567890'
		assertTrue eq.validate()
		eq.city = '123456789012345678901234567890+'
		assertFalse eq.validate()
	}

	void testStateConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.state = null
		assertTrue eq.validate()
		eq.state = '12'
		assertTrue eq.validate()
		eq.state = '12+'
		assertFalse eq.validate()
	}

	void testInternationalPostalCodeConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.internationalPostalCode = null
		assertTrue eq.validate()
		eq.internationalPostalCode = '1234567890123456'
		assertTrue eq.validate()
		eq.internationalPostalCode = '1234567890123456+'
		assertFalse eq.validate()
	}

	void testZipCodeConstraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		// test valid values
		eq.zipCode = null
		assertTrue eq.validate()
		eq.zipCode = 0
		assertTrue eq.validate()
		eq.zipCode = 55418
		assertTrue eq.validate()
		eq.zipCode = 99999
		assertTrue eq.validate()

		// test out of range values
		eq.zipCode = -1
		assertFalse eq.validate()
		eq.zipCode = 100000
		assertFalse eq.validate()
	}

	void testZip4Constraint() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		// test valid values
		eq.zip4 = null
		assertTrue eq.validate()
		eq.zip4 = 0
		assertTrue eq.validate()
		eq.zip4 = 2008
		assertTrue eq.validate()
		eq.zip4 = 9999
		assertTrue eq.validate()

		// test out of range values
		eq.zip4 = -1
		assertFalse eq.validate()
		eq.zip4 = 10000
		assertFalse eq.validate()
	}

	void testCountryAndGenderAndAddressAssignable() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')
		eq.country = countryInstance
		assertTrue eq.validate()
		eq.gender = genderMale
		assertTrue eq.validate()
		eq.gender = genderFemale
		assertTrue eq.validate()
		eq.useExistingStreetAddress = streetAddressInstance
		assertTrue eq.validate()
	}

	void testTrackedItemAssignable() {
		def eq = new EligibilityQuestionnaire(userCreated: 'fake user', appCreated: 'fake app')

		eq.trackedItem = trackedItemInstance
		assertTrue eq.validate()
	}
}
