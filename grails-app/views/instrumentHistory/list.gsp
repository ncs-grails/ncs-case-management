
<%@ page import="edu.umn.ncs.InstrumentHistory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'instrumentHistory.label', default: 'InstrumentHistory')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'instrumentHistory.id.label', default: 'Id')}" />
                        
                            <th><g:message code="instrumentHistory.instrument.label" default="Instrument" /></th>
                        
                            <g:sortableColumn property="itemVersion" title="${message(code: 'instrumentHistory.itemVersion.label', default: 'Item Version')}" />
                        
                            <th><g:message code="instrumentHistory.isInitial.label" default="Is Initial" /></th>
                        
                            <th><g:message code="instrumentHistory.revisionType.label" default="Revision Type" /></th>
                        
                            <th><g:message code="instrumentHistory.status.label" default="Status" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${instrumentHistoryInstanceList}" status="i" var="instrumentHistoryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${instrumentHistoryInstance.id}">${fieldValue(bean: instrumentHistoryInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: instrumentHistoryInstance, field: "instrument")}</td>
                        
                            <td>${fieldValue(bean: instrumentHistoryInstance, field: "itemVersion")}</td>
                        
                            <td>${fieldValue(bean: instrumentHistoryInstance, field: "isInitial")}</td>
                        
                            <td>${fieldValue(bean: instrumentHistoryInstance, field: "revisionType")}</td>
                        
                            <td>${fieldValue(bean: instrumentHistoryInstance, field: "status")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${instrumentHistoryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
