package edu.umn.ncs

class BotherAaronJob {
	
    def emailService

	static triggers = {

		// http://www.quartz-scheduler.org/docs/tutorials/crontrigger.html

		// run every hour on the hour

		// Seconds Minutes Hours DayOfMonth Month DayOfWeek [ Year ]
		// WARNING: the name must be UNIQUE throughout your app
		cron name: 'cronAaronTrigger', cronExpression: "0 0 * * * ?"
	}
	
    def execute() {
        // execute task
		
		println "Bothering Aaron Now..."
		emailService.sendAaronNote()
		
    }
}
