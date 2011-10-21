package edu.umn.ncs

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_PROTECTED'])
class IncentiveController {
	def authenticateService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "edit", params: params)
    }

    def create = {
        def incentiveInstance = new Incentive()
        incentiveInstance.properties = params
        return [incentiveInstance: incentiveInstance]
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
		def username = authenticateService.principal().getUsername()
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

    def edit = {
        def incentiveInstance = Incentive.get(params.id)
        if (!incentiveInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
			redirect(controller:"appointment", action: "edit", id: incentiveInstance.appointment.id)
        }
        else {
            return [incentiveInstance: incentiveInstance]
        }
    }

    def update = {
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
            if (!incentiveInstance.hasErrors() && incentiveInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'incentive.label', default: 'Incentive'), incentiveInstance.id])}"
				redirect(controller:"appointment", action: "edit", id: incentiveInstance.appointment.id)
            }
            else {
                render(view: "edit", model: [incentiveInstance: incentiveInstance])
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
				def appointmentInstance = incentiveInstance.appointment
                incentiveInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
				
				redirect(controller:"appointment", action: "edit", id: appointmentInstance.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
				redirect(controller:"appointment", action: "index")
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'incentive.label', default: 'Incentive'), params.id])}"
			redirect(controller:"appointment", action: "edit", id: incentiveInstance.appointment.id)
        }
    }
}
