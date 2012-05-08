package edu.umn.ncs

class ReportService {
	//def userDetailsService	
	def springSecurityService
	def directoryService
	def userRoles
    static transactional = true

    def canViewReport(Report report) {
		// Get authenticated user
		def username = springSecurityService.principal.getUsername()
		// Get user's roles from springSecurityService
		userRoles = directoryService.getAuthorities()
		if (! userRoles) {
			directoryService.loadUserByUsername(username)
			userRoles = directoryService.getAuthorities()
		}
		//def userRoles = springSecurityService.principal.authorities
		//def user = userDetailsService.loadUserByUsername('ast', true)
		//def userRoles = user.authorities
		
		// Get roles allowed from Report
		def rolesAllowed = []
		if (report.allowedRoles.contains(',')) {
			report.allowedRoles.split(',').collect(rolesAllowed){ it }			
		} else {
			if (report.allowedRoles) {
				rolesAllowed << report.allowedRoles				
			}
		}

		// Determine if user has role required, or report requires no roles, return true
		if (! rolesAllowed) {
			return true
		} else {
			if ( userRoles.intersect(rolesAllowed)) {
				return true
			}
		}
		return false
    }
}
