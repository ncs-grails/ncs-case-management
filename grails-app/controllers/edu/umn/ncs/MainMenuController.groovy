package edu.umn.ncs
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS'])
class MainMenuController {

    def springSecurityService

    def index = { }

    def whatRolesDoIHave = {
        def principal = springSecurityService.principal
        def username = principal.getUsername()//get username
        def roles = principal.getAuthorities()//get authorities
        [ username: username, roles: roles ]
    }
	
	def indexDev = { }
	
}
