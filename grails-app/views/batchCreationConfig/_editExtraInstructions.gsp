<g:form controller="batchCreationConfig" action="updateExtraInstructions">
	<g:hiddenField name="id" value="${batchCreationConfig?.id}" />
	<g:textArea name="extraInstructions" value="${batchCreationConfig?.extraInstructions}" class="bundle-note" />
	<g:submitToRemote update="batchCreationConfig-${batchCreationConfig?.id}-extraInstructions"
		url="${[controller:'batchCreationConfig', action:'updateExtraInstructions', id:batchCreationConfig?.id ]}" value="Save" />
	<g:submitToRemote update="batchCreationConfig-${batchCreationConfig?.id}-extraInstructions"
		url="${[controller:'batchCreationConfig', action:'showExtraInstructions', id:batchCreationConfig?.id ]}" value="Cancel" />
</g:form>

