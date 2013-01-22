	<g:if test="${success}">
		Successful Receipt of ${studyName} ${instrumentName}: Tracked Item Id ${trackedItemId}, Receipt Date of ${resultDate}, Result of '${resultName}'
	</g:if>
	<g:else>
		Failed Receipt of ${studyName} ${instrumentName}: Tracked Item Id ${trackedItemId}, Error due to '${errorText}'
	</g:else>
