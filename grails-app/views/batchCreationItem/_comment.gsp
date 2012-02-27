<g:if test="${it.comment}">${it.comment}</g:if>
<g:else><em>No Comment</em></g:else>
<g:remoteLink 
	controller="batchCreationItem"
	action="editComment"
	id="${it.id}"
	update="batchCreationItem-${it.id}-comment"
	class="edit">Edit</g:remoteLink>
