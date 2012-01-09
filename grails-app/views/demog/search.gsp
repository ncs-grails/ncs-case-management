<%@ page contentType="text/html;charset=UTF-8"%>
<g:if test="${personInstance}">
	<table>
		<thead>
			<tr>
				<th>Person ID </th>
				<th>Name</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><g:link controller="demog" action="change" id="${personInstance?.id}">${personInstance?.id}</g:link></td>
				<td>${personInstance?.fullName}</td>
			</tr>
		</tbody>
	</table>
</g:if>
