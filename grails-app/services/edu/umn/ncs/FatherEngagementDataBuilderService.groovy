package edu.umn.ncs

import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
// Security stuff
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import grails.plugin.springcache.annotations.Cacheable
import grails.plugin.springcache.annotations.CacheFlush

@Secured(['ROLE_NCS_IT','ROLE_NCS'])
class FatherEngagementDataBuilderService {
	static def debug = false
    static transactional = true

	//static def csvDateFormat = 'yyyy-MM-dd HH:mm:ss'
	static def csvDateFormat = 'yyyy-MM-dd'
	static def csvTimeFormat = 'HH:mm'
	
    def serviceMethod() {

    }
	
	def generateData(List<FatherEngagement> fatherEngagementList) {
		def dataset = []
						
		def fmt = DateTimeFormat.forPattern(csvDateFormat)
		def tfmt = DateTimeFormat.forPattern(csvTimeFormat)
		
		def startTime = new GregorianCalendar().time.time
		def splitTime = startTime
		
		if (debug) {
			def newSplitTime = new GregorianCalendar().time.time
			println "Begin"
			println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
			splitTime = newSplitTime
		}
		
		fatherEngagementList.each {
			if (debug) {
				println "interviewDate::${fmt.print(new DateTime(it?.interviewDate))}"
			}
			def record = [
				tracked_item_id:it.trackedItem.id,
				norc_su_id:PersonLink.findByPerson(it.trackedItem.person).norcSuId,								
				interviewer_initials:it?.interviewerInitials,
				interview_date:fmt.print(new DateTime(it?.interviewDate)),
				interview_start_time:tfmt.print(new DateTime(it?.interviewStartTime)),
				interview_end_time:it?.interviewEndTime ? tfmt.print(new DateTime(it?.interviewEndTime)): '',
				father_present:it?.fatherPresent,
				discussed_need_to_know:it?.discussNeedToKnow,
				father_signed_as_witness:it?.signAsWitness
			]
			dataset.add(record)
			
		}
									
		if (debug) {
			 def newSplitTime = new GregorianCalendar().time.time
			 println "Done."
			 println "Time since [started]:\t${splitTime - startTime}\t[last]:${newSplitTime - splitTime}"
			 splitTime = newSplitTime
		}
 
		//return outputData.writer.toString()
		return dataset
	}

}
