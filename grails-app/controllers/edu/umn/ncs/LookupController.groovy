package edu.umn.ncs

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

def searchService

@Secured(['ROLE_NCS_LOOKUP'])
class LookupController {

	// default search form
    def index = { }

	// default search results, ether by include from index, or by AJAX call
	def find = {

		def results = []
		String searchString = params.id

		if ( ! searchString ) {
			flash.message = "Please enter some search parameters."
		} else {
			
			// now we begin our searching...
			results = searchService.query(searchString)
			
		}

		[ searchString:searchString, results: results ]
	}


	// these might be better off in the dwellingUnit Controller
	def showDwellingUnit = {}

	def showPerson = {}

}
