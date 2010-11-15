package edu.umn.ncs

class RecruitmentSegmentGroup {

    RecruitmentGroup recruitmentGroup
    RecruitmentSegment segment
    Integer selectionCount
    Float populationRatio

    static constraints = {
        recruitmentGroup()
        segment()
        selectionCount()
        populationRatio()
    }
}
