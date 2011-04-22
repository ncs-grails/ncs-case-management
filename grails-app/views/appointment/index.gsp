<%@ page import="edu.umn.ncs.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Appointment Scheduling</h1>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            
            <g:form>
            	Enter the Person's ID:
            	<g:textField name="person.id" value="${personInstance?.id}" />
            	<g:submitButton name="submit" value="Load" />
            	(if you don't know, please use <g:link controller="lookup">Lookup</g:link>)
            </g:form>
            
           	<fieldset class="maroonBorder">
            <g:if test="${personInstance}">
            	<legend>${ personInstance.fullName }'s Appointments</legend>
            	<g:include action="list" params="${ ['person.id': personInstance?.id ] }" />
            </g:if>
            <g:else>
          		<legend>Appointments</legend>
           		Please choose a subject.
            </g:else>
           	</fieldset>
        </div>
    </body>
</html>
