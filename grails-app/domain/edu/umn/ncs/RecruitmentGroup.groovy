package edu.umn.ncs

// Pilot
// HiQ + Incentive
// HiQ + No Incentive
// No HiQ + Incentive
// No HiQ + No Incentive


class RecruitmentGroup {

    // Name of methodology (see above for full list)
    String name
    // The first one will be true
    Boolean pilot
    // These two are only true where pilot = false
    Boolean householdInventoryQuestionnaire
	
    Boolean questionnaireIncentive

    static hasMany = [ recruitmentSegmentGroups: RecruitmentSegmentGroup ]

    String toString() { name }

    static constraints = {
        name(maxSize:64)
    }
}
