
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig')}" />
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
                            <td valign="top" class="name"><g:message code="batchCreationConfig.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.selectionQuery.label" default="Selection Query" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "selectionQuery")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.postGenerationQuery.label" default="Post Generation Query" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "postGenerationQuery")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.defaultReason.label" default="Default Reason" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "defaultReason")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.defaultInstructions.label" default="Default Instructions" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "defaultInstructions")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.defaultComments.label" default="Default Comments" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "defaultComments")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.parentInstrument.label" default="Parent Instrument" /></td>
                            
                            <td valign="top" class="value"><g:link controller="instrument" action="show" id="${batchCreationConfigInstance?.parentInstrument?.id}">${batchCreationConfigInstance?.parentInstrument?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.parentDirection.label" default="Parent Direction" /></td>
                            
                            <td valign="top" class="value"><g:link controller="batchDirection" action="show" id="${batchCreationConfigInstance?.parentDirection?.id}">${batchCreationConfigInstance?.parentDirection?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.parentFormat.label" default="Parent Format" /></td>
                            
                            <td valign="top" class="value"><g:link controller="instrumentFormat" action="show" id="${batchCreationConfigInstance?.parentFormat?.id}">${batchCreationConfigInstance?.parentFormat?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.parentInitial.label" default="Parent Initial" /></td>
                            
                            <td valign="top" class="value"><g:link controller="isInitial" action="show" id="${batchCreationConfigInstance?.parentInitial?.id}">${batchCreationConfigInstance?.parentInitial?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.active.label" default="Active" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.active}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.allowMultiplePersonPerBatch.label" default="Allow Multiple Person Per Batch" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.allowMultiplePersonPerBatch}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.archivedQueries.label" default="Archived Queries" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${batchCreationConfigInstance.archivedQueries}" var="a">
                                    <li><g:link controller="batchCreationQueryArchive" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.autoSetMailDate.label" default="Auto Set Mail Date" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.autoSetMailDate}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.automaticSelection.label" default="Automatic Selection" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.automaticSelection}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.batchReportsToPrint.label" default="Batch Reports To Print" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "batchReportsToPrint")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.direction.label" default="Direction" /></td>
                            
                            <td valign="top" class="value"><g:link controller="batchDirection" action="show" id="${batchCreationConfigInstance?.direction?.id}">${batchCreationConfigInstance?.direction?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.documents.label" default="Documents" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${batchCreationConfigInstance.documents}" var="d">
                                    <li><g:link controller="batchCreationDocument" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.format.label" default="Format" /></td>
                            
                            <td valign="top" class="value"><g:link controller="instrumentFormat" action="show" id="${batchCreationConfigInstance?.format?.id}">${batchCreationConfigInstance?.format?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.generateTrackingDocument.label" default="Generate Tracking Document" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.generateTrackingDocument}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.instrument.label" default="Instrument" /></td>
                            
                            <td valign="top" class="value"><g:link controller="instrument" action="show" id="${batchCreationConfigInstance?.instrument?.id}">${batchCreationConfigInstance?.instrument?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.isInitial.label" default="Is Initial" /></td>
                            
                            <td valign="top" class="value"><g:link controller="isInitial" action="show" id="${batchCreationConfigInstance?.isInitial?.id}">${batchCreationConfigInstance?.isInitial?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.isResend.label" default="Is Resend" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.isResend}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.mailDateDaysShift.label" default="Mail Date Days Shift" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "mailDateDaysShift")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.manualSelection.label" default="Manual Selection" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.manualSelection}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.maxPieces.label" default="Max Pieces" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchCreationConfigInstance, field: "maxPieces")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.oneBatchEventParentItem.label" default="One Batch Event Parent Item" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.oneBatchEventParentItem}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.oneBatchEventPerYear.label" default="One Batch Event Per Year" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.oneBatchEventPerYear}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.oneBatchEventPerson.label" default="One Batch Event Person" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.oneBatchEventPerson}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.optionalSelection.label" default="Optional Selection" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.optionalSelection}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.putInOnlineCallSystem.label" default="Put In Online Call System" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.putInOnlineCallSystem}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.requireCurrentStudyYear.label" default="Require Current Study Year" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.requireCurrentStudyYear}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.subItems.label" default="Sub Items" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${batchCreationConfigInstance.subItems}" var="s">
                                    <li><g:link controller="batchCreationItem" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.useParentItem.label" default="Use Parent Item" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.useParentItem}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batchCreationConfig.useStudyYear.label" default="Use Study Year" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchCreationConfigInstance?.useStudyYear}" /></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${batchCreationConfigInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
