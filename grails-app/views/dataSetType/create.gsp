<%@ page import="edu.umn.ncs.DataSetType" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'dataSetType.label', default: 'DataSetType')}" />
<title><g:message code="default.create.label"
	args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.create.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if> <g:hasErrors bean="${dataSetTypeInstance}">
	<div class="errors"><g:renderErrors bean="${dataSetTypeInstance}"
		as="list" /></div>
	</g:hasErrors> <g:form action="save" method="post" >
	<div class="dialog">
		<div class="prop">
			<span class="name">
				<label for="name"><g:message code="dataSetType.name.label" default="Name" /></label>
			</span>
			<span class="value ${hasErrors(bean: dataSetTypeInstance, field: 'name', 'errors')}">
				<g:textField name="name" value="${dataSetTypeInstance?.name}" />
			</span>
		</div>
	
		<div class="prop">
			<span class="name">
				<label for="code"><g:message code="dataSetType.code.label" default="Abbreviation/Code" /></label>
			</span>
			<span class="value ${hasErrors(bean: dataSetTypeInstance, field: 'code', 'errors')}">
				<g:textField name="code" maxlength="16" value="${dataSetTypeInstance?.code}" />
			</span>
		</div>
	</div>
	<div class="buttons"><span class="button"><g:submitButton
		name="create" class="save"
		value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
	</div>
</g:form></div>
</body>
</html>
