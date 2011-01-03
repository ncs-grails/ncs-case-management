<%@ page contentType="text/html;charset=UTF-8" %>
<table>
  <thead>
	<tr>
	  <th>Match Type</th>
	  <th>Description</th>
	  <th>Matched ID</th>
	</tr>
  </thead>
  <tbody>
	<g:if test="${ ! results}">
	  <tr>
		<td colspan="2">No matches for ${searchString}.</td>
	  </tr>
	</g:if>
	<g:each var="r" in="${results}">
	  <tr>
		<td>${r.matchType}</td>
		<td>${r.description}</td>
		<td><g:link controller="${r.controller}" action="${r.action}" id="${r.id}">
		  ${r.id}</g:link></td>
	  </tr>
	</g:each>
  </tbody>
</table>
