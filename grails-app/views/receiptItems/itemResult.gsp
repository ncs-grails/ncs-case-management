	<g:if test="${success}">
		success, received 
		${studyName}
		${instrumentName}
		, ID: 
		${trackedItemId}
		, Receipt Date:
		${resultDate}
	</g:if>
	<g:else>
		failed to receipt item: ${errorText}
	</g:else>
