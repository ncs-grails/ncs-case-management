package edu.umn.ncs

class EventTriggerService {

    static transactional = true
	static debug = true

    def processEventTriggers(String username) {
		processBirthEvents(username)
    }
	
	// This will find all reports of a birth
	// and create person records for each child's
	// birth that was reported, then create relationships
	// between each child, and the household.
	// each child will only have a date of birth and a 
	// default last name matching the mother, and 
	// a default first name of "child #" + N
	def processBirthEvents(String username) throws RuntimeException {
		def appCreated = 'eventTriggerService.processBirthEvents'
		
		// Find or create all required relationships
		def childRelation = RelationshipType.findByName('child')
		if ( ! childRelation ) {
			childRelation = new RelationshipType(name: 'child').save(flush:true)
		}
		
		def motherRelation = RelationshipType.findByName('mother')
		if ( ! motherRelation ) {
			motherRelation = new RelationshipType(name: 'mother', bidirectional:true, inverse:'child').save(flush:true)
		}
		
		if (childRelation.inverse?.id != motherRelation?.id) {
			childRelation.refresh()
			childRelation.inverse = motherRelation
			childRelation.save(flush:true)
		}

		
		// Look up all birth event type.
		def birthEventType = EventType.findByName('Birth')
		// TODO: check that relations exist
		if (birthEventType) {
			
			// Look up all birth events
			// TODO: look up all birth events that have been created/updated in the last 7 days
			def eventOfInsterestInstanceList = EventOfInterest.createCriteria().list {
				eventType {
					idEq(birthEventType.id)
				}
			}
			
			// for all birth events
			eventOfInsterestInstanceList.each { eoi ->
				// get the mother's ID
				def personInstance = eoi.eventReport.person
				// get dob of child(ren)
				def childBirthDate = eoi.eventDate
				// get # of children born
				def numberOfChildren = eoi.eventCode ?: 1
				// get info source date
				def infoSourceDate = eoi.eventReport.contactDate
				
				// get the first household created that the mother belongs to
				def householdInstance = Household.createCriteria().get{
					people {
						idEq(personInstance.id)
					}
					order "dateCreated", "asc"
					maxResults 1
				}
				
				
				// look for children with the matching birth date
				// for this particular mother
				def children = PersonRelationship.createCriteria().list{
					and {
						person {
							idEq(personInstance.id)
						}
						relation {
							idEq(motherRelation.id)
						}
						relatedPerson {
							eq('birthDate', childBirthDate)
						}
					}
				}
				
				// If there aren't as many children as the EOI says there should be ...
				def childCount = children?.size() ?: 0
				
				// As long as the total number of children born on that day is less than the number
				// of children required...
				while ( childCount < numberOfChildren) {
					
					// set first/last name
					def firstName = "Child #${childCount + 1}"
					def lastName = personInstance.lastName
					// Create new person
					def childPersonInstance = new Person(firstName: firstName,
						lastName: lastName, alive:true, isRecruitable:false, 
						birthDate: childBirthDate, userCreated: username, appCreated:appCreated)
					
					if (childPersonInstance.save(flush:true)) {
						// add to household
						householdInstance.addToPeople(childPersonInstance)
						if ( ! householdInstance.save(flush:true)) {
							throw new RuntimeException("Failed to add child to househould!")
						}
						// add child->mother relation
						def childMotherRelation = new PersonRelationship(person: personInstance, 
							relation: motherRelation, relatedPerson: childPersonInstance,
							userCreated: username, appCreated: appCreated, infoDate:infoSourceDate)
						if (! childMotherRelation.save(flush:true)) {
							throw new RuntimeException("Failed to add child -> mother relation.")
						}
						// add mother->child relation
						def motherChildRelation = new PersonRelationship(person: childPersonInstance, 
							relation: childRelation, relatedPerson: personInstance,
							userCreated: username, appCreated: appCreated, infoDate:infoSourceDate )
						
						if (! motherChildRelation.save(flush:true)) {
							throw new RuntimeException("Failed to add mother -> child relation.")
						}
					} else {
						throw new RuntimeException("Failed to create new child for mother, Person.id: ${personInstance.id}")
					}
					childCount++
				}
			}
		} else if (debug) {
			println "Couldn't find Birth event.  We're not going to do anything"
		}
		
	} 
	
}
