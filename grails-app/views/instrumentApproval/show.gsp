
<%@ page import="edu.umn.ncs.InstrumentApproval" %>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
  <g:set var="entityName" value="${message(code: 'instrumentApproval.label', default: 'InstrumentApproval')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
	<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
	<span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
	<span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
	<h1><g:message code="default.show.label" args="[entityName]" /></h1>
	<g:if test="${flash.message}">
	  <div class="message">${flash.message}</div>
	</g:if>
	<div class="dialog">

	  <div class="prop">
		<span class="name">Approved Item</span>

		<span class="value">
		  <g:link controller="instrumentHistory" action="show" id="${instrumentApprovalInstance?.instrumentHistory?.id}">
${instrumentApprovalInstance?.instrumentHistory?.isInitial.toString().capitalize()}
${instrumentApprovalInstance?.instrumentHistory?.instrument},
								v${instrumentApprovalInstance?.instrumentHistory?.itemVersion}
		  </g:link></span>

	  </div>

	  <div class="prop">
		<span class="name"><g:message code="instrumentApproval.approvalDate.label" default="Approval Date" /></span>

		<span class="value"><g:formatDate date="${instrumentApprovalInstance?.approvalDate}" format="yyyy-MM-dd"/></span>

	  </div>

	  <div class="prop">
		<span class="name"><g:message code="instrumentApproval.approvedBy.label" default="Approved By" /></span>

		<span class="value">${fieldValue(bean: instrumentApprovalInstance, field: "approvedBy")}</span>

	  </div>

	  <div class="prop">
		<span class="name"><g:message code="instrumentApproval.approvalType.label" default="Approval Type" /></span>

		<span class="value">${instrumentApprovalInstance?.approvalType?.encodeAsHTML()}</span>

	  </div>

	  <fieldset class="maroonBorder">
		<legend style="margin-left: 0.5em;"><g:message code="instrumentApproval.comments.label" default="Comments" /></legend>

		<span class="value">${fieldValue(bean: instrumentApprovalInstance, field: "comments")}</span>
	  </fieldset>

	  <fieldset class="maroonBorder">
		<legend style="margin-left: 0.5em;">Provenance Data</legend>

						  Created by ${fieldValue(bean: instrumentApprovalInstance, field: "userCreated")}
						  on <g:formatDate date="${instrumentApprovalInstance?.dateCreated}" />
						  using ${fieldValue(bean: instrumentApprovalInstance, field: "appCreated")}

		<g:if test="${instrumentApprovalInstance?.userUpdated}">
							Updated by ${instrumentApprovalInstance?.userUpdated}
							on <g:formatDate date="${instrumentApprovalInstance?.lastUpdated}" />
		</g:if>
	  </fieldset>

	</div>
	<div class="buttons">
	  <g:form>
		<g:hiddenField name="id" value="${instrumentApprovalInstance?.id}" />
		<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
		<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	  </g:form>
	</div>
  </div>
</body>
</html>
