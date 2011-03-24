package edu.umn.ncs

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT'])
class PdfController {
	def pdfService
	
    def index = { }
	
	def pdfLink = {
		//TODO: enable PDF link to use new string method in addition to url method
		try{
			byte[] b
			def baseUri = ""
			//def baseUri = request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request)
			if (request.serverName == 'secure.ncs.umn.edu') {
				baseUri = "http://" + request.serverName + grailsAttributes.getApplicationUri(request)      // REQUIRED FOR LIVE SERVER secure.healthstudies.umn.edu
			}
			else {
				baseUri = "http://" + request.serverName + ":8080" + grailsAttributes.getApplicationUri(request)      // REQUIRED FOR LOCAL TESTING
			}
			// println "--------------------------------------------"
			// println "BaseUri is $baseUri"

			if (params.pdfController) {
				// println "GSP - Controller: $params.pdfController , Action: $params.pdfAction, Id: $params.pdfId"
				return
				def content = g.include(controller:params.pdfController, action:params.pdfAction, id:params.pdfId)
				b = pdfService.buildPdfFromString(content.readAsString(), baseUri)
			}
			else {
				def url = baseUri + params.url
				// println "Fetching url $url"
				b = pdfService.buildPdf(url)
			}
			/*
			if (b) {
				println "    ** PDF is built"
			}
			else {
				println "    ** Could NOT create PDF"
			}*/

			response.setContentType("application/pdf")
			//println "    Content type set"
			response.setHeader("Content-disposition", "attachment; filename=" + (params.filename ?: "document.pdf"))
			//println "    Header set"
			response.setContentLength(b.length)
			//println "    Length set"
			response.getOutputStream().write(b)
		}
		catch (Throwable e) {
			println "There was a problem with PDF generation..."
			println "    ERROR: ${e}"
			if (params.pdfController) {
				redirect(controller:params.pdfController, action:params.pdfAction, params:params)
			}
			else {
				redirect(uri:params.url + '?' + request.getQueryString())
			}
		}
	}
}
