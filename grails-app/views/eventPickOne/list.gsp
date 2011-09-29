

<%@ page import="edu.umn.ncs.EventPickOne" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'eventPickOne.label', default: 'EventPickOne')}" />
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
			
                            <g:sortableColumn property="id" title="${message(code: 'eventPickOne.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'eventPickOne.name.label', default: 'Name')}" />
                        
                            <th><g:message code="eventPickOne.eventType.label" default="Event Type" /></th>
                        
		</tr>
	</thead>
	<tbody>
		<g:each in="${eventPickOneInstanceList}" status="i" var="eventPickOneInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				
                            <td><g:link action="show" id="${eventPickOneInstance.id}">${fieldValue(bean: eventPickOneInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: eventPickOneInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: eventPickOneInstance, field: "eventType")}</td>
                        
			</tr>
		</g:each>
	</tbody>
</table>
</div>
<div class="paginateButtons"><g:paginate
	total="${eventPickOneInstanceTotal}" /></div>
</div>
</body>
</html>
