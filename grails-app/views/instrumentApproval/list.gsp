
<%@ page import="edu.umn.ncs.InstrumentApproval"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'instrumentApproval.label', default: 'InstrumentApproval')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
<span class="menuButton"><g:link class="create" action="create">
	<g:message code="default.new.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.list.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">
	${flash.message}
	</div>
</g:if>
<div class="list">
<table>
	<thead>
		<tr>

			<g:sortableColumn property="id"
				title="${message(code: 'instrumentApproval.id.label', default: 'Id')}" />

			<th><g:message code="instrumentApproval.instrumentHistory.label"
				default="Instrument History" /></th>

			<g:sortableColumn property="approvalDate"
				title="${message(code: 'instrumentApproval.approvalDate.label', default: 'Approval Date')}" />

			<g:sortableColumn property="approvedBy"
				title="${message(code: 'instrumentApproval.approvedBy.label', default: 'Approved By')}" />

			<th><g:message code="instrumentApproval.approvalType.label"
				default="Approval Type" /></th>

			<g:sortableColumn property="comments"
				title="${message(code: 'instrumentApproval.comments.label', default: 'Comments')}" />

		</tr>
	</thead>
	<tbody>
		<g:each in="${instrumentApprovalInstanceList}" status="i"
			var="instrumentApprovalInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

				<td><g:link action="show" id="${instrumentApprovalInstance.id}">
					${fieldValue(bean: instrumentApprovalInstance, field: "id")}
				</g:link></td>

				<td>
				${fieldValue(bean: instrumentApprovalInstance, field: "instrumentHistory")}
				</td>

				<td><g:formatDate
					date="${instrumentApprovalInstance.approvalDate}" /></td>

				<td>
				${fieldValue(bean: instrumentApprovalInstance, field: "approvedBy")}
				</td>

				<td>
				${fieldValue(bean: instrumentApprovalInstance, field: "approvalType")}
				</td>

				<td>
				${fieldValue(bean: instrumentApprovalInstance, field: "comments")}
				</td>

			</tr>
		</g:each>
	</tbody>
</table>
</div>
<div class="paginateButtons"><g:paginate
	total="${instrumentApprovalInstanceTotal}" /></div>
</div>
</body>
</html>
