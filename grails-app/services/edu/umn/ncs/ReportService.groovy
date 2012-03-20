package edu.umn.ncs

class ReportService {

    static transactional = true

	def springSecurityService

    def canViewReport(Report report) {

		// TODO: get user's roles from springSecurityService
		def userRoles = []
		
		// TODO: get roles allowed from Report
		def rolesAllowed = []

		// TODO: if user has role required, or report requires no roles, return true
		return true

		// TODO: else return false
		// else { return false }
    }
}
