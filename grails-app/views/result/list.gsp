

<%@ page import="edu.umn.ncs.Result" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'result.label', default: 'Result')}" />
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
			
                            <g:sortableColumn property="id" title="${message(code: 'result.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'result.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="abbreviation" title="${message(code: 'result.abbreviation.label', default: 'Abbreviation')}" />
                        
                            <th><g:message code="result.format.label" default="Format" /></th>
                        
                            <g:sortableColumn property="ineligible" title="${message(code: 'result.ineligible.label', default: 'Ineligible')}" />
                        
                            <g:sortableColumn property="resend" title="${message(code: 'result.resend.label', default: 'Resend')}" />
                        
		</tr>
	</thead>
	<tbody>
		<g:each in="${resultInstanceList}" status="i" var="resultInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				
                            <td><g:link action="show" id="${resultInstance.id}">${fieldValue(bean: resultInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: resultInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: resultInstance, field: "abbreviation")}</td>
                        
                            <td>${fieldValue(bean: resultInstance, field: "format")}</td>
                        
                            <td><g:formatBoolean boolean="${resultInstance.ineligible}" /></td>
                        
                            <td><g:formatBoolean boolean="${resultInstance.resend}" /></td>
                        
			</tr>
		</g:each>
	</tbody>
</table>
</div>
</body>
</html>
