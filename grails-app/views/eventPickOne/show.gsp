

<%@ page import="edu.umn.ncs.EventPickOne" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'eventPickOne.label', default: 'EventPickOne')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span> <span class="menuButton"><g:link class="create" action="create">
	<g:message code="default.new.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.show.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if>
<div class="dialog">

                        <div class="prop">
                            <span class="name"><g:message code="eventPickOne.id.label" default="Id" /></span>
                            
                            <span class="value">${fieldValue(bean: eventPickOneInstance, field: "id")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eventPickOne.name.label" default="Name" /></span>
                            
                            <span class="value">${fieldValue(bean: eventPickOneInstance, field: "name")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eventPickOne.eventType.label" default="Event Type" /></span>
                            
                            <span class="value"><g:link controller="eventType" action="show" id="${eventPickOneInstance?.eventType?.id}">${eventPickOneInstance?.eventType?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
</div>
<div class="buttons"><g:form>
	<g:hiddenField name="id" value="${eventPickOneInstance?.id}" />
	<span class="button"><g:actionSubmit class="edit" action="edit"
		value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	<span class="button"><g:actionSubmit class="delete"
		action="delete"
		value="${message(code: 'default.button.delete.label', default: 'Delete')}"
		onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
</g:form></div>
</div>
</body>
</html>
