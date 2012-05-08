package edu.umn.ncs

import grails.test.*

class ReportServiceIntegrationTests extends GrailsUnitTestCase {
	def debug = true
	def userDetailsService
	def reportService
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCanViewReport() {
		// Get a mock user
		def user = userDetailsService.loadUserByUsername('ast', true)
		assert user != null
		if (debug) {
			println "user::$user"
		}

		def userRoles = user.authorities	
		assert userRoles != null		
		if (debug) {
			println "userRoles::$userRoles"
		}
		// Create a study
		def study = Study.findByName('NCS') ?: new Study(name:'NCS', fullName:'National Childrens Study', active:true, sponsor: "NICHD", coordinator:"bsteward", collaborator:"HS", purpose:"study kids", exclusionary:false, subStudyOf:"someotherstudy").save(flush:true)
		assert study != null
		if (debug) {
			println "study::$study"
		}

		// Create a report with one allowed role
		def report = Report.findByTitle('test') ?: new Report(study:study, title:'test', allowedRoles:'ROLE_NCS_IT').save(flush:true)
		assert report != null
		if (debug) {
			println "report::$report"
		}

		// Test that user is member of report allowed roles
		assert reportService.canViewReport(report)
		
		// Create a second report with a list of roles
		def report2 = Report.findByTitle('test2') ?: new Report(study:study, title:'test2', allowedRoles:'ROLE_NCS_IT,ROLE_NCS_ALL,ROLE_SNARK').save(flush:true)
		assert report2 != null
		if (debug) {
			println "report2::$report2"
		}

		// Test that user is member of report2 allowed roles
		assert reportService.canViewReport(report2)

		// Create a third report with no roles -- default is ROLE_NCS_ADMIN
		def report3 = Report.findByTitle('test3') ?: new Report(study:study, title:'test3').save(flush:true)
		assert report3 != null
		if (debug) {
			println "report3::$report3"
		}

		// Test that user is member of report3 allowed roles
		assert reportService.canViewReport(report3) == false

		// Create a fourth report with roles that should not match
		def report4 = Report.findByTitle('test4') ?: new Report(study:study, title:'test4', allowedRoles:'ROLE_NO_WAY,ROLE_NICE_TRY,ROLE_PUNK').save(flush:true)
		assert report4 != null
		if (debug) {
			println "report4::$report4"
		}

		// Test that user is not a member of report4 allowed roles
		assert reportService.canViewReport(report4) == false

	}
}
