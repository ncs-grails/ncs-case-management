package edu.umn.ncs
import grails.plugin.springcache.annotations.Cacheable

class SearchService {

    static transactional = true

	private boolean debug = true

	@Cacheable("lookupCache")
    def query(String queryString) {

		def results = []

		// set some defaults
		def numericPattern = ~/[0-9]*/
		def identifierPattern = ~/[0-9\-]*/
		def alphaPattern = ~/[a-zA-Z]*/
		def alphaNumericPattern = ~/[ a-zA-Z0-9\-#%&]*/

		def norcMailingPattern = ~/[0-9]{4}-[0-9]{10}-[0-9]{2}/
		def norcSuPattern = ~/[0-9]{10}/

		def streetAddressPattern = ~/[0-9]{1,7}[ a-zA-Z0-9\-#%&]*/

		if (debug) {
			println "SearchService:query:queryString::${queryString}"
		}

		// if the query is an ID
		if ( norcMailingPattern.matcher(queryString).matches() ) {
			if (debug) {
				println "looking for NORC Mailing Pattern ${queryString}..."
			}

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
		if ( norcSuPattern.matcher(queryString).matches() ) {

			if (debug) {
				println "looking for NORC SU ID pattern ${queryString}..."
			}

			// we found a norc SU ID... maybe.
			def dwellingUnitLinkInstance = DwellingUnitLink.findByNorcSuId(queryString)

			if ( dwellingUnitLinkInstance ) {

				if (debug) {
					println "found dwelling unit for NORC SU ID pattern ${queryString}..."
				}
				def dwellingUnitInstance = dwellingUnitLinkInstance.dwellingUnit
				
				// we found a NORC mailing barcode
				results.add([matchType: 'NORC Mailing ID',
						controller: 'dwellingUnit',
						action: 'show',
						description: dwellingUnitInstance.address.address,
						id: dwellingUnitInstance.id ])
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


			/*
			 * This worked, but I wanted to speed it up, so it's now a criteria
			 * builder.
			 *

			// Find the address using a case-insensitive LIKE
			def streetAddressInstanceList = StreetAddress.findAllByAddressIlike("${queryString}%")

			streetAddressInstanceList.eachWithIndex { address, i ->

				// see if the address is associated with a dwelling unit
				def dwellingUnitInstance = DwellingUnit.findByAddress(address)

				if (dwellingUnitInstance && i < 10 ) {

					// we found a dwelling unit by address
					results.add([matchType: 'Street Address',
							controller: 'dwellingUnit',
							action: 'show',
							description: dwellingUnitInstance.address.address,
							id: dwellingUnitInstance.id ])
				}
			}
			*/

		}

		return results
    }
}
