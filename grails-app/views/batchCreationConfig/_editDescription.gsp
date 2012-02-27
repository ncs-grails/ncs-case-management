<g:form controller="batchCreationConfig" action="updateDescription">
	<g:hiddenField name="id" value="${batchCreationConfig?.id}" />
	<g:textArea name="description" value="${batchCreationConfig?.description}" class="bundle-note" />
	<g:submitToRemote update="batchCreationConfig-${batchCreationConfig?.id}-description"
		url="${[controller:'batchCreationConfig', action:'updateDescription', id:batchCreationConfig?.id ]}" value="Save" />
	<g:submitToRemote update="batchCreationConfig-${batchCreationConfig?.id}-description"
		url="${[controller:'batchCreationConfig', action:'showDescription', id:batchCreationConfig?.id ]}" value="Cancel" />
</g:form>

