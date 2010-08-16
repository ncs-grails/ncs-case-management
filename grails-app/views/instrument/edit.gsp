<%@ page import="edu.umn.ncs.Instrument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'instrument.label', default: 'Instrument')}" />
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
            <g:hasErrors bean="${instrumentInstance}">
            <div class="errors">
                <g:renderErrors bean="${instrumentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${instrumentInstance?.id}" />
                <g:hiddenField name="version" value="${instrumentInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="instrument.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="16" value="${instrumentInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="nickName"><g:message code="instrument.nickName.label" default="Nick Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'nickName', 'errors')}">
                                    <g:textField name="nickName" value="${instrumentInstance?.nickName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="daysTillReminder"><g:message code="instrument.daysTillReminder.label" default="Days Till Reminder" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'daysTillReminder', 'errors')}">
                                    <g:textField name="daysTillReminder" value="${fieldValue(bean: instrumentInstance, field: 'daysTillReminder')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="study"><g:message code="instrument.study.label" default="Study" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'study', 'errors')}">
                                    <g:select name="study.id" from="${edu.umn.ncs.Study.list()}" optionKey="id" value="${instrumentInstance?.study?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="reportThisEvent"><g:message code="instrument.reportThisEvent.label" default="Report This Event" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'reportThisEvent', 'errors')}">
                                    <g:checkBox name="reportThisEvent" value="${instrumentInstance?.reportThisEvent}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="requiresPrimaryContact"><g:message code="instrument.requiresPrimaryContact.label" default="Requires Primary Contact" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'requiresPrimaryContact', 'errors')}">
                                    <g:checkBox name="requiresPrimaryContact" value="${instrumentInstance?.requiresPrimaryContact}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="masterInstrument"><g:message code="instrument.masterInstrument.label" default="Master Instrument" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'masterInstrument', 'errors')}">
                                    <g:select name="masterInstrument.id" from="${edu.umn.ncs.Instrument.list()}" optionKey="id" value="${instrumentInstance?.masterInstrument?.id}" noSelection="['null':'None']"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="previousInstrument"><g:message code="instrument.previousInstrument.label" default="Previous Instrument" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'previousInstrument', 'errors')}">
                                    <g:select name="previousInstrument.id" from="${edu.umn.ncs.Instrument.list()}" optionKey="id" value="${instrumentInstance?.previousInstrument?.id}" noSelection="['null':'None']"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="reminderInstrument"><g:message code="instrument.reminderInstrument.label" default="Reminder Instrument" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'reminderInstrument', 'errors')}">
                                    <g:select name="reminderInstrument.id" from="${edu.umn.ncs.Instrument.list()}" optionKey="id" value="${instrumentInstance?.reminderInstrument?.id}" noSelection="['null':'None']"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
