package edu.umn.ncs.instruments

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import edu.umn.ncs.TrackedItem
import edu.umn.ncs.StreetAddress
import edu.umn.ncs.Country

@Secured(['ROLE_NCS_PROTECTED'])
class EligibilityQuestionnaireController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	private static appCreated = 'ncs-case-management'
	private static debug = false
	
	def authenticateService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [eligibilityQuestionnaireInstanceList: EligibilityQuestionnaire.list(params), eligibilityQuestionnaireInstanceTotal: EligibilityQuestionnaire.count()]
    }

    def create = {
        def eligibilityQuestionnaireInstance = new EligibilityQuestionnaire()
        eligibilityQuestionnaireInstance.properties = params


		// Set the street address of the dwelling unit if we can find it.
		if ( eligibilityQuestionnaireInstance.trackedItem ) {
			// get the tracked item ID
			def trackedItemInstance = TrackedItem.read(eligibilityQuestionnaireInstance.trackedItem.id)

			// look up an E.Q. with the same tracked item
			def eq = EligibilityQuestionnaire.findByTrackedItem(trackedItemInstance)
			if (eq) {
				flash.message = "${message(code:'eligibilityQuestionnaire.create.alreadyExists')}"
				redirect(action:edit, id:eq.id)
			} else {
				// set the default country if it's not already set
				if ( ! eligibilityQuestionnaireInstance.country ) {
					eligibilityQuestionnaireInstance.country = Country.findByAbbreviation('us')
				}

				eligibilityQuestionnaireInstance.useExistingStreetAddress = trackedItemInstance?.dwellingUnit?.address
				return [eligibilityQuestionnaireInstance: eligibilityQuestionnaireInstance]
			}

		} else {
			flash.message = "${message(code:'eligibilityQuestionnaire.create.missingTrackedItem')}"
			redirect(controller:'dataEntry', action:'index')
		}
    }

    def save = {
		def user = authenticateService.principal()

        def eligibilityQuestionnaireInstance = new EligibilityQuestionnaire(params)
		eligibilityQuestionnaireInstance.appCreated = appCreated
		eligibilityQuestionnaireInstance.userCreated = user.username
        if (eligibilityQuestionnaireInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), eligibilityQuestionnaireInstance.trackedItem.id])}"
            //redirect(action: "show", id: eligibilityQuestionnaireInstance.id)
			redirect(action:'index', controller:'dataEntry')
        }
        else {

			// get the tracked item ID
			def trackedItemInstance = TrackedItem.read(eligibilityQuestionnaireInstance?.trackedItem?.id)
			eligibilityQuestionnaireInstance.useExistingStreetAddress = trackedItemInstance?.dwellingUnit?.address

            render(view: "create", model: [eligibilityQuestionnaireInstance: eligibilityQuestionnaireInstance])
        }
    }

    def show = {
        def eligibilityQuestionnaireInstance = EligibilityQuestionnaire.get(params.id)
        if (!eligibilityQuestionnaireInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), params.id])}"
            redirect(action: "list")
        }
        else {
            [eligibilityQuestionnaireInstance: eligibilityQuestionnaireInstance]
        }
    }

    def edit = {
        def eligibilityQuestionnaireInstance = EligibilityQuestionnaire.get(params.id)
        if (!eligibilityQuestionnaireInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [eligibilityQuestionnaireInstance: eligibilityQuestionnaireInstance]
        }
    }

    def update = {
		if (debug) { println "params: ${params}" }

        def eligibilityQuestionnaireInstance = EligibilityQuestionnaire.get(params.id)

		if (params['useExistingStreetAddress'] && ! params?.useExistingStreetAddress?.id ) {
			if (debug) { println "removing: useExistingStreetAddress" }
			eligibilityQuestionnaireInstance.useExistingStreetAddress = null
		}

        if (eligibilityQuestionnaireInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (eligibilityQuestionnaireInstance.version > version) {
                    
                    eligibilityQuestionnaireInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire')] as Object[], "Another user has updated this EligibilityQuestionnaire while you were editing")
                    render(view: "edit", model: [eligibilityQuestionnaireInstance: eligibilityQuestionnaireInstance])
                    return
                }
            }
            eligibilityQuestionnaireInstance.properties = params
            if (!eligibilityQuestionnaireInstance.hasErrors() && eligibilityQuestionnaireInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), eligibilityQuestionnaireInstance.trackedItem.id])}"
                // redirect(action: "show", id: eligibilityQuestionnaireInstance.id)
				redirect(action:'index', controller:'dataEntry')
            }
            else {
                render(view: "edit", model: [eligibilityQuestionnaireInstance: eligibilityQuestionnaireInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), params.id])}"
            redirect(action: "list")
        }
    }

	@Secured('ROLE_NCS_IT')
    def delete = {

        def eligibilityQuestionnaireInstance = EligibilityQuestionnaire.get(params.id)
        if (eligibilityQuestionnaireInstance) {
            try {
                eligibilityQuestionnaireInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire'), params.id])}"
            redirect(action: "list")
        }
    }
}
