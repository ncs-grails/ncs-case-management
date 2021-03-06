<%@ page import="edu.umn.ncs.EventReport" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'eventReport.label', default: 'Event of Interest Reports')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1>Event of Interest Reports</h1>
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
            	<legend>
            	<g:link controller="person" action="show" id="${personInstance.id}" title="Open in Lookup">
            		<img style="vertical-align: middle;" src="${resource(dir:'images', file:'magnifying-glass-48x48.png')}" width="24" height="24" alt="View in Lookup" />
            	</g:link>
            	${ personInstance.fullName }'<g:if test="${!personInstance.fullName.getAt(personInstance.fullName.size()-1)=='s'}">s</g:if> Event of Interest Reports
            	</legend>
            	<g:include action="list" params="${ ['person.id': personInstance?.id ] }" />
            </g:if>
            <g:else>
          		<legend>Event of Interest Reports</legend>
           		Please choose a subject.
            </g:else>
           	</fieldset>
        </div>
    </body>
</html>
