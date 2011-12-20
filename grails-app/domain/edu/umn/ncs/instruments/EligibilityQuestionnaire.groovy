package edu.umn.ncs.instruments

import edu.umn.ncs.TrackedItem
import edu.umn.ncs.StreetAddress
import edu.umn.ncs.Gender
import edu.umn.ncs.Country

/** This class is used to store information provided by the NCS Eligibility Quesionnaire.
 */
class EligibilityQuestionnaire {

	TrackedItem trackedItem
	String title
	String firstName
	String middleName
	String lastName
	String suffix

	Gender gender

	StreetAddress useExistingStreetAddress

	String address
	String address2
	String city
	String state
	Integer zipCode
	Integer zip4
	String internationalPostalCode 
	Country country
	boolean standardized = false
	boolean uspsDeliverable = true

	Date dateCreated = new Date()
	String userCreated
	String appCreated
	Date lastUpdated
	String userUpdated

    static constraints = {
		trackedItem(nullable:true)
		title(nullable:true, maxSize:10)
		firstName(nullable:true, maxSize:30)
		middleName(nullable:true, maxSize:20)
		lastName(nullable:true, maxSize:30)
		suffix(nullable:true, maxSize:10)
		gender(nullable:true)
		useExistingStreetAddress(nullable:true)
		address(nullable:true, maxSize:64)
		address2(nullable:true, maxSize:64)
		city(nullable:true, maxSize:30)
		state(nullable:true, maxSize:2)
		zipCode(nullable:true, range:0..99999)
		zip4(nullable:true, range:0..9999)
		internationalPostalCode(nullable:true, maxSize:16)
		country(nullable:true)
		standardized()
		uspsDeliverable()
		dateCreated(visible:false)
		userCreated(visible:false)
		appCreated(visible:false)
		lastUpdated(nullable:true, visible:false)
		userUpdated(nullable:true, visible:false)
    }
}
