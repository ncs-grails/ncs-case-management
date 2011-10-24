

<%@ page import="edu.umn.ncs.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <style>
        	td.name {
        		color:maroon;
        		background-color: gold;
        	}
        	td.maroon {
        		color:gold;
        		background-color: maroon;
        	}
        </style>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="index" params="${ ['person.id':appointmentInstance?.person?.id] }">
            	<g:message code="default.list.label" args="[entityName]" />
            </g:link></span>
            <span class="menuButton"><g:link class="list" action="calendar" id="${formatDate(date:appointmentInstance.startTime, format:'yyyy-MM-dd')}">
            	Calendar
            </g:link></span>

        </div>
        <div class="body">
            <h1>Editing Appointment for ${appointmentInstance?.person?.fullName}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${appointmentInstance}">
            <div class="errors">
                <g:renderErrors bean="${appointmentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${appointmentInstance?.id}" />
                <g:hiddenField name="version" value="${appointmentInstance?.version}" />
            	<g:hiddenField name="dwellingUnit.id" value="${appointmentInstance?.dwellingUnit?.id}" />
            	<g:hiddenField name="person.id" value="${appointmentInstance?.person?.id}" />
            	<g:hiddenField name="letter.id" value="${appointmentInstance?.letter?.id}" />
                <div class="dialog">
	                <div class="buttons">
	                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
	                   	<span class="button"><g:actionSubmit class="edit" action="show" value="Cancel" /></span>
	                    <g:if test="${ ! appointmentInstance.letter && ! appointmentInstance.result }">
	                    	<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	                    </g:if>
	                </div>
                    <table>
                        <tbody>
                        
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
                                  <label for="location"><g:message code="appointment.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'location', 'errors')}">
                                    <g:select name="location.id" from="${edu.umn.ncs.AppointmentLocation.list()}" optionKey="id" value="${appointmentInstance?.location?.id}"  />
                                </td>
                            </tr>

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
	                                    for
	                                    <g:select name="parentAppointment.id" from="${appointmentInstanceList}" optionKey="id" value="${appointmentInstance?.parentAppointment?.id}" default="none" noSelection="['null': 'none']" />
	                                </td>
	                            </tr>
                            </g:if>
                        
                        	<g:if test="${! appointmentInstance?.letter}">
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="generateLetter"><g:message code="appointment.generateLetter.label" default="Generate Letter" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'generateLetter', 'errors')}">
                                    <g:checkBox name="generateLetter" value="${appointmentInstance?.generateLetter}" />
                                </td>
                            </tr>
                            </g:if>
                            <g:else>
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="generateLetter">Letter Already Generated</label>
                                </td>
                                <td valign="top" class="value">
                                    ${appointmentInstance.letter}
                                </td>
                            </tr>
                            </g:else>
                                                
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
                                  <label for="scheduledBy"><g:message code="appointment.scheduledBy.label" default="Scheduled By" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'scheduledBy', 'errors')}">
                                    <g:textField name="scheduledBy" value="${appointmentInstance?.scheduledBy}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="sequenceNumber"><g:message code="appointment.sequenceNumber.label" default="Sequence Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'sequenceNumber', 'errors')}">
                                    <g:textField name="sequenceNumber" size="5" value="${fieldValue(bean: appointmentInstance, field: 'sequenceNumber')}" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
            </g:form>

            <g:form action="addDetail" method="post" >
                <g:hiddenField name="appointment.id" value="${appointmentInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name maroon">
                                  <label for="details"><g:message code="appointment.details.label" default="Details" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'details', 'errors')}">
									<ul>
									<g:each in="${appointmentInstance?.details}" var="i">
									    <li><g:link controller="details" action="edit" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
									</g:each>
									</ul>
									
									<g:if test="${appointmentDetailTypeInstanceList}" >
										<g:link controller="details" action="create" params="['appointment.id': appointmentInstance?.id]">Add Details</g:link>
						            </g:if>
						            <g:else>
						            	Not Available for this Appointment Type
						            </g:else>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:form>
                       
            <g:if test="${incentiveTypeInstanceList}">
            <g:form action="addIncentive"  method="post" >
                <g:hiddenField name="appointment.id" value="${appointmentInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name maroon">
                                  <label for="incentives"><g:message code="appointment.incentives.label" default="Incentives" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'incentives', 'errors')}">
									<ul>
									<g:each in="${appointmentInstance?.incentives?}" var="i">
									    <%--<li><g:link controller="incentive" action="edit" id="${i.id}">${i?.encodeAsHTML()}</g:link></li> --%>
									    <li><g:link controller="incentive" action="editAppointmentIncentive" id="${i?.incentive.id}"><g:formatNumber number="${i?.incentive?.amount}" type="currency" currencyCode="USD" /> ${i?.incentive?.type?.name}</g:link></li>
									</g:each>
									</ul>
									<g:link controller="incentive" action="createAppointmentIncentive" params="['appointment.id': appointmentInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'incentive.label', default: 'Incentive')])}</g:link>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:form>
            </g:if>
            
        </div>
    </body>
</html>
