

<%@ page import="edu.umn.ncs.FatherEngagement" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'fatherEngagement.label', default: 'FatherEngagement')}" />
	<title>Father Engagement Form</title>
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
		<span class="menuButton"><g:link class="create" action="create">New Father Engagement Form</g:link></span>
		<span class="menuButton"><g:link class="list" action="downloadDataset">Download Father Engagement Data</g:link></span>
	</div>
	<div class="body">
		<h1>Father Engagement Form List</h1>
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<div class="list">
			<table>
				<thead>
					<tr>
		
                           <g:sortableColumn property="id" title="${message(code: 'fatherEngagement.id.label', default: 'Id')}" />                        
                       
                           <g:sortableColumn property="interviewDate" title="${message(code: 'fatherEngagement.interviewDate.label', default: 'Interview Date')}" />
                       
                           <g:sortableColumn property="fatherPresent" title="${message(code: 'fatherEngagement.fatherPresent.label', default: 'Father Present')}" />
                       
                           <g:sortableColumn property="discussNeedToKnow" title="${message(code: 'fatherEngagement.discussNeedToKnow.label', default: 'Discussed Need To Know')}" />
                       
                           <g:sortableColumn property="signAsWitness" title="${message(code: 'fatherEngagement.signAsWitness.label', default: 'Father Signed as Witness')}" />
                       
					</tr>
				</thead>
				<tbody>
					<g:each in="${fatherEngagementInstanceList}" status="i" var="fatherEngagementInstance">
						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				
                            <td><g:link action="edit" id="${fatherEngagementInstance.id}">${fieldValue(bean: fatherEngagementInstance, field: "id")}</g:link></td>
                        
                            <td><g:formatDate date="${fatherEngagementInstance?.interviewDate}" format="MM/dd/yyyy" /></td>
                        
                            <td><g:formatBoolean boolean="${fatherEngagementInstance?.fatherPresent}" true="Yes" false="No" /></td>
                        
                            <td><g:formatBoolean boolean="${fatherEngagementInstance?.discussNeedToKnow}" true="Yes" false="No" /></td>
                        
                            <td><g:formatBoolean boolean="${fatherEngagementInstance?.signAsWitness}" true="Yes" false="No" /></td>
			                        
						</tr>
					</g:each>
				</tbody>
			</table>
			</div>
		<div class="paginateButtons">
			<g:paginate total="${fatherEngagementInstanceTotal}" />
		</div>
	</div>
</body>
</html>
