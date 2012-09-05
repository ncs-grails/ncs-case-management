package edu.umn.ncs

import grails.test.*

class PhoneNumberTests extends GrailsUnitTestCase {
	def debug = true
	def phoneNumber
	
    protected void setUp() {
        super.setUp()
		// Setup default phone number
		phoneNumber = new PhoneNumber(phoneNumber:'999-999-9999', dateCreated: new Date(), userCreated: 'nobody', appCreated: 'test')
		assert phoneNumber
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreatePhoneNumber() {
    }

	void testCreatePhoneNumberContact() {
		// Create a person to whom phone number will be assigned
		def person = new Person(lastName:'Kenobi', dateCreated: new Date(), userCreated: 'nobody', appCreated: 'test')
		assert person
		
		// Create a phone number type
		def phoneType = new PhoneType(name:'home',allowAddressLink:true)
		assert phoneType
		
		// Create a person phone number
		def personPhone = new PersonPhone(phoneNumber: phoneNumber, phoneType: phoneType)
		assert personPhone
		
		// Add phone number to person
		if (debug) { println "Assigning PersonPhone::$personPhone to Person:: $person" }
		mockDomain(Person, [person])		
		person.addToPhoneNumbers(personPhone)		
		assert person.phoneNumbers.size() == 1
		
	}

	void testPhoneNumberActive() {
		// Create a person to whom phone number will be assigned
		def person = new Person(lastName:'Kenobi', dateCreated: new Date(), userCreated: 'nobody', appCreated: 'test')
		assert person
		
		// Create a phone number type
		def phoneType = new PhoneType(name:'home',allowAddressLink:true)
		assert phoneType
		
		// Create a person phone number
		def personPhone = new PersonPhone(phoneNumber: phoneNumber, phoneType: phoneType)
		assert personPhone
		
		// Add phone number to person
		if (debug) { println "Assigning PersonPhone::$personPhone to Person:: $person" }
		mockDomain(Person, [person])
		person.addToPhoneNumbers(personPhone)
		assert person.phoneNumbers.size() == 1
		
		// Test if person phone is active - should be active
		mockDomain(PersonPhone, [personPhone])
		assert personPhone.active
		
		// Change start date of person phone to be greater than today - makes it inactive
		def startDate = new Date().next()
		personPhone.startDate = startDate
		if (debug) { println "startDate::$startDate" }		
		assert !personPhone.active

		// Reset startDate
		personPhone.startDate = null
		if (debug) { println "startDate::${personPhone.startDate}" }
		
		// Change end date of person phone to be earlier than today - makes it inactive
		def endDate = new Date().previous()
		personPhone.endDate = endDate
		if (debug) { println "endDate::$endDate" }
		assert !personPhone.active

	}

}
