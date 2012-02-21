package edu.umn.ncs

import java.awt.event.ItemEvent;
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.LocalDate

import grails.converters.*
import edu.umn.ad.DirectoryService
// Let's us use security annotations
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_INCENTIVES'])
class IncentiveController {
	def springSecurityService
	def directoryService
	def memberInstanceList
	static groupName = "EnHS-NCS-Interviewer"
	def debug = false
	static def appName = "ncs-incentive-mailing"
	
	static def dateFormat = DateTimeFormat.forPattern("MM/dd/yyyy")
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

	def list = {
		def username = springSecurityService.principal.getUsername()
		// Get NCS group members for list of interviewers
		//def groupName = "EnHS-NCS"
		
		if (debug) {
			println "Loading users for groupName::${groupName}"
		}
		//memberInstanceList = directoryService.members
		memberInstanceList = directoryService.getMembers()
		if ( ! memberInstanceList ) {
			if (debug) {
				println "Populating user list"
			}
			directoryService.loadUsersByGroupname(groupName)
			//memberInstanceList = directoryService.members
			memberInstanceList = directoryService.getMembers()
		}
		// Filter list based on user selection: 1=all, 2=distributed, 3=checked in, 4=checked out, 5=checked out to
		def tempIncentiveInstanceList = null
		def incentiveInstanceList = null
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params?.id) {
			// prep all the things we'll need to send back
			def result = [
				success: false,
				filterName:"",
				filterId:params?.id.toInteger(),
				incentiveInstanceList:Incentive.list(params),
				incentiveInstanceTotal: Incentive.count(),
				memberInstanceList:memberInstanceList,
				interviewer:username,
				errorText: ""
			]
	
			def filterId = params?.id
			def filterName = "All Incentives"
			if (filterId == '1') {
				result.filterName = filterName
				result.success = true
			}
			filterName = "Distributed Incentives"
			if (filterId == '2') {
				incentiveInstanceList = Incentive.findAllByTrackedItemIsNotNull()
				result.success = true
				result.filterName = filterName
				result.incentiveInstanceList = incentiveInstanceList
				result.incentiveInstanceTotal = incentiveInstanceList.size()
			}
			filterName = "Checked In Incentives"
			if (filterId == '3') {
				incentiveInstanceList = Incentive.findAllByTrackedItemAndCheckedOut(null,false)
				tempIncentiveInstanceList.each {
					// Only add incentives that have not been distributed (i.e. no tracked item)
					if (!it.trackedItem) {
						incentiveInstanceList << it
					}
				}
				result.success = true
				result.filterName = filterName
				result.incentiveInstanceList = incentiveInstanceList
				result.incentiveInstanceTotal = incentiveInstanceList.size()
			}
			filterName = "Checked Out Incentives"
			if (filterId == '4') {
				incentiveInstanceList = Incentive.findAllByTrackedItemAndCheckedOut(null,true)
				result.success = true
				result.filterName = filterName
				result.incentiveInstanceList = incentiveInstanceList
				result.incentiveInstanceTotal = incentiveInstanceList.size()
			}
			filterName = "Incentives Checked Out To "
			if (filterId == '5') {
				def user = memberInstanceList.find{ it?.username == params?.interviewer }
				filterName += user?.displayName
				incentiveInstanceList = Incentive.findAllByTrackedItemAndCheckedOutToWhom(null,params?.interviewer)
				result.success = true
				result.filterName = filterName
				result.incentiveInstanceList = incentiveInstanceList
				result.incentiveInstanceTotal = incentiveInstanceList.size()
				result.interviewer = user?.username
			}
			filterName = "Incentives with Receipt#: "
			if (filterId == '6') {
				filterName += params?.receiptNumber
				incentiveInstanceList = Incentive.findAllByReceiptNumber(params?.receiptNumber)
				result.success = true
				result.filterName = filterName
				result.incentiveInstanceList = incentiveInstanceList
				result.incentiveInstanceTotal = incentiveInstanceList.size()
			}
			render(template:'filterList', model:[result:result])
		}
		else {
			[incentiveInstanceList: Incentive.list(params), incentiveInstanceTotal: Incentive.count(), memberInstanceList: memberInstanceList ]
		}
	}

    def create = {
        def incentiveInstance = new Incentive()
        incentiveInstance.properties = params
        return [incentiveInstance: incentiveInstance]
    }

	def batchCreate = {
	}

    def save = {
        def incentiveInstance = new Incentive(params)
        if (incentiveInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
            redirect(controller:"appointment", action: "edit", id: incentiveInstance.appointment.id)
        }
        else {
            render(view: "create", model: [incentiveInstance: incentiveInstance])
        }
    }
	
    def createAppointmentIncentive = {
        def incentiveInstance = new Incentive()
        incentiveInstance.properties = params
		def appointmentInstance = Appointment.read(params.appointment.id)
        return [incentiveInstance: incentiveInstance, appointmentInstance: appointmentInstance]
    }

	def saveAppointmentIncentive = {
		def username = springSecurityService.principal.getUsername()
		// Get the appointment
		def appointmentInstance = Appointment.get(params?.appointment.id)
		if (appointmentInstance) {
			def incentiveInstance = new Incentive(params)
			// Set the incentive date equal to the appointment date
			incentiveInstance.incentiveDate = appointmentInstance?.startTime
			// Set provenance data
			incentiveInstance.userCreated = username
			incentiveInstance.userUpdated = username
			if (incentiveInstance.save(flush: true)) {
				// Link the appointment to the incentive
				def appointmentIncentive = new AppointmentIncentive(incentive:incentiveInstance,appointment:appointmentInstance).save(flush:true)
				// Update the appointment with the new incentive
				if (appointmentIncentive) {
					appointmentInstance.addToIncentives(appointmentIncentive)
					appointmentInstance.save(flush:true)
					flash.message = "${message(code: 'default.created.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
					redirect(controller:"appointment", action: "edit", id: appointmentInstance.id)
				}
				else {
					flash.message = "Unable to link the incentive to the appointment"
					redirect(controller:"appointment", action: "edit", id: appointmentInstance.id)
				}
			}
			else {
				render(view: "createAppointmentIncentive", model: [incentiveInstance: incentiveInstance, appointmentInstance:appointmentInstance])
			}
		}
		else {
			flash.message = "Appointment not found with id ${params?.appointment.id}"
			redirect(controller:"mainMenu")
		}
	}

	def saveIncentiveBatch = {
		// Mechanism for saving a batch of incentives in one session (e.g. gift cards)
		def username = springSecurityService.principal.getUsername()
		
		// Delay Code, used to test out of sequence responses
		/*
		def sleepTime = rand.nextInt(3000)
		print "Waiting...${sleepTime}"
		sleep(sleepTime)
		println "...Done."
		 */

		def receiptNumber = params.receiptNumberInstance
		def amount = params.amountInstance
		def type = null
		try {
			type = IncentiveType.read(params.incentiveTypeInstance)
		}
		catch (Exception e) {
			if (debug) {
				println "Error getting incentive type. ${e}"
			}
		}
		// prep all the things we'll need to send back
		def result = [
			success: false,
			barcode: "",
			type:"",
			amount:0,
			divId: 0,
			errorText: ""
		]

		// if a div ID was passed, let's save it to the result set
		if (params?.divId) {
			result.divId = params.divId
		}

		if (type) {
			if (params?.id){
				// Get the barcode
				def barcodeValue = params?.id?.toUpperCase()?.replace('INC','')
				if (debug) {
					println "barcodeValue: ${barcodeValue}"
				}
				
				// Check for incentive
				def incentiveInstance = Incentive.get(barcodeValue.toLong())
				// If not found, try by current barcode and type
				if (!incentiveInstance) {
					incentiveInstance = Incentive.findByBarcodeAndType(barcodeValue, type)				
				}
				if (incentiveInstance) {
					// barcode already scanned
					result.errorText = "An incentive already exists with this barcode and type"
				}
				else {
					// Create the incentive
					//incentiveInstance = new Incentive(barcode:barcodeValue,receiptNumber:receiptNumber,amount:amount,incentiveDate:new Date(),type:type,userCreated:username,userUpdated:username)
					incentiveInstance = new Incentive(barcode:barcodeValue
						,receiptNumber:receiptNumber, amount:amount
						,type:type, userCreated:username, userUpdated:username)
					
					// Try to save the incentive
					if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush: true)) {
						result.barcode = incentiveInstance.barcode
						result.amount = incentiveInstance?.amount
						result.incentiveType =  incentiveInstance?.type.name
						result.success = true
					} else {
						// invalid barcode!
						result.errorText = "Invalid barcode"
					}
				}
			}
		}
		else {
			// invalid barcode!
			result.errorText = "No incentive type selected"
		}

		render result as JSON

	}

    def edit = {
        def incentiveInstance = Incentive.get(params.id)
        if (!incentiveInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
			redirect(controller:"appointment", action: "edit", id: incentiveInstance.appointment.id)
        }
        else {
			// Get transaction log
			def incentiveTransactionLogList = incentiveInstance
				.transactionLogs.sort{ a,b -> b?.transactionDate <=> a?.transactionDate }

			// Search for appointment 
			def appointmentIncentiveInstance = AppointmentIncentive.findByIncentive(incentiveInstance)
            return [incentiveInstance: incentiveInstance
				, incentiveTransactionLogList: incentiveTransactionLogList
				, appointmentIncentiveInstance: appointmentIncentiveInstance]
        }
    }

    def editAppointmentIncentive = {
        def incentiveInstance = Incentive.get(params.id)
        if (!incentiveInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
			redirect(controller:"appointment", action: "index")
        }
        else {
			def appointmentIncentiveInstance = AppointmentIncentive.findByIncentive(incentiveInstance)
            return [incentiveInstance: incentiveInstance, appointmentIncentiveInstance: appointmentIncentiveInstance]
        }
    }

    def update = {
		def username = springSecurityService.principal.getUsername()
        def incentiveInstance = Incentive.get(params.id)
        if (incentiveInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (incentiveInstance.version > version) {
                    
                    incentiveInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'incentive.label', default: 'Incentive')] as Object[], "Another user has updated this Incentive while you were editing")
                    render(view: "edit", model: [incentiveInstance: incentiveInstance])
                    return
                }
            }
			params.userUpdated = username
			// Perform a test on the incentive date to make sure it is not in the future
			def localDate = new DateTime().toLocalDate()
			def localIncentiveDate = new DateTime(params?.incentiveDate).toLocalDate()
			if (localIncentiveDate.compareTo(localDate) < 1) {
				// Check to see if the transaction log needs to be updated
				if ((params?.checkedOut && !incentiveInstance?.checkedOut) || (!params?.checkedOut && incentiveInstance?.checkedOut)) {
					if (incentiveInstance.checkedOutToWhom) {
						// Update checkout log information
						new IncentiveTransactionLog(incentive:incentiveInstance
							,transactionDate:new Date()
							,checkedOutInToWhom: incentiveInstance.checkedOutToWhom
							,checkedOutInByWhom: username).save(flush:true)
					}
				} 
				incentiveInstance.properties = params
				// If incentive has been activated, set the activation date
				if (incentiveInstance?.activated) {
					if (!incentiveInstance?.dateActivated) {
						incentiveInstance.dateActivated = new Date()
					}
				}
				else {
					incentiveInstance.dateActivated = null
				}
				if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush: true)) {
					// todo
					def renderError = "${message(code: 'default.updated.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
					
					render(view: "edit", model: [incentiveInstance: incentiveInstance, renderError: renderError])
				}
				else {
					render(view: "edit", model: [incentiveInstance: incentiveInstance])
				}
			}
			else {
				// todo
				def renderError = "Save Failed: An inconsistent incentive date was entered. Please try again."
				render(view: "edit", model: [incentiveInstance: incentiveInstance, renderError: renderError])
			}
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
			redirect(controller:"appointment", action: "index")
        }
    }
	// TODO: Move these into an AppointmentIncentiveController...  some day???
	def updateAppointmentIncentive = {
		
		def username = springSecurityService.principal.getUsername()
		def incentiveInstance = Incentive.get(params.id)
		if (incentiveInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (incentiveInstance.version > version) {
					
					incentiveInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'incentive.label', default: 'Incentive')] as Object[], "Another user has updated this Incentive while you were editing")
					render(view: "edit", model: [incentiveInstance: incentiveInstance])
					return
				}
			}
			incentiveInstance.properties = params
			incentiveInstance.userUpdated = username
			
			
			def appointmentIncentiveInstance = AppointmentIncentive.findByIncentive(incentiveInstance)
			if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
				redirect(controller:"appointment", action: "edit", id: appointmentIncentiveInstance.appointment.id)
			}
			else {
				render(view: "edit", model: [incentiveInstance: incentiveInstance, appointmentIncentiveInstance: appointmentIncentiveInstance])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
			redirect(controller:"appointment", action: "index")
		}
	}

    def delete = {
        def incentiveInstance = Incentive.get(params.id)
        if (incentiveInstance) {
            try {
				def appointmentIncentiveInstance = AppointmentIncentive.findByIncentive(incentiveInstance)
				def appointmentId = appointmentIncentiveInstance?.appointment?.id
				if (debug) {
					println "appointmentId::${appointmentId}"
				}
				if (appointmentIncentiveInstance) {
					def appointmentInstance = appointmentIncentiveInstance?.appointment
					if (debug) {
						println "appointmentInstance::${appointmentInstance}"
					}
					// Remove incentive link from the appointment
					appointmentInstance.removeFromIncentives(appointmentIncentiveInstance)
					appointmentInstance.save()
					if (debug) {
						println "Removed incentive from appointment"
					}
					appointmentIncentiveInstance.delete()					
					if (debug) {
						println "Deleted appointment incentive"
					}
					incentiveInstance.delete(flush: true)
					if (debug) {
						println "Deleted incentive"
					}
					flash.message = "${message(code: 'default.updated.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
					redirect(controller:"appointment", action: "edit", id: appointmentIncentiveInstance.appointment.id)
				}
				else {
					if (debug) {
						println "Attempting to delete incentive::${incentiveInstance}"
					}
					incentiveInstance.delete(flush: true)
					if (debug) {
						println "Deleted incentive"
					}
					flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"					
					redirect(action: "list")
				}
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				// TODO: flash.no.render
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
                render(view: "edit", model: [incentiveInstance: incentiveInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
			redirect(action: "list")
        }
    }

	def checkout = {
		// Get current user
		def username = springSecurityService.principal.getUsername()
		// Get NCS group members for list of interviewers
		//def groupName = "EnHS-NCS"

		memberInstanceList = directoryService.getMembers()
		if ( ! memberInstanceList ) {
			directoryService.loadUsersByGroupname(groupName)
			memberInstanceList = directoryService.getMembers()
		}
		
		return [memberInstanceList: memberInstanceList, username: username ]
	}
	
	def checkoutIncentive = {
		/**
		 * Sets the checked out status of an incentive (e.g. gift card) with
		 * corresponding provenance data.
		 */
		def username = springSecurityService.principal.getUsername()
		def checkedOutDate = null
		def checkedOutTo = null
		
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
			barcode: "",
			incentiveType:"",
			amount:0,
			interviewer:"",
			divId: 0,
			errorText: ""
		]

		// if a div ID was passed, let's save it to the result set
		if (params?.divId) {
			result.divId = params.divId
		}

		if (params?.id){
			def barcodeValue = params?.id?.toUpperCase()?.replace('INC','')
			
			if (debug) {
				println "barcodeValue: ${barcodeValue}"
			}
			
			// Check for existing incentive by barcode
			def incentiveInstance = Incentive.findByBarcode(barcodeValue)
			// If not found, try by id
			if (!incentiveInstance) {
				incentiveInstance = Incentive.get(barcodeValue.toLong())				
			}
			if (incentiveInstance) {
				// Get Assignment status
				if (incentiveInstance?.trackedItem) {
					result.errorText = "${incentiveInstance?.type?.name} was distributed to ${incentiveInstance?.trackedItem?.person} on ${dateFormat.print(new DateTime(incentiveInstance?.incentiveDate))}"					
				}
				else {
					// Get checked out status
					if (incentiveInstance?.checkedOut) {
						checkedOutDate = new java.text.SimpleDateFormat("MM/dd/yyyy").format(incentiveInstance?.dateCheckedOut)
						result.errorText = "${incentiveInstance?.type?.name} was checked out to ${incentiveInstance?.checkedOutToWhom} on ${checkedOutDate}"
					}
					else {
						// Get person to whom the gift card will be checked out
						if (params?.checkedOutToInstance) {
							checkedOutTo = params.checkedOutToInstance
							incentiveInstance.checkedOut = true
							incentiveInstance.checkedOutToWhom = checkedOutTo
							incentiveInstance.checkedOutByWhom = username
							incentiveInstance.dateCheckedOut = new Date()
							incentiveInstance.userUpdated = username
							if (incentiveInstance.save(flush:true)) {
								result.barcode = incentiveInstance.barcode
								result.amount = incentiveInstance?.amount
								result.incentiveType =  incentiveInstance?.type.name
								result.checkedOutTo =  incentiveInstance?.checkedOutToWhom
								result.success = true
								
								// Update checkout log information
								new IncentiveTransactionLog(incentive:incentiveInstance,transactionDate:incentiveInstance.dateCheckedOut,checkedOut:true,checkedOutInToWhom:incentiveInstance?.checkedOutToWhom,checkedOutInByWhom:username).save(flush:true)
								
							}
							else {
								result.errorText = "Unable to check out ${incentiveInstance?.type?.name}!"
							}
						}
						else {
							result.errorText = "No interviewer selected"
						}
					}	
				}
			} else {
				// Get incentive details
				def receiptNumber = params.receiptNumberInstance
				def amount = params.amountInstance
				def type = null
				try {
					type = IncentiveType.read(params.incentiveTypeInstance)
				} catch (Exception e) {
					if (debug) {
						println "Error getting incentive type. ${e}"
					}
				}
				// Get person to whom the incentive will be checked out
				if (type) {
					if (params?.checkedOutToInstance) {
						checkedOutTo = params.checkedOutToInstance
							
						// Try to create the incentive
						//incentiveInstance = new Incentive(barcode:barcodeValue,receiptNumber:receiptNumber,amount:amount,type:type,checkedOut:true,checkedOutToWhom:checkedOutTo,checkedOutByWhom:username,incentiveDate:new Date(),dateCheckedOut:new Date(),userCreated:username,userUpdated:username)
						incentiveInstance = new Incentive(barcode:barcodeValue
							,receiptNumber:receiptNumber
							,amount:amount
							,type:type,checkedOut:true
							,checkedOutToWhom:checkedOutTo
							,checkedOutByWhom:username
							,dateCheckedOut:new Date()
							,userCreated:username
							,userUpdated:username)
						
						// Try to save the incentive
						if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush: true)) {
							result.barcode = incentiveInstance.barcode
							result.amount = incentiveInstance?.amount
							result.incentiveType =  incentiveInstance?.type.name
							result.checkedOutTo =  incentiveInstance?.checkedOutToWhom
							result.success = true

						} else {
							// invalid barcode!
							result.errorText = "Unable to create ${type?.name} incentive"
						}
					}
				}
				else {
					result.errorText = "Please enter incentive details"
				}
			}
		}

		render result as JSON

	}

	def checkin = {
	}
	
	def checkinIncentive = {
		
		def username = springSecurityService.principal.getUsername()
		
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
			barcode: "",
			incentiveType:"",
			amount:0,
			divId: 0,
			errorText: ""
		]

		// if a div ID was passed, let's save it to the result set
		if (params?.divId) {
			result.divId = params.divId
		}

		if (params?.id){
			def barcodeValue = params?.id?.toUpperCase()?.replace('INC','')
			
			if (debug) {
				println "barcodeValue: ${barcodeValue}"
			}
			
			// Check for existing incentive by barcode
			def incentiveInstance = Incentive.findByBarcode(barcodeValue)
			// If not found, try by id
			if (!incentiveInstance) {
				incentiveInstance = Incentive.get(barcodeValue.toLong())				
			}
			if (incentiveInstance) {
				// Determine if the incentive has been assigned
				if (incentiveInstance?.trackedItem) {
					result.errorText = "${incentiveInstance?.type.name} has been assigned to item ${incentiveInstance?.trackedItem?.id}: ${incentiveInstance?.trackedItem?.batch?.primaryBatchInstrument?.instrument?.name}"					
				}
				else {
					// Get checked out status
					if (incentiveInstance?.checkedOut) {
						// Check in card and clear out checked status
						def lastCheckedOutToWhom = incentiveInstance?.checkedOutToWhom
						incentiveInstance?.checkedOut = false
						incentiveInstance.checkedOutToWhom = null
						incentiveInstance.dateCheckedOut = null
						incentiveInstance.userUpdated = username
						if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush:true)) {
							result.barcode = incentiveInstance.barcode
							result.amount = incentiveInstance?.amount
							result.incentiveType =  incentiveInstance?.type.name
							result.success = true
	
							// Update checkout log information
							new IncentiveTransactionLog(incentive:incentiveInstance,transactionDate:new Date(),checkedOutInToWhom:lastCheckedOutToWhom,checkedOutInByWhom:username).save(flush:true)
	
						}
						else {
							result.errorText = "Unable to check in ${incentiveInstance?.type.name} incentive due to errors"
						}
					}
					else {
						result.errorText = "Incentive is not checked out"
					}
				}
			}
			else {
				result.errorText = "Incentive not found"
			}
		}
		else {
			result.errorText = "Invalid barcode:${params?.id}"
		}

		render result as JSON

	}

	def assignIncentive = { }
	
	def assignIncentiveToItem = {
		def username = springSecurityService.principal.getUsername()

		// prep all the things we'll need to send back
		def result = [
			success: false,
			incentiveId: "",
			incentiveBarcode: "",
			incentiveVersion: 0,
			trackedItemId: "",
			appointment: "NA",
			appointmentDate: new Date(),
			person: null,
			resultName: false,
			errorText: ""
		]
	
		def incentiveInstance = null
		def trackedItemInstance = null
		
		// Get the incentive barcode -- Replace INC prefix if
		def barcodeValue = params?.code?.toUpperCase()?.replace('INC','')
		if (debug) {
			println "params::barcode::${barcode}"
		}
		if (barcodeValue) {
			// Get incentive by barcode
			incentiveInstance = Incentive.findByBarcode(barcodeValue)
			// If not found, try by id
			if (!incentiveInstance) {
				incentiveInstance = Incentive.get(barcodeValue.toLong())				
			}
			if (incentiveInstance) {
				// Determine if incentive has already been assigned
				if (incentiveInstance?.trackedItem) {
					result.success = false
					result.incentiveId = ""
					result.resultName = "Found tracked item"
					result.errorText = "${incentiveInstance?.type?.name}: ${incentiveInstance?.barcode} has already been assigned to ${incentiveInstance?.trackedItem?.person}"
					render(view: "assignIncentive", model:[result: result])
					return false
				}
				else {
					// Get the tracked item
					if (params?.trackedItemId) {
						try {
							trackedItemInstance = TrackedItem.read(params?.trackedItemId?.toUpperCase()?.replace('I','')?.toLong())							
						}
						catch (e) {
							trackedItemInstance = null
						}
					}
					if (trackedItemInstance) {
						// Verify that the tracked item is linked to a person
						if (trackedItemInstance?.person) {
							// Attempt to update the incentive assigning it to tracked item
							incentiveInstance.userUpdated = username
							incentiveInstance.trackedItem = trackedItemInstance
							if (trackedItemInstance.batch.instrumentDate || trackedItemInstance.batch.dateCreated) {
								incentiveInstance.incentiveDate = trackedItemInstance?.batch?.instrumentDate ?: trackedItemInstance.batch.dateCreated
							}
							else {
								incentiveInstance.incentiveDate = new Date()
							}
							
							// Search for an appointment associated with this tracked item
							def appointmentInstance = Appointment.findByLetter(trackedItemInstance)
							result.incentiveId = incentiveInstance.id
							result.incentiveBarcode = barcodeValue
							result.incentiveVersion = incentiveInstance.version
							result.trackedItemId = trackedItemInstance.id
							if (appointmentInstance) {
								result.appointment = appointmentInstance
								result.appointmentDate = appointmentInstance?.startTime
								incentiveInstance.incentiveDate = appointmentInstance?.startTime
							}
							else {
								// Search for an appointment associated with the parent item of the current tracked item
								// TODO: Warning, this only goes one deep
								if (trackedItemInstance?.parentItem) {
									appointmentInstance = Appointment.findByLetter(trackedItemInstance?.parentItem)
									if (appointmentInstance) {
										result.appointment = appointmentInstance
										result.appointmentDate = appointmentInstance?.startTime
										incentiveInstance.incentiveDate = appointmentInstance?.startTime
									}
								}
							}
							
							// Attempt to save
							if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush: true)) {
								result.success = true
								// Update transaction log
								new IncentiveTransactionLog(incentive:incentiveInstance, 
									transactionDate:new Date(),
									givenToPerson:true,
									checkedOut:false,
									checkedOutInToWhom:"unassigned",
									checkedOutInByWhom:"unassigned").save(flush:true)
									
								//flash.message = "${message(code: 'default.updated.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
								flash.message = "${incentiveInstance?.type?.name}  assigned to ${trackedItemInstance?.person} for item ${trackedItemInstance?.id}: ${trackedItemInstance?.batch?.primaryBatchInstrument?.instrument?.name}"
								redirect(action: "assignIncentive")
							}
							else {
								result.success = false
								result.errorText = "An error occurred. Incentive could not be saved."
								render(view: "assignIncentive", model:[result:result])
								return
							}
	
							result.person = trackedItemInstance?.person
							result.resultName = "Found incentive and tracked item"
							render(template:'assignIncentiveForm', model:[result:result])
							return false
						}
						else {
							result.errorText = "No person associated with tracked item ${trackedItemInstance?.id}: ${trackedItemInstance?.batch?.primaryBatchInstrument?.instrument?.name}"
							//render(template:'assignErrorForm', model:[result:result])
							render(view: "assignIncentive", model:[result:result])
							return false
						}
					}
					else {
						result.errorText = "Tracked item with id [${params?.trackedItemId}] does not exist"
						//render(template:'assignErrorForm', model:[result:result])
						result.incentiveBarcode = barcodeValue
						render(view: "assignIncentive", model:[result:result])
						return false
					}
				}
			}
			else {
				result.errorText = "Incentive not found with barcode: ${barcodeValue}"
				//render(template:'assignErrorForm', model:[result:result])
				render(view: "assignIncentive", model:[result:result])
				return false
			}
		}
		else {
			result.errorText = "No incentive barcode entered. Please try again"
			//render(template:'assignErrorForm', model:[result:result])
			render(view: "assignIncentive", model:[result:result])
			return false
		}
	
	}

	def unassignIncentive = { }
	
	def unassignIncentiveFromItem = { 
		def username = springSecurityService.principal.getUsername()
		
		// prep all the things we'll need to send back
		def result = [
			success: false,
			incentiveId: "",
			incentiveBarcode: "",
			incentiveVersion: 0,
			trackedItemId: "",
			person: null,
			resultName: false,
			errorText: ""
		]
	
		def incentiveInstance = null
		def trackedItemInstance = null
		
		// Get the incentive barcode
		def barcodeValue = params?.incentiveBarcode?.toUpperCase()?.replace('INC','')
		if (debug) {
			println "params::barcode::${barcodeValue}"
		}
		if (barcodeValue) {
			// Get incentive
			incentiveInstance = Incentive.findByBarcode(barcodeValue?.toLong())
			// If not found, try by barcode
			if (!incentiveInstance) {
				incentiveInstance = Incentive.findByBarcode(barcodeValue)				
			}
			if (incentiveInstance) {
				// Determine if incentive has been assigned
				if (incentiveInstance?.trackedItem) {
					def msg = "${incentiveInstance?.type?.name}  unassigned from ${incentiveInstance?.trackedItem?.person} for item ${incentiveInstance?.trackedItem?.id}: ${incentiveInstance?.trackedItem?.batch?.primaryBatchInstrument?.instrument?.name}"
					result.incentiveId = incentiveInstance?.id
					result.resultName = "Unassigning from tracked item"
					result.incentiveBarcode = barcodeValue
					result.incentiveVersion = incentiveInstance.version
					result.trackedItemId = incentiveInstance?.trackedItem.id

					// Attempt to update the incentive by removing the tracked item link
					incentiveInstance.trackedItem = null
					incentiveInstance.incentiveDate = null
					incentiveInstance.checkedOutToWhom = null
					incentiveInstance.checkedOutByWhom = null
					incentiveInstance.checkedOut = false
					incentiveInstance.dateCheckedOut = null
					incentiveInstance.userUpdated = username

					// Attempt to save
					if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush: true)) {
						result.success = true
						// Update transaction log
						new IncentiveTransactionLog(incentive:incentiveInstance, 
							transactionDate:new Date(),
							givenToPerson:false,
							checkedOutInToWhom:"unassigned",
							checkedOutInByWhom:username).save(flush:true)
						
						//flash.message = "${message(code: 'default.updated.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
						flash.message = msg
						redirect(action: "unassignIncentive")
					} else {
						result.success = false
						result.errorText = "An error occurred. Incentive could not be unassigned."
						render(view: "assignIncentive", model:[result:result])
						return false
					}
				} else {
						result.errorText = "This incentive has not been assigned. Nothing to do."
						//render(template:'assignErrorForm', model:[result:result])
						result.incentiveBarcode = barcodeValue
						render(view: "assignIncentive", model:[result:result])
						return false
				}
			} else {
				result.errorText = "Incentive not found with barcode: ${barcodeValue}"
				//render(template:'assignErrorForm', model:[result:result])
				render(view: "assignIncentive", model:[result:result])
				return false
			}
		} else {
			result.errorText = "No incentive barcode entered. Please try again"
			//render(template:'assignErrorForm', model:[result:result])
			render(view: "assignIncentive", model:[result:result])
			return false
		}
		
	}
	
	def transactionLog = {
		def incentiveInstance = Incentive.get(params.id)
		if (!incentiveInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'giftCard.label', default: 'GiftCard'), params.id])}"
			redirect(action: "list")
		}
		else {
			def incentiveTransactionLogList = incentiveInstance.transactionLogs
			return [incentiveInstance: incentiveInstance
				, incentiveTransactionLogList: incentiveTransactionLogList.sort{ a,b -> b?.transactionDate <=> a?.transactionDate }
				, incentiveTransactionLogTotal: incentiveTransactionLogList.size() ]
		}
	}

	def printableIncentiveList = {
		if (params?.id) {
			def incentiveInstanceList = Incentive.findAllByCheckedOutToWhomAndTrackedItem(params?.id, null)
			// Get NCS group members for list of interviewers
			//def groupName = "EnHS-NCS"
			
			memberInstanceList = directoryService.getMembers()
			if ( ! memberInstanceList ) {
				directoryService.loadUsersByGroupname(groupName)
				memberInstanceList = directoryService.getMembers()
			}
			return [incentiveInstanceList: incentiveInstanceList
				, interviewer: memberInstanceList.find{ it.username == params?.id }]
		}
	}

	def findAppointments = {
		// prep all the things we'll need to send back
		def result = [
			success: false,
			appointments: [],
			errorText: ""
		]

		if (params?.id){
			def personId = params?.id.toLong()
			
			if (debug) {
				println "personId: ${personId}"
			}
			
			// Check for the person
			def personInstance = Person.read(personId)
			if (personInstance) {
				if (debug) {
					println "person:${personInstance}"
				}
				// Get all the appointments
				def appointments = []
				Appointment.findAllByPerson(personInstance).each {
					appointments << [ id:it.id, name:it.type.name ]
				}
				result.appointments = appointments
				result.success = true
			}
			else {
				result.errorText = "Person not found"
			}
		}

		render result as JSON
	}
	
	def activateIncentives = { }
	
	def activateByReceiptNumber = {
		/**
		 * Activate all incentives with a given receipt number
		 */
		def username = springSecurityService.principal.getUsername()
		
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
			receiptNumber: "",
			incentiveCount:0,
			divId: 0,
			errorText: ""
		]

		// if a div ID was passed, let's save it to the result set
		if (params?.divId) {
			result.divId = params.divId
		}

		if (params?.id){
			def receiptNumber = params?.id
			def incentiveCount = 0
			if (receiptNumber) {
				def incentiveInstanceList = Incentive.findAllByReceiptNumber(receiptNumber)
				if (incentiveInstanceList) {
					incentiveInstanceList.each {
						if (!it?.activated) {
							it.activated = true
							it.dateActivated = new Date()
							it.userUpdated = username
							it.lastUpdated = new Date()
							if (it.save(flush: true)) {
								incentiveCount++
							}
						}
					}
					result.success = true
					result.receiptNumber = receiptNumber
					result.incentiveCount = incentiveCount
				}
				else {
					result.errorText = "No incentives found with receipt number: ${params?.id}"					
				}
			}
		}
		else {
			result.errorText = "Invalid receipt number:${params?.id}"
		}
		if (debug) {
			println "activate incentive result::${result}"
		}
		render result as JSON

	}

}
