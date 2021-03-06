package edu.umn.ncs
//import grails.plugin.springcache.annotations.Cacheable

class SearchService {

    static transactional = true

	private boolean debug = false

	//@Cacheable("lookupCache")
    def query(String queryString) {

		def results = []

		// set some defaults
		def numericPattern = ~/[0-9]*/
		def identifierPattern = ~/[0-9\-]*/
		def alphaPattern = ~/[a-zA-Z]*/
		def alphaNumericPattern = ~/[ a-zA-Z0-9\-#%&]*/

		def norcMailingPattern = ~/[0-9]{4}-[0-9]{10}-[0-9]{2}/
		def norcMailIdPattern = ~/[0-9]{10}/
		def norcSuPattern = ~/[0-9]{8}/

		def streetAddressPattern = ~/[0-9]{1,7}[ a-zA-Z0-9\-#%&]*/

		if (debug) {
			println "SearchService:query:queryString::${queryString}"
		}

		
		if ( numericPattern.matcher(queryString).matches()) {
			if (debug) {
				println "looking for an ID ${queryString}..."
			}

			def personInstance = Person.read(queryString)
			
			if (personInstance) {
				results.add([matchType:'Person ID',
					controller: 'person',
					description: personInstance.fullName,
					action: 'show',
					id: personInstance.id ])
			}

			def dwellingUnitInstance = DwellingUnit.read(queryString)
			
			if (dwellingUnitInstance) {
				results.add([matchType:'Dwelling Unit ID',
					controller: 'dwellingUnit',
					description: dwellingUnitInstance.address.address,
					action: 'show',
					id: dwellingUnitInstance.id ])
			}

			def trackedItemInstance = TrackedItem.read(queryString)
			
			if (trackedItemInstance) {
				results.add([matchType:'Tracked Item ID',
					controller: 'trackedItem',
					description: "${trackedItemInstance.batch.primaryInstrument}",
					action: 'show',
					id: trackedItemInstance.id ])
			}
		}
		
		// if the query is an ID
		if ( norcMailingPattern.matcher(queryString).matches() ) {
			if (debug) {
				println "looking for NORC Mailing Pattern ${queryString}..."
			}

			// let's search through the IDs
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
						description: "Tracked Item: ${ti.id}",
						action: 'show',
						id: ti.id ])
				}
				

				// we found a NORC mailing barcode
				results.add([matchType: 'NORC Mailing ID',
						controller: 'dwellingUnit',
						description: "Tracked Item: ${dwellingUnitInstance.streetAddress.address}",
						action: 'show',
						id: dwellingUnitInstance.id ])

			}

		}

		// 0061674400
		if ( norcMailIdPattern.matcher(queryString).matches() ) {
			// we found a norc SU ID... maybe.
			def dwellingUnitLinkInstance = DwellingUnitLink.findByNorcSuId(queryString)
			if ( dwellingUnitLinkInstance ) {
				def dwellingUnitInstance = dwellingUnitLinkInstance.dwellingUnit
				
				// we found a NORC mailing barcode
				results.add([matchType: 'NORC Mailing ID',
						controller: 'dwellingUnit',
						action: 'show',
						description: dwellingUnitInstance.address.address,
						id: dwellingUnitInstance.id ])
			}
		}
		
		
		// 61674400
		if ( norcSuPattern.matcher(queryString).matches() ) {
			// we found a norc SU ID... maybe.
			def dwellingUnitLinkInstance = DwellingUnitLink.findByNorcSuId('00' + queryString)

			if ( dwellingUnitLinkInstance ) {
				def dwellingUnitInstance = dwellingUnitLinkInstance.dwellingUnit
				
				// we found a NORC mailing barcode
				results.add([matchType: 'NORC Mailing ID',
						controller: 'dwellingUnit',
						action: 'show',
						description: dwellingUnitInstance.address.address,
						id: dwellingUnitInstance.id ])
			}
			def personLinkInstance = PersonLink.findByNorcSuId(queryString)
			if ( personLinkInstance ) {
				def personInstance = personLinkInstance.person
				
				// we found a NORC mailing barcode
				results.add([matchType: 'NORC Mailing ID',
						controller: 'person',
						action: 'show',
						description: personInstance.fullName,
						id: personInstance.id ])
			}
		}

		if (streetAddressPattern.matcher(queryString).matches()) {

			def c = DwellingUnit.createCriteria()

			def dwellingUnitInstanceList = c.list{
				address {
					ilike("address", "${queryString}%")
				}
				maxResults(10)
			}

			dwellingUnitInstanceList.each { dwellingUnitInstance ->

				// we found a dwelling unit by address
				results.add([matchType: 'Street Address',
						controller: 'dwellingUnit',
						action: 'show',
						description: dwellingUnitInstance.address.address,
						id: dwellingUnitInstance.id ])
			}
		}
		
		if (alphaPattern.matcher(queryString).matches()) {
			def c = Person.createCriteria()
			
			def personInstanceList = c.list{
				ilike("lastName", "${queryString}%")
				maxResults(20)
			}
			
			personInstanceList.each { personInstance ->
				
				// we found a dwelling unit by address
				results.add([matchType: 'Person, first name',
						controller: 'person',
						action: 'show',
						description: personInstance.fullName,
						id: personInstance.id ])
			}

			c = Person.createCriteria()
			personInstanceList = c.list{
				ilike("firstName", "${queryString}%")
				maxResults(20)
			}
			
			personInstanceList.each { personInstance ->
				
				// we found a dwelling unit by address
				results.add([matchType: 'Person, last name',
						controller: 'person',
						action: 'show',
						description: personInstance.fullName,
						id: personInstance.id ])
			}

		}

		return results
    }
}
