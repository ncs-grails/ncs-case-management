package edu.umn.ncs

import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import edu.umn.ad.DirectoryService
// Let's us use security annotations
import grails.converters.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT','ROLE_NCS'])
class FatherEngagementController {
    def fatherEngagementDataBuilderService
	def directoryService
	
	def debug = true
	
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
		
        return [fatherEngagementInstance: fatherEngagementInstance ]
    }

    def save = {
		if (params.trackedItem) {
			// Get the tracked item
			def trackedItemInstance = TrackedItem.read(params.trackedItem.toLong())
			if (trackedItemInstance) {
				params.trackedItem = trackedItemInstance
				
				def fatherEngagementInstance = new FatherEngagement(params)
				
				def interviewDate = null
				def localStartDate = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalDate()
				def localEndDate = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalDate()
				def localStartTime = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalTime()
				def localEndTime = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalTime()
				def localDate = new DateTime().toLocalDate()
	
				if (localStartDate && localEndDate) {
					// Set the interview date 
					interviewDate = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalDate()
					fatherEngagementInstance.interviewDate = interviewDate
					if (debug) {
						println "localStartDate::${localStartDate}"
						println "localEndDate::${localEndDate}"
						println "date_diff::${localStartDate.compareTo(localEndDate)}"
					}
	
					// Verify that start date/time is not in the future
					if (localStartDate.compareTo(localDate) < 1) {
						// Verify that end date/time is not in the future
						localEndDate = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalDate()
						if (localEndDate.compareTo(localDate) < 1) {
							if (debug) {
								println "localStartTime::${localStartTime}"
								println "localEndTime::${localEndTime}"
								println "time_diff::${localEndTime.compareTo(localStartTime)}"
							}
							// compare dates for interview start and end times
							if (localStartDate.compareTo(localEndDate) == 0) {
								// compare start and end times (end time should be greater)
								if (localEndTime.compareTo(localStartTime) > 0) {								
									if (fatherEngagementInstance.save(flush: true)) {
										if (debug) {
											println "Saving father engagement"
										}
										flash.message = "Father Engagement Form Saved"
										redirect(action: "edit", id: fatherEngagementInstance.id)
									}
									else {
										render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance])
									}
								}
								else {
									flash.message = "Interview start and end times are not consistent. Please review."
									render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance])
								}
							}
							else {
								flash.message = "Dates for interview start and end times are not equal. Please review."
								render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance])
							}		
						}
						else {
							// Stop if interview date is greater than current date
							flash.message = "Invalid Interview End Time. Please review."
							render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance])
						}	
					}
					else {
						// Stop if interview date is greater than current date
						flash.message = "Invalid Interview Start Time. Please review."
						render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance])
					}
				}
				else {
					flash.message = "Please enter interview start and end times. Please review."
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
			// Get NCS group members for list of interviewers
			def groupName = "EnHS-NCS"
			def groupDescription = directoryService.loadUsersByGroupname(groupName)
			def memberInstanceList = directoryService.getMembers()
			
            return [fatherEngagementInstance: fatherEngagementInstance, memberInstanceList: memberInstanceList.sort { a,b -> a.displayName <=> b.displayName } ]
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

			def interviewDate = new DateTime(fatherEngagementInstance?.interviewDate).toLocalDate()
			def localStartDate = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalDate()
			def localEndDate = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalDate()
			def localStartTime = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalTime()
			def localEndTime = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalTime()
			def localDate = new DateTime().toLocalDate()

			if (localStartDate && localEndDate) {
				if (debug) {
					println "localStartDate::${localStartDate}"
					println "localEndDate::${localEndDate}"
					println "date_diff::${localStartDate.compareTo(localEndDate)}"
				}

				// update the interview date if necessary
				if (localStartDate.compareTo(interviewDate) != 0) {
					fatherEngagementInstance.interviewDate = fatherEngagementInstance?.interviewStartTime
				}				

				// Verify that start date/time is not in the future
				if (localStartDate.compareTo(localDate) < 1) {
					// Verify that end date/time is not in the future
					localEndDate = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalDate()
					if (localEndDate.compareTo(localDate) < 1) {
						if (debug) {
							println "localStartTime::${localStartTime}"
							println "localEndTime::${localEndTime}"
							println "time_diff::${localEndTime.compareTo(localStartTime)}"
						}
						// compare dates for interview start and end times
						if (localStartDate.compareTo(localEndDate) == 0) {
							// compare start and end times (end time should be greater)
							if (localEndTime.compareTo(localStartTime) > 0) {								
								if (!fatherEngagementInstance.hasErrors() && fatherEngagementInstance.save(flush: true)) {
									if (debug) {
										println "Updating father engagement"
									}
									flash.message = "Father Engagement Form Updated"
									redirect(action: "edit", id: fatherEngagementInstance.id)
								}
								else {
									render(view: "edit", model: [fatherEngagementInstance: fatherEngagementInstance])
								}
							}
							else {
								flash.message = "Interview start and end times are not consistent. Please review."
								redirect(action: "edit", id: fatherEngagementInstance.id)
							}
						}
						else {
							flash.message = "Dates for interview start and end times are not equal. Please review."
							redirect(action: "edit", id: fatherEngagementInstance.id)
						}		
					}
					else {
						// Stop if interview date is greater than current date
						flash.message = "Invalid Interview End Time. Please review."
						redirect(action: "edit", id: fatherEngagementInstance.id)
					}	
				}
				else {
					// Stop if interview date is greater than current date
					flash.message = "Invalid Interview Start Time. Please review."
					redirect(action: "edit", id: fatherEngagementInstance.id)
				}
			}
			else {
				flash.message = "Please enter interview start and end times. Please review."
				redirect(action: "edit", id: fatherEngagementInstance.id)
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
	
	def updatePersonInfo = {
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
			fullname: null,
			resultName: false,
			memberInstanceList: [:],
			errorText: ""
		]

		// Get NCS group members for list of interviewers
		def groupName = "EnHS-NCS"
		def groupDescription = directoryService.loadUsersByGroupname(groupName)
		def memberInstanceList = directoryService.getMembers()

		if (debug) {
			println "params::trackedItem::${params?.trackedItemId}"				
		}
		
		def trackedItemInstance = TrackedItem.read(params?.trackedItemId.toLong())
		if (trackedItemInstance) {
			// we have an item
			result.success = true
			result.trackedItemId = trackedItemInstance.id
			result.person = trackedItemInstance?.person
			result.fullname = trackedItemInstance?.person.fullName
			result.memberInstanceList = memberInstanceList.sort { a,b -> a.displayName <=> b.displayName }
			result.resultName = "Found tracked item"
		} else {
			result.errorText = "Tracked Item does not exist!"
		}

		//render result as JSON
		if (debug) {
			println "result::${result}"
		}
		
		render(template:'consentForm', model:[result:result])
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
