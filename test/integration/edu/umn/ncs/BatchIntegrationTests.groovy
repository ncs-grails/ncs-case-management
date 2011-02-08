package edu.umn.ncs

import grails.test.*

class BatchIntegrationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()        
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSubBatches() {

        // this only works in the test database

        def batchDirectionInstance = BatchDirection.read(1)
        def instrumentFormatInstance = InstrumentFormat.read(1)
        def batchCreationConfigInstance = BatchCreationConfig.read(1)

        def now = new Date()

        def masterBatch = new Batch(batchRunBy: 'test',
                        format: instrumentFormatInstance,
                        direction: batchDirectionInstance,
                        instrumentDate: now,
                        batchRunByWhat: 'test',
                        trackingDocumentSent: false,
                        creationConfig:batchCreationConfigInstance ).save(flush:true)

        def childBatch1 = new Batch(batchRunBy: 'test1',
                        format: instrumentFormatInstance,
                        direction: batchDirectionInstance,
                        instrumentDate: now,
                        batchRunByWhat: 'test1',
                        trackingDocumentSent: false,
                        creationConfig:batchCreationConfigInstance,
                        master: masterBatch ).save(flush:true)

        def childBatch2 = new Batch(batchRunBy: 'test2',
                        format: instrumentFormatInstance,
                        direction: batchDirectionInstance,
                        instrumentDate: now,
                        batchRunByWhat: 'test2',
                        trackingDocumentSent: false,
                        creationConfig:batchCreationConfigInstance,
                        master: masterBatch ).save(flush:true)

        def childBatch3 = new Batch(batchRunBy: 'test3',
                        format: instrumentFormatInstance,
                        direction: batchDirectionInstance,
                        instrumentDate: now,
                        batchRunByWhat: 'test3',
                        trackingDocumentSent: false,
                        creationConfig:batchCreationConfigInstance ).save(flush:true)



        def subBatchIds = masterBatch.getSubBatches().collect{ it.id }

        assert subBatchIds.contains(childBatch1.id)

        assert subBatchIds.contains(childBatch2.id)

        assert ( ! subBatchIds.contains(childBatch3.id) )

        assert ( ! subBatchIds.contains(masterBatch.id) )

    }
}
