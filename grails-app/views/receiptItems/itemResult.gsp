	<g:if test="${success}">
		SUCCESS. ${studyName} ${instrumentName}, Receipt Date: ${resultDate}, Result: '${resultName}'
	</g:if>
	<g:else>
		FAIL. ${studyName} ${instrumentName}, Error: ${errorText}
	</g:else>
