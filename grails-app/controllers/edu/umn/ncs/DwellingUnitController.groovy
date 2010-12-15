package edu.umn.ncs
// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

class DwellingUnitController {

	@Secured(['ROLE_NCS_DOCGEN'])
	def quickAdd = {

		def dwellingUnitList = DwellingUnit.list(params)

		def streetAddressInstance = new StreetAddress(params.address)

		def dwellingUnitInstance = new DwellingUnit(params)

		dwellingUnitInstance.appCreated = 'ncs-case-management'

        if(!dwellingUnitInstance.hasErrors()
			&& dwellingUnitInstance.save()) {
            flash.message = "Dwelling Unit # ${dwellingUnitInstance.id} created"
            redirect(action:list)
        }
        else {
            render(view:'list',model:[dwellingUnitInstance:dwellingUnitInstance,
					dwellingUnitList:dwellingUnitList])
        }
		
	}

	@Secured(['ROLE_NCS_LOOKUP'])
	def show = {
        def dwellingUnitInstance = DwellingUnit.read(params.id)
        if (!dwellingUnitInstance) {
			response.sendError(404)
			render "Dwelling Unit ${params.id} Not found"
        }
        else {
			def dwellingUnitLinkInstance = DwellingUnitLink.findByDwellingUnit(dwellingUnitInstance)

			def trackedItemInstanceList = TrackedItem.findAllByDwellingUnit(dwellingUnitInstance)

            [dwellingUnitInstance: dwellingUnitInstance,
				dwellingUnitLinkInstance: dwellingUnitLinkInstance,
				trackedItemInstanceList: trackedItemInstanceList]
        }
		
	}
}
