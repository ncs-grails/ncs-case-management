
<%@ page import="edu.umn.ncs.BatchCreationDocument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationDocument.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationDocumentInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationDocument.documentLocation.label" default="Document Location" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationDocumentInstance, field: "documentLocation")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationDocument.mergeSourceQuery.label" default="Merge Source Query" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationDocumentInstance, field: "mergeSourceQuery")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationDocument.mergeSourceFile.label" default="Merge Source File" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationDocumentInstance, field: "mergeSourceFile")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationDocument.batchCreationConfig.label" default="Batch Creation Config" /></td>
                            
                            <td valign="top" class="value"><g:link controller="batchCreationConfig" action="show" id="${batchCreationDocumentInstance?.batchCreationConfig?.id}">${batchCreationDocumentInstance?.batchCreationConfig?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${batchCreationDocumentInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
