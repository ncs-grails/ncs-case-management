<fieldset class="maroonBorder">
	<legend>${batchCreationConfigInstance.name} - Details</legend>
	<h2>Selection Criteria</h2>
	<p>${batchCreationConfigInstance?.description}</p>

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
				<td>${i.relation}</td>
			</tr>
			<g:if test="${i.comment}">
			<tr><td/><td colspan="4" class="message">${i.comment}</td></tr>
			</g:if>
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
				<td><g:link action="downloadDocument" id="${d.id}">${d.documentLocation}</g:link></td>
				<td>${d.mergeSourceFile}</td>
				<td><g:include action="documentLastModified" id="${d.id}" /></td>
			</tr>
			<g:if test="${d.comment}">
			<tr><td colspan="3" class="message">${d.comment}</td></tr>
			</g:if>
			</g:each>
		</tbody>
	</table>

</fieldset>
