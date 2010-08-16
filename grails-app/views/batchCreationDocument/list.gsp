
<%@ page import="edu.umn.ncs.BatchCreationDocument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'batchCreationDocument.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="documentLocation" title="${message(code: 'batchCreationDocument.documentLocation.label', default: 'Document Location')}" />
                        
                            <g:sortableColumn property="mergeSourceQuery" title="${message(code: 'batchCreationDocument.mergeSourceQuery.label', default: 'Merge Source Query')}" />
                        
                            <g:sortableColumn property="mergeSourceFile" title="${message(code: 'batchCreationDocument.mergeSourceFile.label', default: 'Merge Source File')}" />
                        
                            <th><g:message code="batchCreationDocument.batchCreationConfig.label" default="Batch Creation Config" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${batchCreationDocumentInstanceList}" status="i" var="batchCreationDocumentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${batchCreationDocumentInstance.id}">${fieldValue(bean: batchCreationDocumentInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: batchCreationDocumentInstance, field: "documentLocation")}</td>
                        
                            <td>${fieldValue(bean: batchCreationDocumentInstance, field: "mergeSourceQuery")}</td>
                        
                            <td>${fieldValue(bean: batchCreationDocumentInstance, field: "mergeSourceFile")}</td>
                        
                            <td>${fieldValue(bean: batchCreationDocumentInstance, field: "batchCreationConfig")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${batchCreationDocumentInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
