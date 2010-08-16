

<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${batchCreationConfigInstance}">
            <div class="errors">
                <g:renderErrors bean="${batchCreationConfigInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="batchCreationConfig.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="32" value="${batchCreationConfigInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="selectionQuery"><g:message code="batchCreationConfig.selectionQuery.label" default="Selection Query" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'selectionQuery', 'errors')}">
                                    <g:textArea name="selectionQuery" cols="40" rows="5" value="${batchCreationConfigInstance?.selectionQuery}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="postGenerationQuery"><g:message code="batchCreationConfig.postGenerationQuery.label" default="Post Generation Query" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'postGenerationQuery', 'errors')}">
                                    <g:textArea name="postGenerationQuery" cols="40" rows="5" value="${batchCreationConfigInstance?.postGenerationQuery}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="defaultReason"><g:message code="batchCreationConfig.defaultReason.label" default="Default Reason" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'defaultReason', 'errors')}">
                                    <g:textArea name="defaultReason" cols="40" rows="5" value="${batchCreationConfigInstance?.defaultReason}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="defaultInstructions"><g:message code="batchCreationConfig.defaultInstructions.label" default="Default Instructions" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'defaultInstructions', 'errors')}">
                                    <g:textArea name="defaultInstructions" cols="40" rows="5" value="${batchCreationConfigInstance?.defaultInstructions}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="defaultComments"><g:message code="batchCreationConfig.defaultComments.label" default="Default Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'defaultComments', 'errors')}">
                                    <g:textArea name="defaultComments" cols="40" rows="5" value="${batchCreationConfigInstance?.defaultComments}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="parentInstrument"><g:message code="batchCreationConfig.parentInstrument.label" default="Parent Instrument" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentInstrument', 'errors')}">
                                    <g:select name="parentInstrument.id" from="${edu.umn.ncs.Instrument.list()}" optionKey="id" value="${batchCreationConfigInstance?.parentInstrument?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="parentDirection"><g:message code="batchCreationConfig.parentDirection.label" default="Parent Direction" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentDirection', 'errors')}">
                                    <g:select name="parentDirection.id" from="${edu.umn.ncs.BatchDirection.list()}" optionKey="id" value="${batchCreationConfigInstance?.parentDirection?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="parentFormat"><g:message code="batchCreationConfig.parentFormat.label" default="Parent Format" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentFormat', 'errors')}">
                                    <g:select name="parentFormat.id" from="${edu.umn.ncs.InstrumentFormat.list()}" optionKey="id" value="${batchCreationConfigInstance?.parentFormat?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="parentInitial"><g:message code="batchCreationConfig.parentInitial.label" default="Parent Initial" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentInitial', 'errors')}">
                                    <g:select name="parentInitial.id" from="${edu.umn.ncs.IsInitial.list()}" optionKey="id" value="${batchCreationConfigInstance?.parentInitial?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="active"><g:message code="batchCreationConfig.active.label" default="Active" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'active', 'errors')}">
                                    <g:checkBox name="active" value="${batchCreationConfigInstance?.active}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="allowMultiplePersonPerBatch"><g:message code="batchCreationConfig.allowMultiplePersonPerBatch.label" default="Allow Multiple Person Per Batch" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'allowMultiplePersonPerBatch', 'errors')}">
                                    <g:checkBox name="allowMultiplePersonPerBatch" value="${batchCreationConfigInstance?.allowMultiplePersonPerBatch}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="autoSetMailDate"><g:message code="batchCreationConfig.autoSetMailDate.label" default="Auto Set Mail Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'autoSetMailDate', 'errors')}">
                                    <g:checkBox name="autoSetMailDate" value="${batchCreationConfigInstance?.autoSetMailDate}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="automaticSelection"><g:message code="batchCreationConfig.automaticSelection.label" default="Automatic Selection" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'automaticSelection', 'errors')}">
                                    <g:checkBox name="automaticSelection" value="${batchCreationConfigInstance?.automaticSelection}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="batchReportsToPrint"><g:message code="batchCreationConfig.batchReportsToPrint.label" default="Batch Reports To Print" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'batchReportsToPrint', 'errors')}">
                                    <g:textField name="batchReportsToPrint" value="${fieldValue(bean: batchCreationConfigInstance, field: 'batchReportsToPrint')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="direction"><g:message code="batchCreationConfig.direction.label" default="Direction" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'direction', 'errors')}">
                                    <g:select name="direction.id" from="${edu.umn.ncs.BatchDirection.list()}" optionKey="id" value="${batchCreationConfigInstance?.direction?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="format"><g:message code="batchCreationConfig.format.label" default="Format" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'format', 'errors')}">
                                    <g:select name="format.id" from="${edu.umn.ncs.InstrumentFormat.list()}" optionKey="id" value="${batchCreationConfigInstance?.format?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="generateTrackingDocument"><g:message code="batchCreationConfig.generateTrackingDocument.label" default="Generate Tracking Document" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'generateTrackingDocument', 'errors')}">
                                    <g:checkBox name="generateTrackingDocument" value="${batchCreationConfigInstance?.generateTrackingDocument}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="instrument"><g:message code="batchCreationConfig.instrument.label" default="Instrument" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'instrument', 'errors')}">
                                    <g:select name="instrument.id" from="${edu.umn.ncs.Instrument.list()}" optionKey="id" value="${batchCreationConfigInstance?.instrument?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="isInitial"><g:message code="batchCreationConfig.isInitial.label" default="Is Initial" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'isInitial', 'errors')}">
                                    <g:select name="isInitial.id" from="${edu.umn.ncs.IsInitial.list()}" optionKey="id" value="${batchCreationConfigInstance?.isInitial?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="isResend"><g:message code="batchCreationConfig.isResend.label" default="Is Resend" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'isResend', 'errors')}">
                                    <g:checkBox name="isResend" value="${batchCreationConfigInstance?.isResend}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="mailDateDaysShift"><g:message code="batchCreationConfig.mailDateDaysShift.label" default="Mail Date Days Shift" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'mailDateDaysShift', 'errors')}">
                                    <g:textField name="mailDateDaysShift" value="${fieldValue(bean: batchCreationConfigInstance, field: 'mailDateDaysShift')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="manualSelection"><g:message code="batchCreationConfig.manualSelection.label" default="Manual Selection" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'manualSelection', 'errors')}">
                                    <g:checkBox name="manualSelection" value="${batchCreationConfigInstance?.manualSelection}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="maxPieces"><g:message code="batchCreationConfig.maxPieces.label" default="Max Pieces" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'maxPieces', 'errors')}">
                                    <g:textField name="maxPieces" value="${fieldValue(bean: batchCreationConfigInstance, field: 'maxPieces')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="oneBatchEventParentItem"><g:message code="batchCreationConfig.oneBatchEventParentItem.label" default="One Batch Event Parent Item" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'oneBatchEventParentItem', 'errors')}">
                                    <g:checkBox name="oneBatchEventParentItem" value="${batchCreationConfigInstance?.oneBatchEventParentItem}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="oneBatchEventPerYear"><g:message code="batchCreationConfig.oneBatchEventPerYear.label" default="One Batch Event Per Year" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'oneBatchEventPerYear', 'errors')}">
                                    <g:checkBox name="oneBatchEventPerYear" value="${batchCreationConfigInstance?.oneBatchEventPerYear}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="oneBatchEventPerson"><g:message code="batchCreationConfig.oneBatchEventPerson.label" default="One Batch Event Person" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'oneBatchEventPerson', 'errors')}">
                                    <g:checkBox name="oneBatchEventPerson" value="${batchCreationConfigInstance?.oneBatchEventPerson}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="optionalSelection"><g:message code="batchCreationConfig.optionalSelection.label" default="Optional Selection" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'optionalSelection', 'errors')}">
                                    <g:checkBox name="optionalSelection" value="${batchCreationConfigInstance?.optionalSelection}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="putInOnlineCallSystem"><g:message code="batchCreationConfig.putInOnlineCallSystem.label" default="Put In Online Call System" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'putInOnlineCallSystem', 'errors')}">
                                    <g:checkBox name="putInOnlineCallSystem" value="${batchCreationConfigInstance?.putInOnlineCallSystem}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="requireCurrentStudyYear"><g:message code="batchCreationConfig.requireCurrentStudyYear.label" default="Require Current Study Year" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'requireCurrentStudyYear', 'errors')}">
                                    <g:checkBox name="requireCurrentStudyYear" value="${batchCreationConfigInstance?.requireCurrentStudyYear}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="useParentItem"><g:message code="batchCreationConfig.useParentItem.label" default="Use Parent Item" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'useParentItem', 'errors')}">
                                    <g:checkBox name="useParentItem" value="${batchCreationConfigInstance?.useParentItem}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="useStudyYear"><g:message code="batchCreationConfig.useStudyYear.label" default="Use Study Year" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'useStudyYear', 'errors')}">
                                    <g:checkBox name="useStudyYear" value="${batchCreationConfigInstance?.useStudyYear}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
