package edu.umn.ncs

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;

import grails.test.*

class PersonServiceIntegrationTests extends GrailsUnitTestCase {
	def personService
	def debug = true
	def person
	
    protected void setUp() {
        super.setUp()
		validPerson()
    }

	protected void validPerson() {
		// map for a valid person object
		person = [
			title: 'Mr',
			firstName: 'John',
			lastName: 'Doe'			
			]
	}

	protected void invalidPerson() {
		// map for an invalid person object
		person = [
			title: 'Mrs',
			firstName: 'DK',
			lastName: 'unknown'
			]
	}
	
    protected void tearDown() {
        super.tearDown()
    }

    void testMakePerson() {
		Person personInstance = personService.makePerson(person)
		
		assert personInstance
		
		if (debug) { println "Created person::$personInstance" }
		
		// Test creating an invalid person
		invalidPerson()
		personInstance = personService.makePerson(person)
		
		assert ! personInstance
		
		if (debug) { println "Could not create person::$personInstance" }
		
    }
	
	void testCreateStreetAddress() {
		def streetAddressInstance = new StreetAddress(address:"123 Main St",city:"anywhere",state:"MN",zipCode:99999,appCreated:"test-app")
		assert streetAddressInstance
		if (debug) { println "Created streetAddress::$streetAddressInstance.id" }
		
	}

	void testLinkPersonAndStreetAddress() {
		Person personInstance = personService.makePerson(person)
		
		assert personInstance
		
		if (debug) { println "Created person::$personInstance" }

		def streetAddressInstance = new StreetAddress(address:"123 Main St",city:"anywhere",state:"MN",zipCode:99999,appCreated:"test-app")
		assert streetAddressInstance
		if (debug) { println "Created streetAddress::$streetAddressInstance.id" }
		
		def source = "eligibility-questionnaire"
		def appCreated = "test-app"
		def homeAddress = new AddressType(name:"home")
		def personAddressInstance = new PersonAddress(person:personInstance,
					streetAddress:streetAddressInstance, addressType: homeAddress,
					appCreated: appCreated, source: source,
					infoDate: new Date())
		
		assert personAddressInstance
		if (debug) { println "Created personAddress::$personAddressInstance.id" }
		
		personInstance.addToStreetAddresses(personAddressInstance)
		assert personInstance.streetAddresses.size() == 1
		
	}

}
