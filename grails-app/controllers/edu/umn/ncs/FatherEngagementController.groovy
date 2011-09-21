package edu.umn.ncs

import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

// Let's us use security annotations
import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT','ROLE_NCS'])
class FatherEngagementController {
    def fatherEngagementDataBuilderService
	def debug = false
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [fatherEngagementInstanceList: FatherEngagement.list(params), fatherEngagementInstanceTotal: FatherEngagement.count()]
    }

    def create = {
        def fatherEngagementInstance = new FatherEngagement()
        fatherEngagementInstance.properties = params
        return [fatherEngagementInstance: fatherEngagementInstance]
    }

    def save = {
		if (params.trackedItem) {
			// Get the tracked item
			def trackedItemInstance = TrackedItem.read(params.trackedItem.toLong())
			if (trackedItemInstance) {
				params.trackedItem = trackedItemInstance
				
				def fatherEngagementInstance = new FatherEngagement(params)
				
				def interviewDate = null
				if (params.interviewStartTime) {
					interviewDate = params.interviewStartTime
				}
				fatherEngagementInstance.interviewDate = interviewDate
				
				if (fatherEngagementInstance.save(flush: true)) {
					flash.message = "${message(code: 'default.created.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), fatherEngagementInstance.id])}"
					redirect(action: "show", id: fatherEngagementInstance.id)
				}
				else {
					render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance])
				}		
			}
			else {
				flash.message = "Tracked item ${params.trackedItem} not found."
				redirect(action: "list")	
			}
		}		
        else {
            flash.message = "No tracked item specified."
            redirect(action: "list")
        }
    }

    def show = {
        def fatherEngagementInstance = FatherEngagement.get(params.id)
        if (!fatherEngagementInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), params.id])}"
            redirect(action: "list")
        }
        else {
            [fatherEngagementInstance: fatherEngagementInstance]
        }
    }

    def edit = {
        def fatherEngagementInstance = FatherEngagement.get(params.id)
        if (!fatherEngagementInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [fatherEngagementInstance: fatherEngagementInstance]
        }
    }

    def update = {
        def fatherEngagementInstance = FatherEngagement.get(params.id)
        if (fatherEngagementInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (fatherEngagementInstance.version > version) {
                    
                    fatherEngagementInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'fatherEngagement.label', default: 'FatherEngagement')] as Object[], "Another user has updated this FatherEngagement while you were editing")
                    render(view: "edit", model: [fatherEngagementInstance: fatherEngagementInstance])
                    return
                }
            }
            fatherEngagementInstance.properties = params
            if (!fatherEngagementInstance.hasErrors() && fatherEngagementInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), fatherEngagementInstance.id])}"
                redirect(action: "show", id: fatherEngagementInstance.id)
            }
            else {
                render(view: "edit", model: [fatherEngagementInstance: fatherEngagementInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def fatherEngagementInstance = FatherEngagement.get(params.id)
        if (fatherEngagementInstance) {
            try {
                fatherEngagementInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'fatherEngagement.label', default: 'FatherEngagement'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def getPersonInfo = {
		if (debug) {
			println "Getting person info..."
		}
		
		// Delay Code, used to test out of sequence responses
		/*
		def sleepTime = rand.nextInt(3000)
		print "Waiting...${sleepTime}"
		sleep(sleepTime)
		println "...Done."
		 */

		// prep all the things we'll need to send back
		def result = [
			success: false,
			trackedItemId: "",
			divId: 0,
			person: null,
			fullName: null,
			resultName: false,
			errorText: ""
		]

		// if a div ID was passed, let's save it to the result set
		if (params?.divId) {
			result.divId = params.divId
		}

		if (debug) {
			println "params::trackedItem::${params?.trackedItemInstance}"				
		}
		if (params?.id){
			def barcodeValue = params.id
			// Check if it has I infront. If yes remove the "I" and proceed
			def id = null
            if (barcodeValue[0].toUpperCase() == "I") {
                id = barcodeValue.replace("I", "")
            }
			else {
				id = params.id
			}
			def trackedItemInstance = TrackedItem.read(id.toLong())
			if (debug) {
				println "trackedItem::${trackedItemInstance}"				
			}
			if (trackedItemInstance) {
				// we have an item
				result.success = true
	
				result.trackedItemId = trackedItemInstance.id
				result.person = trackedItemInstance?.person
				result.fullName = trackedItemInstance?.person.fullName
				result.resultName = "Found tracked item"

			} else {
				result.errorText = "Tracked Item does not exist!"
			}
		} else {
			// invalid tracked item!
			result.errorText = "Invalid tracked item id"
		}

		render result as JSON

	}
	
	// this sends a CSV file to the user on the other end
	def downloadDataset = {
		if (debug) {
			println "downloadDataset..."			
		}
		def dataSourceContents = new StringBuffer()
		
		def fatherEngagementList = FatherEngagement.list()

		// Check for father engagement data
		if (fatherEngagementList) {

			// the file name... we should trim off the file name from the end of
			// the path.  (q:\stuff\data.csv --> data.csv)

			def filepath = "father_engagement_data.csv"
			def dataSourceFile = new File(filepath).name

			def outputData = fatherEngagementDataBuilderService.generateData(fatherEngagementList)

			if (outputData) {
				// assume this doesn't work
				// Render outputData as CSV
	
				// get a field list
				def firstRow = outputData[0]
				def columnNames = firstRow.collect{ it.key }
	
				/*columnNames.each{
					println "Dataset columnNames >> ${it}"
				}*/
	
				// write the header column
				//  "ID","FirstName","MiddleName","LastName","Suffix"
				columnNames.eachWithIndex{ col, i ->
					if (i > 0) {
						dataSourceContents << ","
					}
					dataSourceContents << ("\"" + col.replace("\"", "\"\"") + "\"")
				}
				// Using \r\n for MS Windows
				dataSourceContents << "\r\n"
	
				// write the data
				outputData.each{ row ->
					columnNames.eachWithIndex{ col, i ->
	
						if (i > 0) {
							dataSourceContents << ","
						}
						// default content is empty
						def columnValue = ""
						// If there's a non-null value...
						if (row[col] != null) {
							// take the content and escape the double quotes (")
							
							
							
							def columnContent = row[col].toString().replace('"', '""')
							
							if (row[col].class.toString() == 'class java.sql.Timestamp') {
								// if (debug) { println "Row Class for ${col}: " + row[col].class.toString() }
								// This is a SQL Timestamp.  We're changing the date format so that MS Word can parse it
								// (it has trouble with milliseconds)
								def sqlDate = new DateTime(row[col].time)
								columnContent = fmt.print(sqlDate)
							}
							
							// then surround it with double quotes
							columnValue = '"' + columnContent  + '"'
						}
	
						dataSourceContents << columnValue
					}
					dataSourceContents << "\r\n"
				}
			}
			response.setHeader("Content-disposition", "attachment; filename=\"${dataSourceFile}\"");
			
			render(contentType: "application/octet-stream", text: dataSourceContents)

			// Use for debugging (doesn't download)
			//render(text: dataSourceContents);
		} else {
			flash.message = "No father engagement data found"
			redirect(action:"list")
		}
	}

}
