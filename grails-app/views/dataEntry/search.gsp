<%@ page contentType="text/html;charset=UTF-8"%>
<g:if test="${trackedItemInstance}">
<table>
	<thead>
		<tr>
			<th>Tracked Item ID</th>
			<th>Direction</th>
			<th>Description</th>
			<th>Format</th>
			<th>Data Entry Form</th>
		</tr>
	</thead>
	<tbody>
			<tr>
			<td> ${trackedItemInstance?.id} </td>
			<td> ${trackedItemInstance?.batch?.direction.toString().capitalize()} </td>
			<td> ${trackedItemInstance?.batch?.primaryInstrument} </td>
			<td> ${trackedItemInstance?.batch?.format.toString().capitalize()} </td>
			<td>
				<ul>
					<g:each var="c" in="${controllerList}">
					<li>
					<g:link controller="${c.controller}" action="${c.action}" params="${c.params}">Enter Form</g:link>
					</li>
					</g:each>
					<g:if test="${ ! controllerList}">
						<li>No Entry Available</li>
					</g:if>
				</ul>
				</td>
			</tr>
	</tbody>
</table>
</g:if>
<g:else>
Tracked Item Not Found.
</g:else>
