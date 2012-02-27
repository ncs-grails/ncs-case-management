<g:form controller="batchCreationItem" action="updateComment">
	<g:hiddenField name="id" value="${batchCreationItem?.id}" />
	<g:textArea name="comment" value="${batchCreationItem?.comment}" class="bundle-note" />
	<g:submitToRemote update="batchCreationItem-${batchCreationItem?.id}-comment"
		url="${[controller:'batchCreationItem', action:'updateComment', id:batchCreationItem?.id ]}" value="Save" />
	<g:submitToRemote update="batchCreationItem-${batchCreationItem?.id}-comment"
		url="${[controller:'batchCreationItem', action:'showComment', id:batchCreationItem?.id ]}" value="Cancel" />
</g:form>
