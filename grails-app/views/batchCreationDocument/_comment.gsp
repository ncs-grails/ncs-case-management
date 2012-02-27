<g:if test="${it.comment}">${it.comment}</g:if>
<g:else><em>No Comment</em></g:else>
<g:remoteLink 
	controller="batchCreationDocument"
	action="editComment"
	id="${it.id}"
	update="batchCreationDocument-${it.id}-comment"
	class="edit">Edit</g:remoteLink>

