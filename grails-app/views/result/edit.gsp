


<%@ page import="edu.umn.ncs.Result" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'result.label', default: 'Result')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
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
<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if> <g:hasErrors bean="${resultInstance}">
	<div class="errors"><g:renderErrors bean="${resultInstance}"
		as="list" /></div>
</g:hasErrors> <g:form method="post"
	>
	<g:hiddenField name="id" value="${resultInstance?.id}" />
	<g:hiddenField name="version" value="${resultInstance?.version}" />
	<div class="dialog">
	
                            <div class="prop">
                                <span class="name">
                                  <label for="name"><g:message code="result.name.label" default="Name" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="50" value="${resultInstance?.name}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="abbreviation"><g:message code="result.abbreviation.label" default="Abbreviation" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'abbreviation', 'errors')}">
                                    <g:textField name="abbreviation" maxlength="15" value="${resultInstance?.abbreviation}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="format"><g:message code="result.format.label" default="Format" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'format', 'errors')}">
                                    <g:select name="format.id" from="${edu.umn.ncs.InstrumentFormat.list()}" optionKey="id" value="${resultInstance?.format?.id}" noSelection="['null': '']" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="ineligible"><g:message code="result.ineligible.label" default="Ineligible" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'ineligible', 'errors')}">
                                    <g:checkBox name="ineligible" value="${resultInstance?.ineligible}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="resend"><g:message code="result.resend.label" default="Resend" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'resend', 'errors')}">
                                    <g:checkBox name="resend" value="${resultInstance?.resend}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="disqualify"><g:message code="result.disqualify.label" default="Disqualify" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'disqualify', 'errors')}">
                                    <g:checkBox name="disqualify" value="${resultInstance?.disqualify}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="refused"><g:message code="result.refused.label" default="Refused" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'refused', 'errors')}">
                                    <g:checkBox name="refused" value="${resultInstance?.refused}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="valid"><g:message code="result.valid.label" default="Valid" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'valid', 'errors')}">
                                    <g:checkBox name="valid" value="${resultInstance?.valid}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="photoCopy"><g:message code="result.photoCopy.label" default="Photo Copy" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'photoCopy', 'errors')}">
                                    <g:checkBox name="photoCopy" value="${resultInstance?.photoCopy}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="requiresPrimaryContact"><g:message code="result.requiresPrimaryContact.label" default="Requires Primary Contact" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'requiresPrimaryContact', 'errors')}">
                                    <g:checkBox name="requiresPrimaryContact" value="${resultInstance?.requiresPrimaryContact}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="phoneCall"><g:message code="result.phoneCall.label" default="Phone Call" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'phoneCall', 'errors')}">
                                    <g:checkBox name="phoneCall" value="${resultInstance?.phoneCall}" />
                                </span>
                            </div>
                        
                            <div class="prop">
                                <span class="name">
                                  <label for="callSheet"><g:message code="result.callSheet.label" default="Call Sheet" /></label>
                                </span>
                                <span class="value ${hasErrors(bean: resultInstance, field: 'callSheet', 'errors')}">
                                    <g:checkBox name="callSheet" value="${resultInstance?.callSheet}" />
                                </span>
                            </div>
                        
	</div>
	<div class="buttons"><span class="button"><g:actionSubmit
		class="save" action="update"
		value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
	<span class="button"><g:actionSubmit class="delete"
		action="delete"
		value="${message(code: 'default.button.delete.label', default: 'Delete')}"
		onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	</div>
</g:form></div>
</body>
</html>
