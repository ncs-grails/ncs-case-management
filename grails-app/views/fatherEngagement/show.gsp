

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
		<span class="menuButton"><g:link class="list" action="list">Father Engagement Form List</g:link></span>
		<span class="menuButton"><g:link class="create" action="create">New Father Engagement Form</g:link></span>
	</div>
	<div class="body">
		<h1>Father Engagement Form</h1>
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<div class="dialog">
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.id.label" default="Id" /></span>
			    
			    <span class="value">${fieldValue(bean: fatherEngagementInstance, field: "id")}</span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.trackedItem.label" default="Tracked Item ID" /></span>
			    
			    <span class="value"><g:link controller="trackedItem" action="show" id="${fatherEngagementInstance?.trackedItem?.id}">${fatherEngagementInstance?.trackedItem?.encodeAsHTML()}</g:link></span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.interviewerInitials.label" default="Interviewer Initials" /></span>
			    
			    <span class="value">${fatherEngagementInstance?.interviewerInitials}</span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.interviewer.label" default="Interviewer" /></span>
			    
			    <span class="value">${fatherEngagementInstance?.interviewer}</span>
			    
			</div>
			
			<%--<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.interviewDate.label" default="Interview Date" /></span>
			    
			    <span class="value"><g:formatDate date="${fatherEngagementInstance?.interviewDate}" format="MM/dd/yyyy" /></span>
			    
			</div> --%>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.interviewStartTime.label" default="Interview Start Time" /></span>
			    
			    <span class="value"><g:formatDate date="${fatherEngagementInstance?.interviewStartTime}" format="MM/dd/yyyy h:mm a" /></span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.interviewEndTime.label" default="Interview End Time" /></span>
			    
			    <span class="value"><g:formatDate date="${fatherEngagementInstance?.interviewEndTime}" format="MM/dd/yyyy h:mm a" /></span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.psuId.label" default="Psu Id" /></span>
			    
			    <span class="value">${fieldValue(bean: fatherEngagementInstance, field: "psuId")}</span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.fatherPresent.label" default="Father Present" /></span>
			    
			    <span class="value"><g:formatBoolean boolean="${fatherEngagementInstance?.fatherPresent}" true="Yes" false="No" /></span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.discussNeedToKnow.label" default="Discussed Need To Know" /></span>
			    
			    <span class="value"><g:formatBoolean boolean="${fatherEngagementInstance?.discussNeedToKnow}" true="Yes" false="No" /></span>
			    
			</div>
			
			<div class="prop">
			    <span class="name"><g:message code="fatherEngagement.signAsWitness.label" default="Signed As Witness" /></span>
			    
			    <span class="value"><g:formatBoolean boolean="${fatherEngagementInstance?.signAsWitness}" true="Yes" false="No" /></span>
			    
			</div>
                    
		</div>
		<div class="buttons">
			<g:form>
				<g:hiddenField name="id" value="${fatherEngagementInstance?.id}" />
				<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
				<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
			</g:form>
		</div>
	</div>
</body>
</html>
