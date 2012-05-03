package edu.umn.ncs
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class LookupService {

	static transactional = true

	def resultHistory(resultHistoryList, itemResultIdList, trackedItemInstanceList){

		// Find Matching Audit Events for ItemResults       
		def auditLogEventInstanceList = []
		if (itemResultIdList) {
			auditLogEventInstanceList = AuditLogEvent.createCriteria().list{
				eq('className', 'edu.umn.ncs.ItemResult')
				eq('propertyName', 'result')
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
			item.oldResult = null

			try {
				def resultId = it?.oldValue?.replace('edu.umn.ncs.Result : ', '')?.toInteger()
				item.oldResult = Result.read(resultId)
			} catch(NumberFormatException ex) {
				log.debug "Logging result changed on 2012-05-03. But we still have old logged results. This catch should stay here."
			}

			try {
				item.oldResult = Result.findByName(it?.oldValue)
			} catch(NumberFormatException ex) {
				log.debug ex
			}

			if (item.oldResult) {
				resultHistoryList.add(item)
			}
		}
		return resultHistoryList
	}
}
