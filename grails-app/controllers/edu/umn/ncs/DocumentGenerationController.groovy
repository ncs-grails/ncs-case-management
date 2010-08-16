package edu.umn.ncs

class DocumentGenerationController {

    def index = { redirect(action:'search', params:params) }
	
	// find a mailing type
	def search = {}

	// show a mailing type
	def show = {}

	// reprint a previously generated mailing
	def reprint = {}

	// generate a new mailing automatically
	def autoGenerate = {}

	// manually generate a mailing
	def manualGenerate = {}

	// display a batch report
	def batchReport = {}

}
