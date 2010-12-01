
<%@ page import="edu.umn.ncs.InstrumentHistory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'instrumentHistory.label', default: 'InstrumentHistory')}" />
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
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.id.label" default="Id" /></span>
                            
                            <span class="value">${fieldValue(bean: instrumentHistoryInstance, field: "id")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.instrument.label" default="Instrument" /></span>
                            
                            <span class="value"><g:link controller="instrument" action="show" id="${instrumentHistoryInstance?.instrument?.id}">${instrumentHistoryInstance?.instrument?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.itemVersion.label" default="Item Version" /></span>
                            
                            <span class="value">${fieldValue(bean: instrumentHistoryInstance, field: "itemVersion")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.isInitial.label" default="Is Initial" /></span>
                            
                            <span class="value"><g:link controller="isInitial" action="show" id="${instrumentHistoryInstance?.isInitial?.id}">${instrumentHistoryInstance?.isInitial?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.revisionType.label" default="Revision Type" /></span>
                            
                            <span class="value"><g:link controller="instrumentRevision" action="show" id="${instrumentHistoryInstance?.revisionType?.id}">${instrumentHistoryInstance?.revisionType?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.status.label" default="Status" /></span>
                            
                            <span class="value"><g:link controller="instrumentStatus" action="show" id="${instrumentHistoryInstance?.status?.id}">${instrumentHistoryInstance?.status?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.comments.label" default="Comments" /></span>
                            
                            <span class="value">${fieldValue(bean: instrumentHistoryInstance, field: "comments")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.userCreated.label" default="User Created" /></span>
                            
                            <span class="value">${fieldValue(bean: instrumentHistoryInstance, field: "userCreated")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.appCreated.label" default="App Created" /></span>
                            
                            <span class="value">${fieldValue(bean: instrumentHistoryInstance, field: "appCreated")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.userUpdated.label" default="User Updated" /></span>
                            
                            <span class="value">${fieldValue(bean: instrumentHistoryInstance, field: "userUpdated")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.dateCreated.label" default="Date Created" /></span>
                            
                            <span class="value"><g:formatDate date="${instrumentHistoryInstance?.dateCreated}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.lastUpdated.label" default="Last Updated" /></span>
                            
                            <span class="value"><g:formatDate date="${instrumentHistoryInstance?.lastUpdated}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="instrumentHistory.approvals.label" default="Approvals" /></span>
                            
                            <span style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${instrumentHistoryInstance.approvals}" var="a">
                                    <li><g:link controller="instrumentApproval" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </span>
                            
                        </div>
                    
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${instrumentHistoryInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
