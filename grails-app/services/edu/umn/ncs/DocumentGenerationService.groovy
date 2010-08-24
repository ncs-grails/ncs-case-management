package edu.umn.ncs

/* This class is based on the C# class written by Aaron J. Zirbes @ umn.edu
 * located here:
 *
 *   https://svn.cccs.umn.edu/svn/VisualStudio/2008.CS.NET/_general/DocumentGeneration/Objects/clsDocGen.cs
 *
 * NOTE, the aformationed code is not open source, but this link is left in here
 * for historical reference */
class DocumentGenerationService {

    static transactional = true

    def generateMailing(BatchCreationConfig config, Map params) {
        /*
         *
         * Params should contain...
         *	config		: BatchCreationConfig
         *	mailDate	: Date
         *	maxPieces	: Integer
         *	manual		: Boolean
         *	reason		: String
         *	instructions    : String
         *	comments	: String
         *	username        : String
         *
         **/

        if (config && username) {
            // TODO: Verify Selection Criteria Columns
            // TODO: Replace SQL Variables with Data
            // TODO: Replace SELECT TOP N or LIMIT 0, N with MaxPeices
            //      Note: Check Oracle and Postgres TOP N code
            // Verify result set is not empty

            // Create Primary Batch
            //   Create Attachments of Primary Batch
            // Create Sister and Child Batches
            //   Create Attachments of Sister and Child Batches
            //   Note Child's Parent Batch.id

            // Create TrackedItems (sid)
            //   If Primary Item
            //     ParentItem = parent_item from recordset
            //   Else
            //     ParentItem = id Of Parent in current batch
            //   ...if OCS, put in call system (later...)

            // Run Post-Generation SQL

            // Done!
            // TODO: for controller
            //   Open Batch Report if set to true
            //   Display link to D/L merge data, and show where to save it
            //   Display link to merge documents

        }
    }

    def generateMergeData() {

    }
	
    def getDocumentLocation() {

    }

}
