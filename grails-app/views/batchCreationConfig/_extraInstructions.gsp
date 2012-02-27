<g:if test="${it.extraInstructions}">${it.extraInstructions}</g:if>
<g:else><em>No Extra Instructions</em></g:else>
<g:remoteLink 
	controller="batchCreationConfig"
	action="editExtraInstructions"
	id="${it.id}"
	update="batchCreationConfig-${it.id}-extraInstructions"
	class="edit">Edit</g:remoteLink>
