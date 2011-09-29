


<%@ page import="edu.umn.ncs.EventPickOne" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'eventPickOne.label', default: 'EventPickOne')}" />
<title><g:message code="default.create.label"
	args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.create.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if> <g:hasErrors bean="${eventPickOneInstance}">
	<div class="errors"><g:renderErrors bean="${eventPickOneInstance}"
		as="list" /></div>
</g:hasErrors> <g:form action="save" method="post"
	>
	<div class="dialog">
	
                            <div class="prop">
                                <span class="name">
                                    <label for="name"><g:message code="eventPickOne.name.label" default="Name" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: eventPickOneInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${eventPickOneInstance?.name}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                    <label for="eventType"><g:message code="eventPickOne.eventType.label" default="Event Type" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: eventPickOneInstance, field: 'eventType', 'errors')}">
                                    <g:select name="eventType.id" from="${edu.umn.ncs.EventType.findAllWhere(useEventPickOne:true)}" optionKey="id" value="${eventPickOneInstance?.eventType?.id}"  />
                                </span>
                            </div>
                        
	</div>
	<div class="buttons"><span class="button"><g:submitButton
		name="create" class="save"
		value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
	</div>
</g:form></div>
</body>
</html>
