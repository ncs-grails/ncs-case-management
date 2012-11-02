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
		 * from a hash map. Requires the following
		 * keys at a minimum and the lastName
		 * key must have a value and not be unknown/DK:
		 * title, firstName, middleName,
		 * lastName, suffix, gender, dob, useExistingStreetAddress,
		 * address.
		 */
		def personInstance = null

		log.trace "Looking for Person: ${personMap.lastName}, ${personMap.firstName}"
		
		if (personMap.lastName && personMap.lastName != 'DK' && personMap.lastName != 'unknown') {
		
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

	def multipleContactRecords(String type) {
		/**
		 * Return list of people with multiple contact records
		 * filtered by type.
		 * Parameters: type [address|phone|email]
		 */
		// TODO: Filter results with a time parameter (e.g. new contact info added in last x days)
		def results = []
		switch (type) {
			case "address":
				results = Person.list().findAll{ it.streetAddresses?.size() > 1 }
				break
			case "phone":
				results = Person.list().findAll{ it.phoneNumbers?.size() > 1 }
				break
			case "email":
				results = Person.list().findAll{ it.emailAddresses?.size() > 1 }
				break
			default:
				results = null
		}
		if (results) {
			results = results.sort{ it.id }
		}
		return results
	}

    def personContactRecords(String type, Person personInstance) {
        /**
         * Return person contact records for a specific person
         * filtered by type.
         * @param type [address|phone|email]
         * @param personInstance
         * @return results (list of person contact records)
         */
        def results = []
        switch (type) {
            case "address":
                results = personInstance.streetAddresses?.sort{ a,b -> b.dateCreated <=> a.dateCreated }
                break
            case "phone":
                results = personInstance.phoneNumbers?.sort{ a,b -> b.dateCreated <=> a.dateCreated }
                break
            case "email":
                results = personInstance.emailAddresses?.sort{ a,b -> b.dateCreated <=> a.dateCreated }
                break
            default:
                results = null
        }
        return results
    }

    def contactInfoRecord(String type, Long id) {
		def result = null
		switch (type) {
			case "address":
				result = PersonAddress.get(id)
				break
			case "phone":
				result = PersonPhone.get(id)
				break
			case "email":
				result = PersonEmail.get(id)
				break
			default:
				result = null
		}
		return result
	}
	
	Boolean updateContactInfoRecord(contactInfoInstance, String endDate) {
        // Make contact info active/inactive based on presence of an end date
        if (debug) {
            println "Active status is $contactInfoInstance.active but an end date has been entered. Updating..."
        }
        if (endDate) {
            try {
                if (debug) {
                    println "    setting end date to::$endDate"
                }
                contactInfoInstance.endDate = new Date(endDate)
            } catch (e) {
                return false
            }
        } else {
            contactInfoInstance.endDate = null
        }
		return true
	}
}
