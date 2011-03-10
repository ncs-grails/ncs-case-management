package edu.umn.ncs
import groovy.sql.Sql

class PingDatabaseJob {

	def dataSource
	
		static triggers = {
	
			// http://www.quartz-scheduler.org/docs/tutorials/crontrigger.html
	
			// run every two minutes
			// Seconds Minutes Hours DayOfMonth Month DayOfWeek [ Year ]
			// WARNING: the name must be UNIQUE throughout your app
			cron name: 'cronPingTrigger', cronExpression: "0 */10 * * * ?"
		}
	
		def execute() {
			
			def now = new Date()
			def sql = new Sql(dataSource)
			def query = "SELECT 1 AS OK"
			def success = false
			sql.eachRow(query){
				def result = it['OK']
				if (result == 1) {
					success = true
				}
			}
		}
	}
