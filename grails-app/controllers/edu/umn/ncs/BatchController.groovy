package edu.umn.ncs

// Let's us use security annotations
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DOCGEN'])
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
