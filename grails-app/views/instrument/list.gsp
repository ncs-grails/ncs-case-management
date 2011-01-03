<%@ page import="edu.umn.ncs.Instrument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'instrument.label', default: 'Instrument')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
			<span class="logoutButton"><g:link class="create" controller="logout" action="index">Logout</g:link></span>
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'instrument.id.label', default: 'ID')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'instrument.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="nickName" title="${message(code: 'instrument.nickName.label', default: 'Nick Name')}" />
                        
                            <g:sortableColumn property="daysTillReminder" title="${message(code: 'instrument.daysTillReminder.label', default: 'Days Till Reminder')}" />
                        
                            <th><g:message code="instrument.study.label" default="Study" /></th>
                        
                            <g:sortableColumn property="reportThisEvent" title="${message(code: 'instrument.reportThisEvent.label', default: 'Report This Event')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${instrumentInstanceList}" status="i" var="instrumentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${instrumentInstance.id}">${fieldValue(bean: instrumentInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: instrumentInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: instrumentInstance, field: "nickName")}</td>
                        
                            <td>${fieldValue(bean: instrumentInstance, field: "daysTillReminder")}</td>
                        
                            <td>${fieldValue(bean: instrumentInstance, field: "study")}</td>
                        
                            <td><g:formatBoolean boolean="${instrumentInstance.reportThisEvent}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${instrumentInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
