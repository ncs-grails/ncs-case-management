<div class="body">
	<h1>Choose Item</h1>
	<h3>Please choose the tracked item associated with the NORC Mailing ID you just scanned in.</h3>
	<fieldset class="maroonBorder">
		<legend>Linked Items</legend>
		<div class="list">
			<g:form action="findItem">
				<g:hiddenField name="queueId" value="${queueId}"/>
				<g:hiddenField name="useParentItem" value="${useParentItem}"/>
				<g:hiddenField name="batchCreationConfig.id" value="${batchCreationConfigId}"/>
				<g:hiddenField name="batchCreationQueueSource.id" value="${batchCreationQueueSourceId}"/>
				<ul>
					<g:each var="trackedItemInstance" in="${trackedItemInstanceList}">
						<li>
							<input type="radio" name="id" value="I${trackedItemInstance.id}" />
							${trackedItemInstance.id} : 
							${trackedItemInstance.batch.primaryInstrument.study}
							${trackedItemInstance.batch.primaryInstrument.name}
							Generated: ${formatDate(date:trackedItemInstance.batch.dateCreated, format:'M/d/yyyy')}
							Mailed: ${formatDate(date: trackedItemInstance.batch.mailDate, format: 'M/d/yyyy')}
						</li>
					</g:each>				
				</ul>
				<g:submitToRemote url="${[action: 'findItem', controller: 'documentGeneration']}" update="queueAddResult-${queueId}" name="Submit" value="Submit" />
				<g:submitToRemote url="${[action: 'cancelItem', controller: 'documentGeneration']}" update="queueAddResult-${queueId}" name="Cancel" value="Cancel"/>
			</g:form>		
		</div>
	</fieldset>
</div>		