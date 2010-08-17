package edu.umn.ncs

class DocumentGenerationController {

    def index = { redirect(action:'list', params:params) }
	
	// find a mailing type
	def list = {

		def q = params.q
		// List of matching configs per search criteria
		def batchCreationConfigInstanceList = []

		// list of recently generated mailing types
		def batchCreationConfigRecentList = []

		// if a query was passed, we'll run it
		if (q) {
			def criteria = BatchCreationConfig.createCriteria()
			batchCreationConfigInstanceList = criteria.list{
				or {
					ilike('name', "%${q}%")
					instrument {
						or {
							ilike('name', "%${q}%")
							ilike('nickName', "%${q}%")
						}
					}
				}
			}
		}

		[batchCreationConfigInstanceList:batchCreationConfigInstanceList,
			batchCreationConfigRecentList:batchCreationConfigRecentList]
	}

	// show a mailing type
	def show = {
		// return instance of config
		def batchCreationConfigInstance = BatchCreationConfig.get(params.id)


		// return list of recently run batches
		def batchInstanceList = []


		[batchCreationConfigInstance:batchCreationConfigInstance,
			batchInstanceList:batchInstanceList]

	}

	// reprint a previously generated mailing
	def reprint = {}

	// generate a new mailing automatically
	def autoGenerate = {}

	// manually generate a mailing
	def manualGenerate = {}

	// display a batch report
	def batchReport = {}

}
