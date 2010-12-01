

<%@ page import="edu.umn.ncs.InstrumentApproval" %>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
  <g:set var="entityName" value="${message(code: 'instrumentApproval.label', default: 'Instrument Approval')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
	<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
	<span class="menuButton"><g:link class="list" controller="instrumentHistory" action="show" id="${instrumentApprovalInstance?.instrumentHistory?.id}">Back to Instrument Revision</g:link></span>
  </div>
  <div class="body">
	<h1>New Instrument Approval</h1>
	<g:if test="${flash.message}">
	  <div class="message">${flash.message}</div>
	</g:if>
	<g:hasErrors bean="${instrumentApprovalInstance}">
	  <div class="errors">
		<g:renderErrors bean="${instrumentApprovalInstance}" as="list" />
	  </div>
	</g:hasErrors>
	<g:form action="save" method="post" >
	  <div class="dialog">

		<div class="prop">
		  <g:hiddenField name="instrumentHistory.id" value="${instrumentApprovalInstance?.instrumentHistory?.id}" />
		  <h2>
			${instrumentApprovalInstance?.instrumentHistory?.isInitial.toString().capitalize()}
			${instrumentApprovalInstance?.instrumentHistory?.instrument},
			v${instrumentApprovalInstance?.instrumentHistory?.itemVersion}
		  </h2>
		</div>

		<div class="prop">
		  <span class="name">
			<label for="approvalDate"><g:message code="instrumentApproval.approvalDate.label" default="Approval Date" /></label>
		  </span>
		  <span class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'approvalDate', 'errors')}">
			<g:datePicker name="approvalDate" precision="day" value="${instrumentApprovalInstance?.approvalDate}"  />
		  </span>
		</div>

		<div class="prop">
		  <span class="name">
			<label for="approvedBy"><g:message code="instrumentApproval.approvedBy.label" default="Approved By" /></label>
		  </span>
		  <span class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'approvedBy', 'errors')}">
			<g:textField name="approvedBy" value="${instrumentApprovalInstance?.approvedBy}" />
		  </span>
		</div>

		<div class="prop">
		  <span class="name">
			<label for="approvalType"><g:message code="instrumentApproval.approvalType.label" default="Approval Type" /></label>
		  </span>
		  <span class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'approvalType', 'errors')}">
			<g:select name="approvalType.id" from="${edu.umn.ncs.InstrumentApprovalType.list()}" optionKey="id" value="${instrumentApprovalInstance?.approvalType?.id}"  />
		  </span>
		</div>

		<div class="prop">
		  <span class="name">
			<label for="comments"><g:message code="instrumentApproval.comments.label" default="Comments" /></label>
		  </span>
		  <span class="value ${hasErrors(bean: instrumentApprovalInstance, field: 'comments', 'errors')}">
			<g:textArea name="comments" value="${instrumentApprovalInstance?.comments}" rows="5" cols="40" />
		  </span>
		</div>

	  </div>
	  <div class="buttons">
		<span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
	  </div>
	</g:form>
  </div>
</body>
</html>
