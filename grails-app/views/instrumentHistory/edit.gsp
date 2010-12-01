

<%@ page import="edu.umn.ncs.InstrumentHistory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'instrumentHistory.label', default: 'InstrumentHistory')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${instrumentHistoryInstance}">
            <div class="errors">
                <g:renderErrors bean="${instrumentHistoryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${instrumentHistoryInstance?.id}" />
                <g:hiddenField name="version" value="${instrumentHistoryInstance?.version}" />
                <div class="dialog">
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="instrument"><g:message code="instrumentHistory.instrument.label" default="Instrument" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'instrument', 'errors')}">
                                    <g:select name="instrument.id" from="${edu.umn.ncs.Instrument.list()}" optionKey="id" value="${instrumentHistoryInstance?.instrument?.id}"  />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="itemVersion"><g:message code="instrumentHistory.itemVersion.label" default="Item Version" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'itemVersion', 'errors')}">
                                    <g:textField name="itemVersion" value="${fieldValue(bean: instrumentHistoryInstance, field: 'itemVersion')}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="isInitial"><g:message code="instrumentHistory.isInitial.label" default="Is Initial" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'isInitial', 'errors')}">
                                    <g:select name="isInitial.id" from="${edu.umn.ncs.IsInitial.list()}" optionKey="id" value="${instrumentHistoryInstance?.isInitial?.id}"  />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="revisionType"><g:message code="instrumentHistory.revisionType.label" default="Revision Type" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'revisionType', 'errors')}">
                                    <g:select name="revisionType.id" from="${edu.umn.ncs.InstrumentRevision.list()}" optionKey="id" value="${instrumentHistoryInstance?.revisionType?.id}"  />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="status"><g:message code="instrumentHistory.status.label" default="Status" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'status', 'errors')}">
                                    <g:select name="status.id" from="${edu.umn.ncs.InstrumentStatus.list()}" optionKey="id" value="${instrumentHistoryInstance?.status?.id}"  />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="comments"><g:message code="instrumentHistory.comments.label" default="Comments" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'comments', 'errors')}">
                                    <g:textField name="comments" value="${instrumentHistoryInstance?.comments}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="userCreated"><g:message code="instrumentHistory.userCreated.label" default="User Created" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'userCreated', 'errors')}">
                                    <g:textField name="userCreated" value="${instrumentHistoryInstance?.userCreated}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="appCreated"><g:message code="instrumentHistory.appCreated.label" default="App Created" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'appCreated', 'errors')}">
                                    <g:textField name="appCreated" value="${instrumentHistoryInstance?.appCreated}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="userUpdated"><g:message code="instrumentHistory.userUpdated.label" default="User Updated" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'userUpdated', 'errors')}">
                                    <g:textField name="userUpdated" value="${instrumentHistoryInstance?.userUpdated}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="approvals"><g:message code="instrumentHistory.approvals.label" default="Approvals" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: instrumentHistoryInstance, field: 'approvals', 'errors')}">
                                    
<ul>
<g:each in="${instrumentHistoryInstance?.approvals?}" var="a">
    <li><g:link controller="instrumentApproval" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="instrumentApproval" action="create" params="['instrumentHistory.id': instrumentHistoryInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'instrumentApproval.label', default: 'InstrumentApproval')])}</g:link>

                                </span>
                            </div>
                        
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
