package edu.umn.ncs

import java.util.Date;

class Report {
    String title
    String subtitle
    String description
    String query
	String designedName
	Boolean useQuery = false
	Boolean enabled = false
	Boolean underConstruction = true
	Boolean adminsOnly = false
	
	Study study
	
    // Provenance data
    Date dateCreated
    String userCreated = "report-designer"
    Date lastUpdated
    String userUpdated  = "report-designer"

	static hasMany = [ reportParams: ReportParam ]
	
    static constraints = {
		title(unique:true)
        subtitle(nullable:true)
        designedName(nullable:true)
        query(maxSize:4000,nullable:true)
        description(maxSize:2000,nullable:true)
        dateCreated(display:false)
        lastUpdated(display:false)
    }
}
