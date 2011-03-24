package edu.umn.ncs

class AlertNorcJob {

	def emailService

	static triggers = {

		// http://www.quartz-scheduler.org/docs/tutorials/crontrigger.html

		// run at 12:30 AM

		// Seconds Minutes Hours DayOfMonth Month DayOfWeek [ Year ]
		// WARNING: the name must be UNIQUE throughout your app
		cron name:'cronAlertNorcTrigger', cronExpression:'0 30 0 * * ?'
	}

    def execute() {
		def now = new Date()
		emailService.sendNorcAlert()
        println "Sent NORC Alert @ ${now}"
    }
}
