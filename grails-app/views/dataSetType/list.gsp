

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
                        
                            <g:sortableColumn property="closure" title="${message(code: 'dataSetType.closure.label', default: 'Closure')}" />
                        
                            <g:sortableColumn property="sqlQuery" title="${message(code: 'dataSetType.sqlQuery.label', default: 'Sql Query')}" />
                        
                            <g:sortableColumn property="closureTested" title="${message(code: 'dataSetType.closureTested.label', default: 'Closure Tested')}" />
                        
		</tr>
	</thead>
	<tbody>
		<g:each in="${dataSetTypeInstanceList}" status="i" var="dataSetTypeInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				
                            <td><g:link action="show" id="${dataSetTypeInstance.id}">${fieldValue(bean: dataSetTypeInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: dataSetTypeInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: dataSetTypeInstance, field: "code")}</td>
                        
                            <td>${fieldValue(bean: dataSetTypeInstance, field: "closure")}</td>
                        
                            <td>${fieldValue(bean: dataSetTypeInstance, field: "sqlQuery")}</td>
                        
                            <td><g:formatBoolean boolean="${dataSetTypeInstance.closureTested}" /></td>
                        
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
