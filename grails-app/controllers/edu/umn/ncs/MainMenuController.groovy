package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS'])
class MainMenuController {

    def authenticateService

    def index = { }

    def whatRolesDoIHave = {
        def principal = authenticateService.principal()
        def username = principal.getUsername()//get username
        def roles = principal.getAuthorities()//get authorities
        [ username: username, roles: roles ]
    }
}
