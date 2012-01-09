package edu.umn.ncs

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import edu.umn.ncs.Person
import edu.umn.ncs.PersonContact
import edu.umn.ncs.PersonAddress
import edu.umn.ncs.Source
import edu.umn.ncs.Country

@Secured(['ROLE_NCS_PROTECTED'])
class DemogController {

    def index = {}

	def search = {
		def id = params?.id ?: params?.value
		def personInstance = Person.read(id)
		[personInstance: personInstance]
	}

}
