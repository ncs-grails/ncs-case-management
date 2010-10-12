package edu.umn.ncs

class DwellingSegment {
	
	// This is used to determine the selection
	// for the initial mailings
	
	// Tie this selection to the address
	DwellingUnit dwellingUnit
	// Tie the segment to the address
	Integer segmentId
	// Assigne a recruitment methodology
	RecruitmentMethodology recruitmentMethodology
	
	// This is probably shouldn't be here.
	// Does this address get a pregenancy screener incentive
	Boolean pregnancyScreenerIncentive

	// This is the random selection order chosen by Simone Q. Vuong
	Double randOrder

    static constraints = {
		dwellingUnit()
		segmentId()
		recruitmentMethodology()
		pregnancyScreenerIncentive()
		randOrder()
    }
}
