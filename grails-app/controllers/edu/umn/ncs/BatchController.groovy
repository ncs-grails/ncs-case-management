package edu.umn.ncs

class BatchController {

    def index = { 
		redirect(action:'show',params:params)
	}

	def show = {
		def batchInstance = Batch.get(params.id)

		if (!batchInstance) {
			redirect(action:'noneFound',params:params)
		}
		[batchInstance:batchInstance]
	}
	
	def noneFound = {}
}
