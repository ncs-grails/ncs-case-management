package edu.umn.ncs

class RecruitmentSegment {

    // this is a place to store the percentage of the total population
    // that this segment represents
    Integer selectionCount
    Float populationRatio

    static hasMany = [ recruitmentSegmentGroups: RecruitmentSegmentGroup ]
    
}
