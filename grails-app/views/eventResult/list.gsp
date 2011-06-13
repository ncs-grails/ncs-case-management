
<%@ page import="edu.umn.ncs.EventResult" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'eventResult.label', default: 'EventResult')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'eventResult.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'eventResult.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="negatesEvent" title="${message(code: 'eventResult.negatesEvent.label', default: 'Negates Event')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${eventResultInstanceList}" status="i" var="eventResultInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${eventResultInstance.id}">${fieldValue(bean: eventResultInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: eventResultInstance, field: "name")}</td>
                        
                            <td><g:formatBoolean boolean="${eventResultInstance.negatesEvent}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${eventResultInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
