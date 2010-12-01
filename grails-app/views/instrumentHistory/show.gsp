
<%@ page import="edu.umn.ncs.InstrumentHistory" %>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
	<title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
	<div class="nav">
	  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
	  <span class="menuButton"><g:link class="list" action="list">Back to Revision History</g:link></span>
	  <span class="menuButton"><g:link class="create" action="create" params="${ [ 'instrument.id': instrumentHistoryInstance?.instrument?.id, itemVersion: instrumentHistoryInstance?.itemVersion + 1 ] }">New Revision</g:link></span>
	</div>
	<div class="body">
	  <g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
	  </g:if>
	  <div class="dialog">

		<h2>
${instrumentHistoryInstance?.isInitial.toString().capitalize()}
${instrumentHistoryInstance?.instrument},
				v${instrumentHistoryInstance?.itemVersion}
		</h2>

		<div class="prop">
		  <span class="name"><g:message code="instrumentHistory.revisionType.label" default="Revision Type" /></span>

		  <span class="value">${instrumentHistoryInstance?.revisionType?.encodeAsHTML()}</span>

		</div>

		<div class="prop">
		  <span class="name"><g:message code="instrumentHistory.status.label" default="Status" /></span>

		  <span class="value">${instrumentHistoryInstance?.status?.encodeAsHTML()}</span>


		</div>

		<div class="prop">
		  <span class="name"><g:message code="instrumentHistory.startDate.label" default="Start Date" /></span>

		  <span class="value"><g:formatDate date="${instrumentHistoryInstance?.startDate}" /></span>
		</div>

		<g:if test="${instrumentHistoryInstance?.endDate}" >
		  <div class="prop">
			<span class="name"><g:message code="instrumentHistory.endDate.label" default="Retire Date" /></span>

			<span class="value"><g:formatDate date="${instrumentHistoryInstance?.endDate}" /></span>
		  </div>
		</g:if>


		<fieldset class="maroonBorder">
		  <legend style="margin-left: 0.5em;"><g:message code="instrumentHistory.comments.label" default="Comments" /></legend>

		  <span class="value">${fieldValue(bean: instrumentHistoryInstance, field: "comments")}</span>
		</fieldset>

		<fieldset class="maroonBorder">
		  <legend style="margin-left: 0.5em;"><g:message code="instrumentHistory.approvals.label" default="Approvals" /></legend>

		  <span style="text-align: left;" class="value">
			<ul>
			  <g:if test="${ ! instrumentHistoryInstance.approvals }">
				<li>None</li>
			  </g:if>
			  <g:each in="${instrumentHistoryInstance.approvals}" var="a">
				<li>
				<g:link controller="instrumentApproval" action="show" id="${a.id}">
				  ${a?.approvalType} approval
				  by ${a?.approvedBy}
				  on <g:formatDate date="${a?.approvalDate}" format="yyyy-MM-dd"/>
				</g:link>
				</li>
			  </g:each>

			  <li>
			  <g:link controller="instrumentApproval" action="create" params="['instrumentHistory.id': instrumentHistoryInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'instrumentApproval.label', default: 'Approval')])}</g:link>
			  </li>
			</ul>
		  </span>
		</fieldset>

		<fieldset class="maroonBorder">
		  <legend style="margin-left: 0.5em;">Provenance Data</legend>

						  Created by ${fieldValue(bean: instrumentHistoryInstance, field: "userCreated")}
						  on <g:formatDate date="${instrumentHistoryInstance?.dateCreated}" />
						  using ${fieldValue(bean: instrumentHistoryInstance, field: "appCreated")}

		  <g:if test="${instrumentHistoryInstance?.userUpdated}">
							Updated by ${instrumentHistoryInstance?.userUpdated}
							on <g:formatDate date="${instrumentHistoryInstance?.lastUpdated}" />
		  </g:if>
		</fieldset>

	  </div>
	  <div class="buttons">
		<g:form>
		  <g:hiddenField name="id" value="${instrumentHistoryInstance?.id}" />
		  <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
		</g:form>
	  </div>
	</div>
  </body>
</html>
