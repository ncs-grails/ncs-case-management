package edu.umn.ncs

class DwellingSegment {
	
    // This is used to determine the selection
    // for the initial mailings
	
    // Tie this selection to the address
    DwellingUnit dwellingUnit

    // Tie the segment to the address
    RecruitmentSegment segment

    // Assign a recruitment methodology
    RecruitmentGroup recruitmentGroup

    // This is probably shouldn't be here.
    // Does this address get a pregenancy screener incentive
    Boolean pregnancyScreenerIncentive

    // This is the random selection order chosen by Simone Q. Vuong
    Integer randomizedId  = new Random(System.currentTimeMillis()).nextInt(10000000)

    static constraints = {
        dwellingUnit()
        segmentId()
        recruitmentGroup()
        pregnancyScreenerIncentive()
        randomizedId(unique:true)
        selectionCount()
    }
}
