
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'batchCreationConfig.id.label', default: 'ID')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'batchCreationConfig.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="selectionQuery" title="${message(code: 'batchCreationConfig.selectionQuery.label', default: 'Selection Query')}" />
                        
                            <g:sortableColumn property="postGenerationQuery" title="${message(code: 'batchCreationConfig.postGenerationQuery.label', default: 'Post Generation Query')}" />
                        
                            <g:sortableColumn property="defaultReason" title="${message(code: 'batchCreationConfig.defaultReason.label', default: 'Default Reason')}" />
                        
                            <g:sortableColumn property="defaultInstructions" title="${message(code: 'batchCreationConfig.defaultInstructions.label', default: 'Default Instructions')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${batchCreationConfigInstanceList}" status="i" var="batchCreationConfigInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${batchCreationConfigInstance.id}">${fieldValue(bean: batchCreationConfigInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: batchCreationConfigInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: batchCreationConfigInstance, field: "selectionQuery")}</td>
                        
                            <td>${fieldValue(bean: batchCreationConfigInstance, field: "postGenerationQuery")}</td>
                        
                            <td>${fieldValue(bean: batchCreationConfigInstance, field: "defaultReason")}</td>
                        
                            <td>${fieldValue(bean: batchCreationConfigInstance, field: "defaultInstructions")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${batchCreationConfigInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
