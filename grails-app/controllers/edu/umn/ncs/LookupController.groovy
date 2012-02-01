package edu.umn.ncs

// Let's us use security annotations
import grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_LOOKUP'])
class LookupController {

	def searchService

	// default search form
	
	// Turn this off in production!!!
	@CacheFlush("lookupCache")
    def index = { }

	// default search results, ether by include from index, or by AJAX call
	@Cacheable("lookupCache")
	def find = {

		//println "LookupController:find:params::${params}"
		// 0061674400

		def results = []
		String searchString = params.id
		if ( ! searchString ) {
			searchString = params.value
		}

		if ( ! searchString ) {
			flash.message = "Please enter some search parameters."
			//println "LookupController:find:Nothing Passed."
		} else {

			//println "LookupController:find:searchString::${searchString}"

			// now we begin our searching...
			results = searchService.query(searchString)
		}

		[ searchString:searchString, results: results ]
	}

}
