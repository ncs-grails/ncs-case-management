package edu.umn.ncs
import groovy.sql.Sql

class LocalNcsPdfController {
	def dataSource      // inject the Spring-Bean dataSource
	
    def index = { }
	
	def exportReportByQueryToPdf = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(controller:"report", action: "list")
		}
		else {
			def db = new Sql(dataSource)
			String query = reportInstance?.query
			//println "QUERY:  << ${query} >>"
			def results = db.rows(query)
	
			def headerList = []
			def columnList = []
			if (results) {
				columnList = results[0].collect { it.key }
				headerList = results[0].collect { col ->
					col.key.replaceAll('_', ' ').replaceAll(/\w+/, { it[0].toUpperCase() + ((it.size() > 1) ? it[1..-1] : '') })
				}
			}
	
			[ reportInstance: reportInstance, headerList: headerList, columnList: columnList, recordList: results ]
		}
	}
}
