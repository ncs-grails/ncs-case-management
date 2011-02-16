package edu.umn.ncs

class ReportParam {
	String name
	String value
	
	static belongsTo = [ report: Report ]
	
    static constraints = {
    }
}
