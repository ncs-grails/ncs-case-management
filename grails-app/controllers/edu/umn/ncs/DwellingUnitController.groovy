package edu.umn.ncs
// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

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

			def householdInstanceList = Household.findAllByDwelling(dwellingUnitInstance)
			
			def resultHistoryList = []
			
			// Find IDs for ItemResults
			def itemResultIdList = trackedItemInstanceList.collect{ it?.result?.id?.toString() }
			// get rid of nulls in the list
			itemResultIdList.removeAll([null])

			// Find Matching Audit Events for ItemResults		
			def auditLogEventInstanceList = []
			if (itemResultIdList) {
				def auditLogEventInstanceList = AuditLogEvent.createCriteria().list{
					eq('className', 'edu.umn.ncs.ItemResult')
					'in'('persistedObjectId', itemResultIdList)
				}
			}
			
			auditLogEventInstanceList.each{
				def item = [:]
				
				item.id = it.persistedObjectId.toInteger()
				def ti = trackedItemInstanceList.find{it.result?.id == item.id}
				item.trackedItem = TrackedItem.read(ti.id)
				item.username = it.actor
				item.dateCreated = it.dateCreated
				
				def resultId = it?.oldValue?.replace('edu.umn.ncs.Result : ', '')?.toInteger()
				item.oldResult = Result.read(resultId)
				
				if (item.oldResult) {
					resultHistoryList.add(item)
				}
			}

			[dwellingUnitInstance: dwellingUnitInstance,
				dwellingUnitLinkInstance: dwellingUnitLinkInstance,
				trackedItemInstanceList: trackedItemInstanceList,
				householdInstanceList: householdInstanceList,
				resultHistoryList: resultHistoryList ]
        }
		
	}
}
