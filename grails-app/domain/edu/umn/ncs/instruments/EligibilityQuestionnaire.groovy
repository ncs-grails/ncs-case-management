package edu.umn.ncs.instruments

import edu.umn.ncs.TrackedItem
import edu.umn.ncs.StreetAddress
import edu.umn.ncs.Gender
import edu.umn.ncs.Country

/** This class is used to store information provided by the NCS Eligibility Quesionnaire.
 */
class EligibilityQuestionnaire {

	/** This is the tracked item of the Eligibility Quesionnaire being entered */
	TrackedItem trackedItem
	/** The title of the person who completed the survey.
	 This field is optional, but has a maximum size of 10 characters. */
	String title
	/** The first name of the person who completed the survey.
	 This field is optional, but has a maximum size of 30 characters. */
	String firstName
	/** The middle name of the person who completed the survey.
	 This field is optional, but has a maximum size of 20 characters. */
	String middleName
	/** The last name or surname of the person who completed the survey.
	 This field is optional, but has a maximum size of 30 characters. */
	String lastName
	/** The suffix, such as Jr, Sr, MD, of the person who completed the survey.
	 This field is optional, but has a maximum size of 10 characters. */
	String suffix

	/** The gender of teh person who completed the survey if available.
	  This field is optional.
	  */
	Gender gender

	/** If set, this is the address of the person who completed the survey.
	This field is optional, but if it is null, the fields {@link address},
	{@link city}, and {@link state} must not be null.
	*/
	StreetAddress useExistingStreetAddress

	/** This is the address of the person who completed the survey.
	This field is required if {@link useExistingStreetAddress} is null.
	This field can be no longer than 64 characters, however the US post office
	accepts address fields no longer than 40 characters.
	*/
	String address
	/** This is the business address of the person who completed the survey.
	This field is optional, and really shouldn't be used for residential addresses.
	This field can be no longer than 64 characters, however the US post office
	accepts address fields no longer than 40 characters.
	*/
	String address2
	/** This is the city of the person who completed the survey.
	This field is required if {@link useExistingStreetAddress} is null.
	This field can be no longer than 30 characters.
	*/
	String city
	/** This is the state abbreviation of the person who completed the survey.
	This field is required if {@link useExistingStreetAddress} is null.
	This field can be no longer than 2 characters.
	*/
	String state
	/** this is the zip code of the person who completed the survey.
	This field is optional, but can be no longer than a 5 digits.
	*/
	Integer zipCode
	/** This is the zip+4 code of the person who completed the survey.
	This field should not be entered by a person, but should be computed by
	some kind of address standardization software.
	This field is optional, but can be no longer than a 4 digits.
	*/
	Integer zip4
	/** this is the international postal code of the person who completed the survey.
	This field is optional, and should only be used for Non-US addresses.
	This field can not be any longer than 16 characters.
	*/
	String internationalPostalCode 
	/** this is the country of the person who completed the survey.
	This field is optional.
	*/
	Country country
	/** This field flags whether or not the address was standardized. */
	boolean standardized = false
	/** This field flags whether or not the address . */
	boolean uspsDeliverable = true

	/** This is the date the record was created. */
	Date dateCreated = new Date()
	/** This is the username of the person who created the record. */
	String userCreated
	/** This is the name of the application that created the record. */
	String appCreated
	/** If this record is updated, this should be set to the date the record was last updated */
	Date lastUpdated
	/** If this record is updated, this should be set to the user who updated the record */
	String userUpdated

	/** This validator ensures that either {@link useExistingStreetAddress} is set, or an address was entered. */
	static mustHaveAnAddressValidator = { val, obj ->
		if (
			(obj.properties['useExistingStreetAddress'] != null)
			|| ( 
				obj.properties['address']
				&& obj.properties['city']
				&& obj.properties['state'] )
			) {
			return true
		} else {
			return 'eligibilityQuestionnaire.address.required'
		}
	}

	/** these are the constraint definitions. */
    static constraints = {
		trackedItem(unique:true, nullable:true)
		title(nullable:true, maxSize:10)
		firstName(maxSize:30)
		middleName(nullable:true, maxSize:20)
		lastName(maxSize:30)
		suffix(nullable:true, maxSize:10)
		gender(nullable:true)
		useExistingStreetAddress(nullable:true, validator: mustHaveAnAddressValidator)
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
