package edu.umn.ncs

import java.util.Date;

/** This class holds metadata for web reports available to the system */
class Report {
	/** The main title of the report */
    String title
	/** a sub-title if needed */
    String subtitle
	/** a longer description of what the report provides */
    String description
	/** if the report is an SQL query, then this is the SQL code */
    String query
	/** ??? */
	String designedName
	/** Flags whether or not to use the SQL contained in the query attribute to generate the report */
	Boolean useQuery = false
	/** Flags whether or not this report is enabled */
	Boolean enabled = false
	/** Flags whether or not this report is under construction */
	Boolean underConstruction = true
	/** Flags whether or not this report is for admin use only.
	 *  TODO: Replace this with the 'allowedRoles' attribute
	 */
	Boolean adminsOnly = false
	/** A comma delimited string listing the roles allowed to view this report.
	 *  If this is null, anyone with access to the system can see the report.
	 */
	String allowedRoles = 'ROLE_NCS_ADMIN'
	
	/** The study associated with this report */
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
		allowedRoles(nullable:true)
    }
}
