package edu.umn.ncs

import grails.validation.ValidationException

class PersonService {
	def debug = true
	
	static appCreated = 'ncs-case-management'
	
    static transactional = true

	Person makePersonFromEligibiltyQuestionnaire(eligibilityQuestionnaireInstance) {
		def personMap = [
			title : eligibilityQuestionnaireInstance.title?.toLowerCase()?.capitalize(),
			firstName : eligibilityQuestionnaireInstance.firstName?.toLowerCase()?.capitalize(),
			middleName : eligibilityQuestionnaireInstance.middleName?.toLowerCase()?.capitalize(),
			lastName : eligibilityQuestionnaireInstance.lastName?.toLowerCase()?.capitalize(),
			suffix : eligibilityQuestionnaireInstance.suffix?.toLowerCase()?.capitalize(),
			gender : eligibilityQuestionnaireInstance.gender,
			dob : null,
			useExistingStreetAddress : eligibilityQuestionnaireInstance.useExistingStreetAddress,
			address : eligibilityQuestionnaireInstance.address
		]
		
		Person personInstance = makePerson(personMap)
	}	
	
    Person makePerson(personMap) {
		/**
		 * Create a new person in the HS system
		 * from a hash map.
		 */
		def personInstance = null

		log.trace "Looking for Person: ${personMap.lastName}, ${personMap.firstName}"
		
		// first name, last name, street address object match
		if (! personInstance && personMap.useExistingStreetAddress && personMap.firstName){
			def personInstanceList = Person.createCriteria().list {
				and {
					eq("firstName", personMap.firstName)
					eq("lastName", personMap.lastName)
				}
				streetAddresses {
					streetAddress {
						idEq(personMap.useExistingStreetAddress.id)
					}
				}
			}
			personInstanceList.each{
				if ( ! personInstance) { personInstance = it }
			}
		}

		// last name only, street address object match
		if (! personInstance && personMap.useExistingStreetAddress && ! personMap.firstName){
			def personInstanceList = Person.createCriteria().list {
				and {
					isNull("firstName")
					eq("lastName", personMap.lastName)
				}
				streetAddresses {
					streetAddress {
						idEq(personMap.useExistingStreetAddress.id)
					}
				}
			}
			personInstanceList.each{
				if ( ! personInstance) { personInstance = it }
			}
		}

		// first name, last name, entered string address match
		if (! personInstance && personMap.address && personMap.firstName){
			def personInstanceList = Person.createCriteria().list {
				and {
					eq("firstName", personMap.firstName)
					eq("lastName", personMap.lastName)
				}
				streetAddresses {
					streetAddress {
						ilike("address", "%${personMap.address}%")
					}
				}
			}
			personInstanceList.each{
				if ( ! personInstance) { personInstance = it }
			}
		}

		// last name only, entered string address match
		if (! personInstance && personMap.address && ! personMap.firstName){
			def personInstanceList = Person.createCriteria().list {
				and {
					isNull("firstName")
					eq("lastName", personMap.lastName)
				}
				streetAddresses {
					streetAddress {
						ilike("address", "%${personMap.address}%")
					}
				}
			}
			personInstanceList.each{
				if ( ! personInstance) { personInstance = it }
			}
		}

		if (debug && personInstance) { "Person exists::$personInstance" }
		
		// Attempt to create a new person
		if (! personInstance){
			if (debug) { "Creating new person::$personInstance" }
			
			personInstance = new Person(title: personMap.title,
				firstName: personMap.firstName,
				middleName: personMap.middleName,
				lastName: personMap.lastName,
				suffix: personMap.suffix,
				gender: personMap.gender,
				birthDate: personMap.dob,
				appCreated: appCreated,
				isRecruitable: true)
			
			if (! personInstance.save(flush:true)) {
				log.warn "Unable to save person::$personInstance"
				personInstance.errors.each{ println "${it}" }
				throw new ValidationException("Person could not be saved due to validation errors", personInstance.errors) 
			}
		}
		
		return personInstance
    }
	
	void linkPersonToTrackedItem(personInstance, trackedItemInstance) {
		trackedItemInstance.person = personInstance
		if (! trackedItemInstance.save(flush: true)) {
			log.warn "Unable to link Person ${personInstance} and tracked item ${trackedItemInstance}"
		}
		if (debug) { "Linked person::$personInstance to tracked item::$trackedItemInstance" }
	}
	
	void linkPersonToStreetAddress(personInstance, streetAddressInstance, source) {
		// Attempt to link person to street address
		if (personInstance && streetAddressInstance) {
			def personAddressInstance = PersonAddress.findByPersonAndStreetAddress(personInstance, streetAddressInstance)
			
			if ( ! personAddressInstance ) {
				def homeAddress = AddressType.read(1)

				personAddressInstance = new PersonAddress(person:personInstance,
					streetAddress:streetAddressInstance, addressType: homeAddress,
					appCreated: appCreated, source: source,
					infoDate: new Date())
				
				if (personAddressInstance.save(flush:true) ) {
					personInstance.addToStreetAddresses(personAddressInstance)					
					if (debug) { "Linked person::$personInstance to streetAddress::$personAddressInstance" }
				} else {
					println "Failed to Create Person <-> Address Link for ${personInstance.fullName}"
					personAddressInstance.errors.each{ println "\t${it}" }
					throw new ValidationException("PersonAddress could not be saved due to validation errors", personAddressInstance.errors) 
				}
			}
		}
	}

}
