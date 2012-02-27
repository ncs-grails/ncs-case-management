<g:if test="${it.description}">${it.description}</g:if>
<g:else><em>No Description</em></g:else>
<g:remoteLink 
	controller="batchCreationConfig"
	action="editDescription"
	id="${it.id}"
	update="batchCreationConfig-${it.id}-description"
	class="edit">Edit</g:remoteLink>
