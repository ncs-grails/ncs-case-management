

<%@ page import="edu.umn.ncs.DataSetType" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'dataSetType.label', default: 'DataSetType')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="create" action="create">
	<g:message code="default.new.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.list.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if>
<div class="list">
<table>
	<thead>
		<tr>
			<g:sortableColumn property="id" title="${message(code: 'dataSetType.id.label', default: 'Id')}" />
			<g:sortableColumn property="name" title="${message(code: 'dataSetType.name.label', default: 'Name')}" />
			<g:sortableColumn property="code" title="${message(code: 'dataSetType.code.label', default: 'Code')}" />
		</tr>
	</thead>
	<tbody>
		<g:each in="${dataSetTypeInstanceList}" status="i" var="dataSetTypeInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td><g:link action="edit" id="${dataSetTypeInstance.id}">${fieldValue(bean: dataSetTypeInstance, field: "id")}</g:link></td>
				<td>${fieldValue(bean: dataSetTypeInstance, field: "name")}</td>
				<td>${fieldValue(bean: dataSetTypeInstance, field: "code")}</td>
			</tr>
		</g:each>
	</tbody>
</table>
</div>
<div class="paginateButtons"><g:paginate
	total="${dataSetTypeInstanceTotal}" /></div>
</div>
</body>
</html>
