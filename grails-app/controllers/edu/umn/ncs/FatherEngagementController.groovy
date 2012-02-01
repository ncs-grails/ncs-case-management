package edu.umn.ncs

import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import edu.umn.ad.DirectoryService
// Let's us use security annotations
import grails.converters.*
import grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT','ROLE_NCS'])
class FatherEngagementController {
    def fatherEngagementDataBuilderService
	def directoryService
	def memberInstanceList
	def debug = false
	def groupName = "EnHS-NCS-Interviewer"
	
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
		def errorMessage = ""
		if (params.trackedItem) {
			// Get the tracked item
			def trackedItemInstance = TrackedItem.read(params.trackedItem.toLong())
			if (trackedItemInstance) {
				// Get NCS group members for list of interviewers
				memberInstanceList = directoryService.getMembers()
				if ( ! memberInstanceList ) {
					if (debug) {
						println "Populating user list"
					}
					directoryService.loadUsersByGroupname(groupName)
					//memberInstanceList = directoryService.members
					memberInstanceList = directoryService.getMembers()
				}
				// Determine if a father engagement form already exists for this form
				def fatherEngagementInstance = FatherEngagement.findByTrackedItem(trackedItemInstance)
				if (!fatherEngagementInstance) {
					params.trackedItem = trackedItemInstance
					
					fatherEngagementInstance = new FatherEngagement(params)
					
					def interviewDate = null
					def localStartDate = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalDate()
					def localEndDate = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalDate()
					def localStartTime = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalTime()
					def localEndTime = new DateTime(fatherEngagementInstance?.interviewEndTime).toLocalTime()
					def localDate = new DateTime().toLocalDate()
		
					if (localStartDate && localEndDate) {
						// Set the interview date 
						fatherEngagementInstance.interviewDate = fatherEngagementInstance?.interviewStartTime
						interviewDate = new DateTime(fatherEngagementInstance?.interviewStartTime).toLocalDate()
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
											render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance, memberInstanceList: memberInstanceList.sort { a,b -> a.displayName <=> b.displayName } ])
										}
									}
									else {
										errorMessage = "Interview start and end times are not consistent. Please review."
										render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance, memberInstanceList: memberInstanceList.sort { a,b -> a.displayName <=> b.displayName }, errorMessage: errorMessage ])
									}
								}
								else {
									errorMessage = "Dates for interview start and end times are not equal. Please review."
									render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance, memberInstanceList: memberInstanceList.sort { a,b -> a.displayName <=> b.displayName }, errorMessage: errorMessage ])
								}		
							}
							else {
								// Stop if interview date is greater than current date
								errorMessage = "Invalid Interview End Time. Please review."
								render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance, memberInstanceList: memberInstanceList.sort { a,b -> a.displayName <=> b.displayName }, errorMessage: errorMessage ])
							}	
						}
						else {
							// Stop if interview date is greater than current date
							errorMessage = "Invalid Interview Start Time. Please review."
							render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance, memberInstanceList: memberInstanceList.sort { a,b -> a.displayName <=> b.displayName }, errorMessage: errorMessage ])
						}
					}
					else {
						errorMessage = "Please enter interview start and end times. Please review."
						render(view: "create", model: [fatherEngagementInstance: fatherEngagementInstance, memberInstanceList: memberInstanceList.sort { a,b -> a.displayName <=> b.displayName }, errorMessage: errorMessage ])
					}
				}
				else {
					flash.message = "A father engagement form already exists for this item."
					redirect(action: "edit", id: fatherEngagementInstance.id)
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
			memberInstanceList = directoryService.getMembers()
			if ( ! memberInstanceList ) {
				if (debug) {
					println "Populating user list"
				}
				directoryService.loadUsersByGroupname(groupName)
				//memberInstanceList = directoryService.members
				memberInstanceList = directoryService.getMembers()
			}
			
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
		memberInstanceList = directoryService.getMembers()
		if ( ! memberInstanceList ) {
			if (debug) {
				println "Populating user list"
			}
			directoryService.loadUsersByGroupname(groupName)
			//memberInstanceList = directoryService.members
			memberInstanceList = directoryService.getMembers()
		}

		if (debug) {
			println "params::trackedItem::${params?.trackedItemId}"				
		}
		def trackedItemId = null
		try {
			trackedItemId = params?.trackedItemId?.toUpperCase()?.replace('I','')?.toLong()
		}
		catch (e) {
			result.errorText = "Error: ${e}, Invalid tracked item ID: ${params?.trackedItemId}"
			render(template:'consentErrorForm', model:[result:result])
			return false
		}
		def trackedItemInstance = TrackedItem.read(trackedItemId)
		if (trackedItemInstance) {
			if (trackedItemInstance?.person) {
				// Check to see if this person already has a father engagement form
				def trackedItemList = TrackedItem.findAllByPerson(trackedItemInstance?.person)
				def fatherEngagementInstance = null
				trackedItemList.each {
					if (FatherEngagement.findByTrackedItem(it)) {
						fatherEngagementInstance = it
					}
				}
				// we have an item
				result.success = true
				result.trackedItemId = trackedItemInstance.id
				result.person = trackedItemInstance?.person
				result.fullname = trackedItemInstance?.person.fullName
				result.memberInstanceList = memberInstanceList.sort { a,b -> a.displayName <=> b.displayName }
				result.resultName = "Found tracked item"
				if (fatherEngagementInstance) {
					result.errorText = "A father engagement form has already been entered for ${trackedItemInstance?.person}"
					render(template:'consentErrorForm', model:[result:result])
					return false
				}
				else {
					//render result as JSON
					if (debug) {
						println "result::${result}"
					}
					
					render(template:'consentForm', model:[result:result])			
				}
			}
			else {
				result.errorText = "No person associated with this tracked item"
				render(template:'consentErrorForm', model:[result:result])
				return false
			}
		} else {
			render "Tracked Item does not exist!"
			return false
		}
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
