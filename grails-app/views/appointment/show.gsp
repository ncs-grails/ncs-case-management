<%@ page import="edu.umn.ncs.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        <style>
        	td.name {
        		color:maroon;
        		background-color: gold;
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
            <h1>Appointment Information</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
			<g:if test="${ (! appointmentInstance?.result) && (! appointmentInstance?.letter) }">
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${appointmentInstance?.id}" />
                   	<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                </g:form>
            </div>
			</g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                    	<g:if test="${appointmentInstance?.person}" >
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="appointment.person.label" default="Person" /></td>
	                            <td valign="top" class="value"><g:link controller="person" action="show" id="${appointmentInstance?.person?.id}">${appointmentInstance?.person?.encodeAsHTML()}</g:link></td>
	                        </tr>
                        </g:if>
                        <g:else>
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="appointment.dwellingUnit.label" default="Dwelling Unit" /></td>
	                            <td valign="top" class="value"><g:link controller="dwellingUnit" action="show" id="${appointmentInstance?.dwellingUnit?.id}">${appointmentInstance?.dwellingUnit?.encodeAsHTML()}</g:link></td>
	                        </tr>
                        </g:else>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.study.label" default="Study" /></td>
                            <td valign="top" class="value">${appointmentInstance?.study?.encodeAsHTML()}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.type.label" default="Type" /></td>
                            <td valign="top" class="value">${appointmentInstance?.type?.encodeAsHTML()}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.location.label" default="Location" /></td>
                            <td valign="top" class="value">${appointmentInstance?.location?.encodeAsHTML()}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.startTime.label" default="Start Time" /></td>
                            <td valign="top" class="value"><g:formatDate date="${appointmentInstance?.startTime}" format="MM/dd/yyyy h:mm a" /></td>
                        </tr>
                        <g:if test="${appointmentInstance?.endTime}">
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="appointment.endTime.label" default="End Time" /></td>
	                            <td valign="top" class="value"><g:formatDate date="${appointmentInstance?.endTime}" format="MM/dd/yyyy h:mm a" /></td>
	                        </tr>
                        </g:if>

						<g:if test="${appointmentInstance.details}">
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.details.label" default="Details" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${appointmentInstance.details}" var="d">
                                    <li>${d?.encodeAsHTML()}</li>
                                </g:each>
                                </ul>
                            </td>
                        </tr>
                        </g:if>

						<%--<g:if test="${appointmentInstance.incentives}">
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.incentives.label" default="Incentives" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${appointmentInstance.incentives}" var="i">
                                    <li><g:link controller="incentive" action="edit" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                        </tr>
                        </g:if> --%>

						<g:if test="${appointmentInstance.incentives}">
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.incentives.label" default="Incentives" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${appointmentInstance.incentives}" var="i">
                                    <li><g:link controller="incentive" action="edit" id="${i?.incentive.id}">${i?.incentive}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                        </tr>
                        </g:if>

                    	<g:if test="${appointmentInstance?.followUpAppointment}">
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.followUpAppointment.label" default="Follow Up of Appointment" /></td>
                            <td valign="top" class="value">
               	            	<g:link controller="appointment" action="show" id="${appointmentInstance?.parentAppointment?.id}">${appointmentInstance?.parentAppointment?.encodeAsHTML()}</g:link>
                            </td>
                        </tr>
    		            </g:if>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.generateLetter.label" default="Letter" /></td>
                            <td valign="top" class="value">
                            	<g:if test="${appointmentInstance?.generateLetter}">
                            		<g:if test="${appointmentInstance?.letter}">
                            			Generated on 
                            				<g:link controller="trackedItem" action="show" id="${appointmentInstance?.letter?.id}">
                            					${appointmentInstance.letter.batch.dateCreated}
                            				</g:link>
                            		</g:if>
                            		<g:else>
                            			<em>Generation Pending...</em>
                            		</g:else>
                            	</g:if>
                            	<g:else>
                            		Do Not Generate
                            	</g:else>
                            </td>
                        </tr>
                    
                        <g:if test="${appointmentInstance?.result}">
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.result.label" default="Result" /></td>
                            <td valign="top" class="value"><g:link controller="appointmentResult" action="show" id="${appointmentInstance?.result?.id}">${appointmentInstance?.result?.encodeAsHTML()}</g:link></td>
                        </tr>
                        </g:if>
                    
                    	<g:if test="${appointmentInstance?.sequenceNumber}">
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.sequenceNumber.label" default="Sequence Number" /></td>
                            <td valign="top" class="value">${fieldValue(bean: appointmentInstance, field: "sequenceNumber")}</td>
                        </tr>
                        </g:if>
                                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.scheduledBy.label" default="Scheduled By" /></td>
                            <td valign="top" class="value">${fieldValue(bean: appointmentInstance, field: "scheduledBy")}</td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.billable.label" default="Billable" /></td>
                            <td valign="top" class="value"><g:formatBoolean boolean="${appointmentInstance?.billable}" true="Yes" false="No"/></td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.dateCreated.label" default="Initially Entered" /></td>
                            <td valign="top" class="value"><g:formatDate date="${appointmentInstance?.dateCreated}" /></td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.userCreated.label" default="User Created" /></td>
                            <td valign="top" class="value">${fieldValue(bean: appointmentInstance, field: "userCreated")}</td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="appointment.userUpdated.label" default="User Updated" /></td>
                            <td valign="top" class="value">${fieldValue(bean: appointmentInstance, field: "userUpdated")}</td>
                        </tr>

                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
