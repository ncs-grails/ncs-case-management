package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

class MainMenuController {
    @Secured(['ROLE_NCS_DOCGEN'])
    def index = { }
}
