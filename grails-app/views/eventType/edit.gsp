


<%@ page import="edu.umn.ncs.EventType" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'eventType.label', default: 'EventType')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
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
<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if> <g:hasErrors bean="${eventTypeInstance}">
	<div class="errors"><g:renderErrors bean="${eventTypeInstance}"
		as="list" /></div>
</g:hasErrors> <g:form method="post"
	>
	<g:hiddenField name="id" value="${eventTypeInstance?.id}" />
	<g:hiddenField name="version" value="${eventTypeInstance?.version}" />
	<div class="dialog">
	
                            <div class="prop">
                                <span class="name">
                                  <label for="name"><g:message code="eventType.name.label" default="Name" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${eventTypeInstance?.name}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="useEventCode"><g:message code="eventType.useEventCode.label" default="Use Event Code" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventCode', 'errors')}">
                                    <g:checkBox name="useEventCode" value="${eventTypeInstance?.useEventCode}" />
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'nameEventCode', 'errors')}">
                                    <g:textField name="nameEventCode" value="${eventTypeInstance?.nameEventCode}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="useEventPickOne"><g:message code="eventType.useEventPickOne.label" default="Use Event Pick One" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventPickOne', 'errors')}">
                                    <g:checkBox name="useEventPickOne" value="${eventTypeInstance?.useEventPickOne}" />
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'nameEventPickOne', 'errors')}">
                                    <g:textField name="nameEventPickOne" value="${eventTypeInstance?.nameEventPickOne}" />
                                </span>
                            </div>

                        
                            <div class="prop">
                                <span class="name">
                                  <label for="useEventDate"><g:message code="eventType.useEventDate.label" default="Use Event Date" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventDate', 'errors')}">
                                    <g:checkBox name="useEventDate" value="${eventTypeInstance?.useEventDate}" />
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'nameEventDate', 'errors')}">
                                    <g:textField name="nameEventDate" value="${eventTypeInstance?.nameEventDate}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="useEventDescription"><g:message code="eventType.useEventDescription.label" default="Use Event Description" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventDescription', 'errors')}">
                                    <g:checkBox name="useEventDescription" value="${eventTypeInstance?.useEventDescription}" />
                                </span>
                                <span class="value ${hasErrors(bean: eventTypeInstance, field: 'nameEventDescription', 'errors')}">
                                    <g:textField name="nameEventDescription" value="${eventTypeInstance?.nameEventDescription}" />
                                </span>
                            </div>
                        
	</div>
	<div class="buttons"><span class="button"><g:actionSubmit
		class="save" action="update"
		value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
	<span class="button"><g:actionSubmit class="delete"
		action="delete"
		value="${message(code: 'default.button.delete.label', default: 'Delete')}"
		onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	</div>
</g:form></div>
</body>
</html>
