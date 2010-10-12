package edu.umn.ncs

// Pilot
// HiQ + Incentive
// HiQ + No Incentive
// No HiQ + Incentive
// No HiQ + No Incentive


class RecruitmentMethodology {

	String name
	Boolean pilot
	Boolean highIntensityQuestionnaire
	Boolean questionnaireIncentive

	String toString() { name }

    static constraints = {
    }
}
