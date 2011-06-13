
<%@ page import="edu.umn.ncs.EventType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'eventType.label', default: 'EventType')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'eventType.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'eventType.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="useEventCode" title="${message(code: 'eventType.useEventCode.label', default: 'Use Event Code')}" />
                        
                            <g:sortableColumn property="useEventPickOne" title="${message(code: 'eventType.useEventPickOne.label', default: 'Use Event Pick One')}" />
                        
                            <g:sortableColumn property="useEventDate" title="${message(code: 'eventType.useEventDate.label', default: 'Use Event Date')}" />
                        
                            <g:sortableColumn property="useEventDescription" title="${message(code: 'eventType.useEventDescription.label', default: 'Use Event Description')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${eventTypeInstanceList}" status="i" var="eventTypeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${eventTypeInstance.id}">${fieldValue(bean: eventTypeInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: eventTypeInstance, field: "name")}</td>
                        
                            <td><g:formatBoolean boolean="${eventTypeInstance.useEventCode}" /></td>
                        
                            <td><g:formatBoolean boolean="${eventTypeInstance.useEventPickOne}" /></td>
                        
                            <td><g:formatBoolean boolean="${eventTypeInstance.useEventDate}" /></td>
                        
                            <td><g:formatBoolean boolean="${eventTypeInstance.useEventDescription}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${eventTypeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
