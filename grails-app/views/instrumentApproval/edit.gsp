

<%@ page import="edu.umn.ncs.InstrumentApproval"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'instrumentApproval.label', default: 'InstrumentApproval')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
<span class="menuButton"><g:link class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span> <span class="menuButton"><g:link class="create" action="create">
	<g:message code="default.new.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">
	${flash.message}
	</div>
</g:if> <g:hasErrors bean="${instrumentApprovalInstance}">
	<div class="errors"><g:renderErrors
		bean="${instrumentApprovalInstance}" as="list" /></div>
</g:hasErrors> <g:form method="post">
	<g:hiddenField name="id" value="${instrumentApprovalInstance?.id}" />
	<g:hiddenField name="version"
		value="${instrumentApprovalInstance?.version}" />
	<div class="dialog">

	<div class="prop"><span class="name"> <label
		for="instrumentHistory"><g:message
		code="instrumentApproval.instrumentHistory.label"
		default="Instrument History" /></label> </span> <span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'instrumentHistory', 'errors')}">
	<g:select name="instrumentHistory.id"
		from="${edu.umn.ncs.InstrumentHistory.list()}" optionKey="id"
		value="${instrumentApprovalInstance?.instrumentHistory?.id}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="approvalDate"><g:message
		code="instrumentApproval.approvalDate.label" default="Approval Date" /></label>
	</span> <span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'approvalDate', 'errors')}">
	<g:datePicker name="approvalDate" precision="day"
		value="${instrumentApprovalInstance?.approvalDate}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="approvedBy"><g:message
		code="instrumentApproval.approvedBy.label" default="Approved By" /></label> </span>
	<span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'approvedBy', 'errors')}">
	<g:textField name="approvedBy"
		value="${instrumentApprovalInstance?.approvedBy}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="approvalType"><g:message
		code="instrumentApproval.approvalType.label" default="Approval Type" /></label>
	</span> <span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'approvalType', 'errors')}">
	<g:select name="approvalType.id"
		from="${edu.umn.ncs.InstrumentApprovalType.list()}" optionKey="id"
		value="${instrumentApprovalInstance?.approvalType?.id}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="comments"><g:message
		code="instrumentApproval.comments.label" default="Comments" /></label> </span> <span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'comments', 'errors')}">
	<g:textField name="comments"
		value="${instrumentApprovalInstance?.comments}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="userCreated"><g:message
		code="instrumentApproval.userCreated.label" default="User Created" /></label>
	</span> <span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'userCreated', 'errors')}">
	<g:textField name="userCreated"
		value="${instrumentApprovalInstance?.userCreated}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="appCreated"><g:message
		code="instrumentApproval.appCreated.label" default="App Created" /></label> </span>
	<span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'appCreated', 'errors')}">
	<g:textField name="appCreated"
		value="${instrumentApprovalInstance?.appCreated}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="userUpdated"><g:message
		code="instrumentApproval.userUpdated.label" default="User Updated" /></label>
	</span> <span
		class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'userUpdated', 'errors')}">
	<g:textField name="userUpdated"
		value="${instrumentApprovalInstance?.userUpdated}" /> </span></div>

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
