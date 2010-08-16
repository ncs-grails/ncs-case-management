package edu.umn.ncs

class DwellingUnitController {

    def index = {
		redirect(action:list,params:params)
	}

	def list = {
		println "App Name: ${grailsApplication.metadata['app.name']}"

		def dwellingUnitList = DwellingUnit.list(params)
		return [ dwellingUnitList: dwellingUnitList]
	}

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
}
