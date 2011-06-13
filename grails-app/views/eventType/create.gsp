

<%@ page import="edu.umn.ncs.EventType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'eventType.label', default: 'EventType')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${eventTypeInstance}">
            <div class="errors">
                <g:renderErrors bean="${eventTypeInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="eventType.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventTypeInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${eventTypeInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="useEventCode"><g:message code="eventType.useEventCode.label" default="Use Event Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventCode', 'errors')}">
                                    <g:checkBox name="useEventCode" value="${eventTypeInstance?.useEventCode}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="useEventPickOne"><g:message code="eventType.useEventPickOne.label" default="Use Event Pick One" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventPickOne', 'errors')}">
                                    <g:checkBox name="useEventPickOne" value="${eventTypeInstance?.useEventPickOne}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="useEventDate"><g:message code="eventType.useEventDate.label" default="Use Event Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventDate', 'errors')}">
                                    <g:checkBox name="useEventDate" value="${eventTypeInstance?.useEventDate}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="useEventDescription"><g:message code="eventType.useEventDescription.label" default="Use Event Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventTypeInstance, field: 'useEventDescription', 'errors')}">
                                    <g:checkBox name="useEventDescription" value="${eventTypeInstance?.useEventDescription}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
