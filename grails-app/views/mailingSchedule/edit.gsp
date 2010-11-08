

<%@ page import="edu.umn.ncs.MailingSchedule" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'mailingSchedule.label', default: 'Mailing Schedule')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list" params="${['instrument.id':mailingScheduleInstance?.instrument?.id]}"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${mailingScheduleInstance}">
            <div class="errors">
                <g:renderErrors bean="${mailingScheduleInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${mailingScheduleInstance?.id}" />
                <g:hiddenField name="version" value="${mailingScheduleInstance?.version}" />
                <div class="dialog">
                        <g:hiddenField name="instrument.id" value="${mailingScheduleInstance?.instrument?.id}" />

                            <div class="prop">
                                <span class="name">
                                  <label for="instrument"><g:message code="mailingSchedule.instrument.label" default="Instrument" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: mailingScheduleInstance, field: 'instrument', 'errors')}">
                                    ${mailingScheduleInstance?.instrument}
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="checkpointDate"><g:message code="mailingSchedule.checkpointDate.label" default="Checkpoint Date" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: mailingScheduleInstance, field: 'checkpointDate', 'errors')}">
                                    <g:datePicker name="checkpointDate" precision="day" value="${mailingScheduleInstance?.checkpointDate}"  />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="quota"><g:message code="mailingSchedule.quota.label" default="Cumulative Quota" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: mailingScheduleInstance, field: 'quota', 'errors')}">
                                    <g:textField name="quota" value="${fieldValue(bean: mailingScheduleInstance, field: 'quota')}" />
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
