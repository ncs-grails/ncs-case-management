package edu.umn.ncs

import java.io.File;

import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT','ROLE_NCS_REPORTS'])
class ReportController {
    def dataSource      // inject the Spring-Bean dataSource
	def birtReportService
	
    def index = { 
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [reportInstanceList: Report.list(params), reportInstanceTotal: Report.count()]
    }

	def create = {
		def reportInstance = new Report()
		reportInstance.properties = params
		
		// Populate list of empty report parameters
		def reportParamInstance = null
		def reportParamInstanceList = []
		for (i in 0..9) {
			reportParamInstance = new ReportParam()
			reportParamInstanceList << reportParamInstance
		}
		
		// Get list of designed BIRT reports
		def reportList = getDesignedReports().collect { it.name }

		return [reportInstance: reportInstance, reportList: reportList, reportParamInstanceList: reportParamInstanceList ]
	}
	
	def save = {
		def reportInstance = new Report(params)
		
		if (!reportInstance?.useQuery){
			// Handle uploaded file
			def uploadedFile = request.getFile('reportFile')
			if (!uploadedFile.empty) {
				//println "Class: ${uploadedFile.class}"
				//println "Name: ${uploadedFile.name}"
				//println "Size: ${uploadedFile.size}"
				//println "ContentType: ${uploadedFile.contentType}"
				// Upload the file to server
				def newFile = "/var/lib/webreports/${uploadedFile.originalFilename}"
				uploadedFile.transferTo ( new File(newFile))
				// Set the designed report name
				def designedName = uploadedFile.originalFilename.split("\\.")[0]
				//println "    Designed report name is ${designedName}"
				reportInstance.designedName = designedName				
			}
		}
		
		if (reportInstance.save(flush: true)) {
			flash.message = "Report Created"
			redirect(action: "show", id: reportInstance.id)
		}
		else {
			render(view: "create", model: [reportInstance: reportInstance])
		}
	}
	
	def show = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			[reportInstance: reportInstance]
		}
    }
	
	def showReport = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			if (reportInstance.useQuery) {
				redirect(action: "showReportByQuery", id: reportInstance.id)
			}
			else {
				redirect(action: "birtReport", id: reportInstance.id)
			}
		}
	}
	
    def showReportByQuery = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			def db = new Sql(dataSource)
			String query = reportInstance?.query
			//println "QUERY:  << ${query} >>"
			def results = db.rows(query)
	
			def headerList = []
			if (results) {
				headerList = results[0].collect { it.key }
			}
	
			[ reportInstance: reportInstance, headerList: headerList, recordList: results ]

			/*def query = """
                SELECT b.id AS 'Batch',
                    i.name AS Instrument,
                    DATE_FORMAT(b.date_created, '%m/%d/%Y') AS 'Date Created',
                    ds.segment_id AS Segment,
                    rsg.recruitment_group_id AS 'Group',
                    rg.name AS 'Group Name',
                    rsg.population_ratio AS 'Group Ratio',
                    COUNT(ti.dwelling_unit_id) AS Items,
                    tot.total_items AS 'Batch Size',
                    COUNT(ti.dwelling_unit_id) / tot.total_items AS 'Ratio'
                FROM batch b INNER JOIN
                    batch_instrument bi ON bi.batch_id = b.id INNER JOIN
                    instrument i ON i.id = bi.instrument_id INNER JOIN
                    tracked_item ti ON b.id = ti.batch_id INNER JOIN
                    dwelling_segment ds ON ds.dwelling_unit_id = ti.dwelling_unit_id
                INNER JOIN
                    recruitment_group rg ON ds.recruitment_group_id = rg.id INNER JOIN
                    recruitment_segment rs ON ds.segment_id = rs.id INNER JOIN
                    recruitment_segment_group rsg ON rsg.segment_id = rs.id AND
                    ds.recruitment_group_id = rsg.recruitment_group_id INNER JOIN
                    (
                            SELECT COUNT(ti.dwelling_unit_id) AS total_items,
                                    b.id AS batch_id,
                                    i.id AS instrument_id,
                                    rg.id AS group_id
                            FROM batch b INNER JOIN
                                    batch_instrument bi ON bi.batch_id = b.id INNER JOIN
                                    instrument i ON i.id = bi.instrument_id INNER JOIN
                                    tracked_item ti ON b.id = ti.batch_id INNER JOIN
                                    dwelling_segment ds ON ds.dwelling_unit_id = ti.dwelling_unit_id INNER JOIN
                                    recruitment_group rg ON ds.recruitment_group_id = rg.id
                            GROUP BY b.id,
                                    i.id,
                                    rg.id
                    ) tot ON tot.batch_id = b.id AND tot.instrument_id = i.id AND tot.group_id = rg.id
                GROUP BY b.id,
                    b.date_created,
                    i.name,
                    rs.population_ratio,
                    rsg.recruitment_group_id,
                    rsg.population_ratio,
                    rg.name
                ORDER BY b.id, ds.segment_id
            """*/
		}
    }
	
	def exportReportByQueryToFile = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			def db = new Sql(dataSource)
			String query = reportInstance?.query
			//println "QUERY:  << ${query} >>"
			def results = db.rows(query)
	
			// Get the column headers from keys
			def headerList = []
			if (results) {
				headerList = results[0].collect { it.key }
			}
			
			String reportName = reportInstance.title.replaceAll(" ","_")
			def type = params['format']
			// println "File type: ${type}"
			String reportExt = ""
			
			// CSV Generation
			if (type == 'csv') {
				// println "Generating CSV..."
				reportExt = type
				response.setHeader("Content-disposition", "attachment; filename=${reportName}.${reportExt}")
				response.contentType = "text/csv"
				def outs = response.outputStream
				def dataRow = []
				// Add the column headers 
				outs << headerList.join(";")
				outs << "\n"
				// Add the data
				results.each { row ->
					dataRow = row.collect { it.value }
					// println "        Data Row: ${dataRow}"
					outs <<	dataRow.join(";")
					outs << "\n"
				}
				outs.flush()
				outs.close()
			}
			
		}
    }
	
	def edit = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			// Populate list of empty report parameters
			def reportParamInstance = null
			def reportParamInstanceList = []
			for (i in 0..9) {
				reportParamInstance = new ReportParam()
				reportParamInstanceList << reportParamInstance
			}
			
			// Get list of designed BIRT reports
			def reportList = getDesignedReports().collect { it.name }
	
			return [reportInstance: reportInstance, reportList: reportList, reportParamInstanceList: reportParamInstanceList ]
		}
	}

	def update = {
		def reportInstance = Report.get(params.id)
		if (reportInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (reportInstance.version > version) {
					
					reportInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'report.label', default: 'Report')] as Object[], "Another user has updated this Report while you were editing")
					render(view: "edit", model: [reportInstance: reportInstance])
					return
				}
			}
			reportInstance.properties = params
		
			if (!reportInstance?.useQuery){
				// Handle uploaded file
				def uploadedFile = request.getFile('reportFile')
				if (!uploadedFile.empty) {
					// Upload the file to server
					def newFile = "/var/lib/webreports/${uploadedFile.originalFilename}"
					// Check to see if file exists
					/*File src = new File(newFile)
					if (src.exists()) {
						reportInstance.errors.rejectValue("designedName", "default.designed.report.name.exists", [message(code: 'report.label', default: 'Report')] as Object[], "A designed report with this name already exists on the server. Please change the name of the report.")
						render(view: "edit", model: [reportInstance: reportInstance])
						return
					}
					else {*/
						uploadedFile.transferTo ( new File(newFile))
						// Set the designed report name
						def designedName = uploadedFile.originalFilename.split("\\.")[0]
						//println "    Designed report name is ${designedName}"
						reportInstance.designedName = designedName
					//}
				}
			}

			if (!reportInstance.hasErrors() && reportInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'report.label', default: 'Report'), reportInstance.id])}"
				redirect(action: "show", id: reportInstance.id)
			}
			else {
				render(view: "edit", model: [reportInstance: reportInstance])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
	}

	def delete = {
		def reportInstance = Report.get(params.id)
		if (reportInstance) {
			try {
				// Check for designed report and delete if found
				def designedName = reportInstance.designedName
				reportInstance.delete(flush: true)
				if (designedName) {
					def f = new File("/var/lib/webreports/${designedName}.rptdesign")
					if (f) {
						f.delete()
					}
				}

				flash.message = "Report deleted"
				redirect(action: "list")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
				redirect(action: "show", id: params.id)
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
	}

	
	def birtReport = {
		//println "CURRENT PARAMS: ${params}"
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			String reportName = reportInstance.designedName
			//println "Report name is ${reportName}"
			//def reportParam = params.reportParam
			params.remove('id')
			params.remove('action')
			params.remove('controller')
			params.remove('name')
			
			def reportParams = birtReportService.getReportParams(reportName)			
			if (reportParams) {
				redirect(action: "birtReportParams", id: reportInstance.id)
			}
			else {
				// def options = birtReportService.getRenderOption(request, 'html')
				// def result = birtReportService.runAndRender(reportName, params, options)
				// render(contentType:"text/html", text:"${result}")	
				redirect(action: "showBirtReport", id: reportInstance.id)
			}
		}
    }
	
	def birtReportParams = {
		def reportInstance = Report.get(params.id)
		String reportName = reportInstance.designedName
		def reportParams = birtReportService.getReportParams(reportName)
		
		if (reportInstance) {
			def format = params.format
			return [reportInstance: reportInstance, reportParams: reportParams, format:format ]				
		}
		else {
			flash.message = "Sorry, report not found."
			redirect(action: "list")
		}		
	}	
	
	def showBirtReport = {
		def reportInstance = Report.get(params.id)
		def format = params.format
		// println "Output format ${format}"
		String reportName = reportInstance.designedName
		// def reportParams = birtReportService.getReportParams(reportName)
		/* if (reportParams) {
			 println "Report Parameters: ${reportParams}"
			reportParams.each {
				println "Param name: ${it.name}"
				println "Param promptText: ${it.promptText}"
				println "List Entries:"
				it.listEntries.each { e ->
					println "    ${e.label} with value = ${e.value}"
				}
			} 
			params.remove('showBirtReport')
		} */
		params.remove('showBirtReport')
		params.remove('id')
		params.remove('action')
		params.remove('controller')
		// println "Report params for ${reportName} are ${params}"
		def options = birtReportService.getRenderOption(request, 'html')
		try {
			def result = birtReportService.runAndRender(reportName, params, options)
			render(contentType:"text/html", text:"${result}")
		}
		catch (Exception ex) {
			flash.message = "Sorry, ${reportName} report could not be loaded. ERROR: ${ex}"
			redirect(action: "list")
		}
    }
	
	def exportBirtReport = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			String reportName = reportInstance.designedName
			def reportParams = birtReportService.getReportParams(reportName)			
			if (reportParams) {
				redirect(action: "birtReportParams", id: reportInstance.id, params:[format:params.format])
			}
			else {				
				redirect(action: "exportBirtReportToFile", id: reportInstance.id, params:[format:params.format])
			}
		}
    }
	
	def exportBirtReportToFile = {
		def reportInstance = Report.get(params.id)
		if (!reportInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), params.id])}"
			redirect(action: "list")
		}
		else {
			String reportName = reportInstance.designedName
			// def reportParams = birtReportService.getReportParams(reportName)			
			params.remove('showBirtReport')
			params.remove('exportBirtReportToFile')
			params.remove('id')
			params.remove('action')
			params.remove('controller')
			def type = params['format']
			// println "File type: ${type}"
			String reportExt = ""
			def paramName = null
			def paramOptions = []
			def paramValue = null
			def paramType = null
			// println "Report params: ${params}"
			// PDF Generation
			if (type == 'pdf') {
				// println "Generating PDF..."
				// Concatenate selected parameter values to export file name
				def reportParams = birtReportService.getReportParams(reportName)		// Get BIRT report parameters
				def fileName = reportName
				if (reportParams) {
					// println "    BIRT Parameters ${reportParams}"
					reportParams.each {
						paramName = it.name
						paramType = it.type
						// println "        Current BIRT Parameter ${paramName} of type ${paramType}"
						// println "        --Options: ${paramOptions}"
						// Get selected value for this parameter
						paramValue = getConvertedValue(params["${paramName}"],paramType)
						// println "        Selected value is: ${paramValue}"
						// Get parameter value/label pairs
						paramOptions = it.listEntries
						
						paramOptions.each { m ->
							// println "        Comparing entry value " + m["value"] + " to selected value ${paramValue}"
							if (m["value"] == paramValue) {
								fileName += "-" + m["label"].replaceAll(' ', '_').toLowerCase()
								// println "    Updating filename to ${fileName}" 
							}
						}
					}
				}
	
				reportExt = type
				def options = birtReportService.getRenderOption(request, 'pdf')
				try {
					def result = birtReportService.runAndRender(reportName, params, options)					
					response.setHeader("Content-disposition", "attachment; filename=" + fileName + "." + reportExt)
					response.contentType = 'application/pdf'
					response.outputStream << result.toByteArray()
					response.outputStream.flush()
				}
				catch (Exception ex) {
					flash.message = "Sorry, ${reportName} report could not be loaded. ERROR: ${ex}"
					redirect(action: "list")
				}
			}
			
			// Excel Generation
			if (type == 'xls') {
				// println "Generating Excel..."
				// reportExt = type
				reportExt = 'xml'
				def options = birtReportService.getRenderOption(request, 'xls')
				try {
					def result = birtReportService.runAndRender(reportName, params, options)					
					response.setHeader("Content-disposition", "attachment; filename=" + reportName + "." + reportExt)
					response.contentType = 'application/xls'
					response.outputStream << result.toByteArray()
					response.outputStream.flush()
				}
				catch (Exception ex) {
					flash.message = "Sorry, ${reportName} report could not be loaded. ERROR: ${ex}"
					redirect(action: "list")
				}
			}
		}
		return false				
	}

	private def getDesignedReports = {
		return birtReportService.listReports()
	}
	
	def uploadFile = {
		//println "PARAMS  ${params}"
		def f = request.getFile('reportFile')
		//println "Report file ${f}"
		if (!f.empty) {
			def newFile = "/var/lib/webreports/${f.name}"
			f.transferTo ( new File(newFile))
			flash.message = "File uploaded"
		}
		else {
			flash.message = "File not found"
		}
		return null
	}
	
	def getConvertedValue = { val, type ->
		def convertedValue = null
		switch ( type ) {
			case 1:			// Boolean
				convertedValue = val.toBoolean()
				break
			case 2:			// Date
				convertedValue = new Date().parse("yyyy-MM-dd hh:mm:ss", val)
				break
			case 3:			// Datetime
				convertedValue = new Date().parse("yyyy-MM-dd hh:mm:ss", val)
				break
			case 4:			// Decimal
				convertedValue = val.toBigDecimal()
				break
			case 5:			// Float
				convertedValue = val.toFloat()
				break
			case 6:			// Integer
				convertedValue = val.toInteger()
				break

			default:		// String
				convertedValue = val
		}
    }
	
}
