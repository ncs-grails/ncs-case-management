<fieldset class="maroonBorder">
	<legend>${batchCreationConfigInstance.name} - Details</legend>
	<h2>Selection Criteria</h2>
	<p id="batchCreationConfig-${batchCreationConfigInstance.id}-description">
		<g:render template="/batchCreationConfig/description" bean="${batchCreationConfigInstance}" />
	</p>

	<h2>Instruments</h2>
	<table>
		<thead><tr>
				<th>&nbsp;</th>
				<th>Instrument</th>
				<th>Direction</th>
				<th>Format</th>
				<th>Type</th>
		</tr></thead>
		<tbody>
			<tr>
				<td style="width:2em;">&nbsp;</td>
				<td>${batchCreationConfigInstance.instrument}</td>
				<td>${batchCreationConfigInstance.direction}</td>
				<td>${batchCreationConfigInstance.format}</td>
				<td>MASTER</td>
			</tr>
			<g:each var="i" in="${batchCreationConfigInstance.subItems}">
			<tr>
				<td style="width:2em;">&nbsp;</td>
				<td>${i.instrument}</td>
				<td>${i.direction}</td>
				<td>${i.format}</td>
				<td>${i.relation}
					<g:if test="${i.optional}">(optional)</g:if>
				</td>
			</tr>
			<tr><td/><td colspan="4" class="message" id="batchCreationItem-${i.id}-comment">
				<g:render template="/batchCreationItem/comment" bean="${i}" />
			</td></tr>
			</g:each>
		</tbody>
	</table>

	<h2>Documents</h2>
	<table>
		<thead><tr>
				<th>Document</th>
				<th>Merge Source</th>
				<th>Last Modified</th>
		</tr></thead>
		<tbody>
			<g:each var="d" in="${batchCreationConfigInstance.documents}">
			<tr>
				<td style="font-size: 0.9em;"><g:link action="downloadDocument" id="${d.id}">${d.documentLocation}</g:link></td>
				<td style="font-size: 0.9em;">${d.mergeSourceFile}</td>
				<td><g:include controller="documentGeneration" action="documentLastModified" id="${d.id}" /></td>
			</tr>
			<tr><td colspan="4" class="message" id="batchCreationDocument-${d.id}-comment">
				<g:render template="/batchCreationDocument/comment" bean="${d}" />
			</td></tr>
			</g:each>
		</tbody>
	</table>
	<h2>Extra Instructions</h2>
	<p id="batchCreationConfig-${batchCreationConfigInstance.id}-extraInstructions">
		<g:render template="/batchCreationConfig/extraInstructions" bean="${batchCreationConfigInstance}" />
	</p>

</fieldset>
