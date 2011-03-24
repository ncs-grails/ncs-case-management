class ProductionReportJob {

	def emailService

	static triggers = {

		// http://www.quartz-scheduler.org/docs/tutorials/crontrigger.html

		// run at 12:30 AM
		// Seconds Minutes Hours DayOfMonth Month DayOfWeek [ Year ]
		// WARNING: the name must be UNIQUE throughout your app
		cron name:'cronProductionReportTrigger', cronExpression:'0 30 0 * * ?'
		
	}

    def execute() {
		def now = new Date()
        println "Sending Production Report @ ${now}"
		emailService.sendProductionReport()
    }
}
