<g:form controller="batchCreationDocument" action="updateComment">
	<g:hiddenField name="id" value="${batchCreationDocument?.id}" />
	<g:textArea name="comment" value="${batchCreationDocument?.comment}" class="bundle-note" />
	<g:submitToRemote update="batchCreationDocument-${batchCreationDocument?.id}-comment"
		url="${[controller:'batchCreationDocument', action:'updateComment', id:batchCreationDocument?.id ]}" value="Save" />
	<g:submitToRemote update="batchCreationDocument-${batchCreationDocument?.id}-comment"
		url="${[controller:'batchCreationDocument', action:'showComment', id:batchCreationDocument?.id ]}" value="Cancel" />
</g:form>
