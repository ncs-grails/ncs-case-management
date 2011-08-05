<g:form action="receiptItem">
	<h3>Please choose the tracked item associated with the NORC Mailing ID you just scanned in.</h3>
	<g:hiddenField name="receiptDate" value="${receiptDate}"/>
	<g:hiddenField name="divId" value="${divId}"/>
	<ul>
	<g:each var="trackedItemInstance" in="${trackedItemInstanceList}">
		<li>
			<input type="radio" name="id" value="I${trackedItemInstance.id}" />
			${trackedItemInstance.id} : 
			${trackedItemInstance.batch.primaryInstrument.study}
			${trackedItemInstance.batch.primaryInstrument.name}
			Generated: ${trackedItemInstance.batch.dateCreated}
			Mailed: ${trackedItemInstance.batch.mailDate}
			<g:if test="${trackedItemInstance.result}">
				<span class="warning">
					Warning: This item already has a result of: ${trackedItemInstance.result.result.name}
					entered on ${trackedItemInstance.result.dateCreated}.
				</span>
			</g:if>
		</li>
	</g:each>
	</ul>
	<g:submitToRemote url="${[action:'receiptItem', controller:'receiptItems']}" update="scan-${divId}-status" name="Submit" value="Submit" />
</g:form>