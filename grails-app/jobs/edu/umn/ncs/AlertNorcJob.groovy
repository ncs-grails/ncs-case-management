package edu.umn.ncs

class AlertNorcJob {

	def emailService

	static triggers = {

		// http://www.quartz-scheduler.org/docs/tutorials/crontrigger.html

		// run at 12:30 AM

		// Seconds Minutes Hours DayOfMonth Month DayOfWeek [ Year ]
		// WARNING: the name must be UNIQUE throughout your app
		cron name: 'cronNorcTrigger', cronExpression: "0 30 0 * * ?"
	}

    def execute() {
		def keepTrying = true
		def attempt = 0
		def now = new Date()
        println "Sending NORC Alert @ ${now}"
		
		while ( keepTrying && (attempt < 5) ) {
			try {
				// execute task
				emailService.sendNorcAlert()
				keepTrying = false
			} catch (Exception e) {
				println "Error Occurred! Retrying in 10 seconds."
				Thread.sleep(10000)
				attempt++
			}
		}

		
    }
}
