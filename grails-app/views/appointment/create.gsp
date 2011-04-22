<%@ page import="edu.umn.ncs.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="index" params="${ ['person.id':appointmentInstance?.person?.id] }">
            	Back to <g:message code="default.list.label" args="[entityName]" />
            </g:link></span>
        </div>
        <div class="body">
        	<g:if test="${appointmentInstance?.person}" >
            	<h1>New Appointment for ${appointmentInstance?.person?.fullName}</h1>
            </g:if>
            <g:else>
            	<h1>New Appointment for ${appointmentInstance?.dwellingUnit}</h1>
            </g:else>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${appointmentInstance}">
            <div class="errors">
                <g:renderErrors bean="${appointmentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
            	<g:hiddenField name="dwellingUnit.id" value="${appointmentInstance?.dwellingUnit?.id}" />
            	<g:hiddenField name="person.id" value="${appointmentInstance?.person?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="startTime"><g:message code="appointment.startTime.label" default="Start Time" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'startTime', 'errors')}">
                                    <g:datePicker name="startTime" precision="minute" value="${appointmentInstance?.startTime}"  />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="endTime"><g:message code="appointment.endTime.label" default="End Time" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'endTime', 'errors')}">
                                    <g:datePicker name="endTime" precision="minute" default="none" noSelection="['null': '--']" value="${appointmentInstance?.endTime}"  />
                                </td>
                            </tr>
                        
                        	<g:if test="${appointmentInstanceList}">
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="followUpAppointment"><g:message code="appointment.followUpAppointment.label" default="Follow Up Appointment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'followUpAppointment', 'errors')}">
                                    <g:checkBox name="followUpAppointment" value="${appointmentInstance?.followUpAppointment}" />
                                    <label for="parentAppointment">to</label>
                                    <g:select name="parentAppointment.id" from="${appointmentInstanceList}" optionKey="id" value="${appointmentInstance?.parentAppointment?.id}" default="none" noSelection="['null': 'none']" />
                                </td>
                            </tr>
                            </g:if>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="location"><g:message code="appointment.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'location', 'errors')}">
                                    <g:select name="location.id" from="${edu.umn.ncs.AppointmentLocation.list()}" optionKey="id" value="${appointmentInstance?.location?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="scheduledBy"><g:message code="appointment.scheduledBy.label" default="Scheduled By" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'scheduledBy', 'errors')}">
                                    <g:textField name="scheduledBy" value="${appointmentInstance?.scheduledBy}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="study"><g:message code="appointment.study.label" default="Study" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'study', 'errors')}">
                                    <g:select name="study.id" from="${edu.umn.ncs.Study.list()}" optionKey="id" value="${appointmentInstance?.study?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="appointment.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'type', 'errors')}">
                                    <g:select name="type.id" from="${edu.umn.ncs.AppointmentType.list()}" optionKey="id" value="${appointmentInstance?.type?.id}"  />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="billable"><g:message code="appointment.billable.label" default="Billable" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'billable', 'errors')}">
                                    <g:checkBox name="billable" value="${appointmentInstance?.billable}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="generateLetter"><g:message code="appointment.generateLetter.label" default="Generate Letter" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'generateLetter', 'errors')}">
                                    <g:checkBox name="generateLetter" value="${appointmentInstance?.generateLetter}" />
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
