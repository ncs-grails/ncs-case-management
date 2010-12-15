package edu.umn.ncs

class SearchService {

    static transactional = true

    def query(String queryString) {

		results = []

		// set some defaults
		def numericPattern = ~/[0-9]*/
		def identifierPattern = ~/[0-9\-]*/
		def alphaPattern = ~/[a-zA-Z]*/
		def alphaNumericPattern = ~/[a-zA-Z0-9\-#%&]*/
		def norcMailingPattern = ~/[0-9]{4}-[0-9]{10}-[0-9]{2}/
		def norcSuPattern = ~/[0-9]{10}/


		// if the query is an ID
		if ( norcMailingPattern.matcher(queryString).matches() ) {
			// let's search throught he IDs
			def norcProjectId = queryString[0..3]
            def norcDocId = queryString[5..14]
            def norcSuId = queryString[16..17]

			def studyInstance = StudyLink.findByNorcProjectId(norcProjectId)
			def instrumentInstance = InstrumentLink.findByNorcDocId(norcDocId)
			def dwellingUnitInstance = DwellingUnitLink.findByNorcSuId(norcSuId)

			if ( studyInstance && instrumentInstance && dwellingUnitInstance ) {

				// theoretically, we could use this to find a specific
				// trackedItem, and link to that... but we'll be lazy

				// something like
				def c = TrackedItem.createCriteria()
				def trackedItemInstanceList = c.list{
					and {
						dwellingUnit {
							eq("id", dwellingUnitInstance.id)
						}
						batch {
							instruments {
								instrument {
									and {
										eq("id", instrumentInstance.id)
										study {
											eq("id", studyInstance.id)
										}
									}
								}
							}
						}
					}
				}

				trackedItemInstanceList.each{ ti
					results.add([matchType:'NORC Mailing ID',
						controller: 'trackedItem',
						action: 'show',
						id: ti.id ])
				}
				

				// we found a NORC mailing barcode
				results.add([matchType: 'NORC Mailing ID',
						controller: 'dwellingUnit',
						action: 'show',
						id: dwellingUnitInstance.id ])

			}

		}

		if ( norcSuPattern.matcher(queryString).matches() ) {
			// we found a norc SU ID... maybe.
			def dwellingUnitInstance = DwellingUnitLink.findByNorcSuId(queryString)

			if ( dwellingUnitInstance ) {
				// we found a NORC mailing barcode
				results.add([matchType: 'NORC Mailing ID',
						controller: 'dwellingUnit',
						action: 'show',
						id: dwellingUnitInstance.id ])
			}
		}
    }
}
