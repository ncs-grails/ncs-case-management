package edu.umn.ncs

class DwellingUnitLink {
	// This class is designed to link internal study classes to external
	// identifiers.

	
	// The dwelling unit this item is tied to
	DwellingUnit dwellingUnit

	// The ID from the Delivery Sequence File
	Integer deliverySequenceId

	// The NORC SU_ID
	String NorcSuId

    static constraints = {
    }
}
