<g:if test="${success}">
	success, added to the queue
	<g:if test="${parentItem}">Parent Item Id: ${parentItem.id} Parent Instrument: ${parentItem?.batch?.primaryInstrument?.name}</g:if>
	<g:if test="${dwellingUnit}">Dwelling Unit Id: ${dwellingUnit.id}</g:if>
	<g:if test="${person}">Person: ${person.id} ${person.firstName} ${person.lastName}</g:if>
	<g:if test="${household}">Household Id: ${household.id}</g:if>
</g:if>
<g:else>
	failed to add item: ${errorText}
</g:else>