

<%@ page import="edu.umn.ncs.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="index" params="${ ['person.id':appointmentInstance?.person?.id] }">
            	Back to <g:message code="default.list.label" args="[entityName]" />
            </g:link></span>

        </div>
        <div class="body">
            <h1>Enter Result for Appointment for ${appointmentInstance?.person?.fullName}</h1>
            <h2><g:formatDate date="${appointmentInstance.startTime}" format="MM/dd/yyyy h:mm a" /></h2>
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
                <div class="dialog">
					<div class="buttons">
						<label for="study" style="color:black;">Result:</label>
						<g:select name="result.id" from="${resultList}" optionKey="id" value="${appointmentInstance?.result?.id}" default="none" noSelection="['null': '(none)']" />
						<span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Save')}" /></span>
					</div>
                </div>
            </g:form>
            
        </div>
    </body>
</html>
