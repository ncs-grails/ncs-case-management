

<%@ page import="edu.umn.ncs.Result" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'result.label', default: 'Result')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span> <span class="menuButton"><g:link class="create" action="create">
	<g:message code="default.new.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.show.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if>
<div class="dialog">

                        <div class="prop">
                            <span class="name"><g:message code="result.id.label" default="Id" /></span>
                            
                            <span class="value">${fieldValue(bean: resultInstance, field: "id")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.name.label" default="Name" /></span>
                            
                            <span class="value">${fieldValue(bean: resultInstance, field: "name")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.abbreviation.label" default="Abbreviation" /></span>
                            
                            <span class="value">${fieldValue(bean: resultInstance, field: "abbreviation")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.format.label" default="Format" /></span>
                            
                            <span class="value"><g:link controller="instrumentFormat" action="show" id="${resultInstance?.format?.id}">${resultInstance?.format?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.ineligible.label" default="Ineligible" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.ineligible}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.resend.label" default="Resend" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.resend}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.disqualify.label" default="Disqualify" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.disqualify}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.refused.label" default="Refused" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.refused}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.valid.label" default="Valid" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.valid}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.photoCopy.label" default="Photo Copy" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.photoCopy}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.requiresPrimaryContact.label" default="Requires Primary Contact" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.requiresPrimaryContact}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.phoneCall.label" default="Phone Call" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.phoneCall}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="result.callSheet.label" default="Call Sheet" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${resultInstance?.callSheet}" /></span>
                            
                        </div>
                    
</div>
<div class="buttons"><g:form>
	<g:hiddenField name="id" value="${resultInstance?.id}" />
	<span class="button"><g:actionSubmit class="edit" action="edit"
		value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	<span class="button"><g:actionSubmit class="delete"
		action="delete"
		value="${message(code: 'default.button.delete.label', default: 'Delete')}"
		onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
</g:form></div>
</div>
</body>
</html>
