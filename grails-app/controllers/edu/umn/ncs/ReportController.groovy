package edu.umn.ncs

class ReportController {
    def reportService

    def reportDescription = [:]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [reportInstanceList: Report.list(params), reportInstanceTotal: Report.count()]
    }

    def searchBatches = {
        def q = params?.q
        println "q -> ${q}"
    }

    def batchAnalysis = {

        // child batches
        def batchInstanceList = []
        def batchInstance = Batch.get(params?.batch?.id)

        println "We got batchId -> ${batchInstance?.id}"

        def advLetterSentInstanceList = reportService.batchAnalysis(15)
        def reportInstance = Report.get(params?.id)

        render(view: "batchAnalysis", model: [advLetterSentInstanceList: advLetterSentInstanceList, advLetterSentInstanceTotal: advLetterSentInstanceList.count(), reportInstance: reportInstance])
    }

}
