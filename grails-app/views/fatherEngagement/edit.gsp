


<%@ page import="edu.umn.ncs.FatherEngagement" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'fatherEngagement.label', default: 'FatherEngagement')}" />
	<title>Edit Father Engagement Form</title>
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
		<span class="menuButton"><g:link class="list" action="list">Father Engagement Form List</g:link></span>
		<span class="menuButton"><g:link class="create" action="create">Enter Father Engagement Form</g:link></span>
	</div>
	<div class="body">
		<h1>Edit Father Engagement Form</h1>
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<g:hasErrors bean="${fatherEngagementInstance}">
			<div class="errors"><g:renderErrors bean="${fatherEngagementInstance}" as="list" />
		</div>
		</g:hasErrors>
		<g:form method="post" >			
			<g:hiddenField name="id" value="${fatherEngagementInstance?.id}" />
			<g:hiddenField name="version" value="${fatherEngagementInstance?.version}" />
			<g:hiddenField name="interviewDate" value="${fatherEngagementInstance?.interviewDate}" />
			<div class="dialog">
				
				<div class="prop">
				    <span class="name">
				      <label for="trackedItem"><g:message code="fatherEngagement.trackedItem.label" default="Lookup Mother" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'trackedItem', 'errors')}">
				        <g:link controller="person" action="show" id="${fatherEngagementInstance?.trackedItem?.person.id}" >${fatherEngagementInstance?.trackedItem?.person}</g:link>
				        <%--<g:link controller="trackedItem" action="show" id="${fatherEngagementInstance?.trackedItem?.id}" >${fatherEngagementInstance?.trackedItem?.id}</g:link> --%>
				    </span>
				</div>
				
				<%--<div class="prop">
				    <span class="name">
				      <label for="trackedItem"><g:message code="fatherEngagement.trackedItem.label" default="Tracked Item ID" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'trackedItem', 'errors')}">
				        <g:link controller="trackedItem" action="show" id="${fatherEngagementInstance?.trackedItem?.id}" >${fatherEngagementInstance?.trackedItem?.id}</g:link>
				    </span>
				</div>  --%>
				
				<div class="prop">
				    <span class="name">
				      <label for="interviewStartTime"><g:message code="fatherEngagement.interviewStartTime.label" default="Interview Start Time" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'interviewStartTime', 'errors')}">
				        <g:datePicker name="interviewStartTime" precision="minute" value="${fatherEngagementInstance?.interviewStartTime}" noSelection="['': '']" />
				    </span>
				</div>
				
				<div class="prop">
				    <span class="name">
				      <label for="interviewEndTime"><g:message code="fatherEngagement.interviewEndTime.label" default="Interview End Time" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'interviewEndTime', 'errors')}">
				        <g:datePicker name="interviewEndTime" precision="minute" value="${fatherEngagementInstance?.interviewEndTime}" noSelection="['': '']" />
				    </span>
				</div>
				
				<%--<div class="prop">
				    <span class="name">
				      <label for="interviewDate"><g:message code="fatherEngagement.interviewDate.label" default="Interview Date" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'interviewDate', 'errors')}">
				        <g:datePicker name="interviewDate" precision="day" value="${fatherEngagementInstance?.interviewDate}" noSelection="['': '']" />
				    </span>
				</div> --%>
				
				<div class="prop">
				    <span class="name">
				        <label for="interviewerInitials"><g:message code="fatherEngagement.interviewerInitials.label" default="Interviewer Initials" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'interviewerInitials', 'errors')}">
				        <g:textField name="interviewerInitials" value="${fatherEngagementInstance?.interviewerInitials}" />
				    </span>
				</div>
		
				<div class="prop">
				    <span class="name">
				        <label for="interviewer"><g:message code="fatherEngagement.interviewer.label" default="Interviewer" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'interviewer', 'errors')}">
				        <%--<g:textField name="interviewer" value="${fatherEngagementInstance?.interviewer}" /> --%>
				        <g:select name="interviewer"
				        	from='${memberInstanceList}'
				        	optionKey="username"
				        	optionValue="displayName"
				        	value="${fatherEngagementInstance?.interviewer}" />
				    </span>
				</div>
				
				<div class="prop">
				    <span class="name">
				        <label for="psuId"><g:message code="fatherEngagement.psuId.label" default="PSU Number" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'psuId', 'errors')}">
				        <g:textField name="psuId" value="${fatherEngagementInstance?.psuId}" />
				    </span>
				</div>
				
				<div class="prop">
				    <span class="name">
				      <label for="fatherPresent"><g:message code="fatherEngagement.fatherPresent.label" default="Was the father (or partner) present during the consent visit?" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'fatherPresent', 'errors')}">
				        <g:checkBox name="fatherPresent" value="${fatherEngagementInstance?.fatherPresent}" />
				    </span>
				</div>
				
				<div class="prop">
				    <span class="name">
				        <label for="discussNeedToKnow">Did you review and discuss the "WHAT FATHER AND SIGNIFICANT OTHERS NEED TO KNOW ABOUT<br /> THE NCS" document with the father (or partner)?</label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'discussNeedToKnow', 'errors')}">
				        <g:checkBox name="discussNeedToKnow" value="${fatherEngagementInstance?.discussNeedToKnow}" />
				    </span>
				</div>
				
				<div class="prop">
				    <span class="name">
				      <label for="signAsWitness"><g:message code="fatherEngagement.signAsWitness.label" default="Did the father sign the consent form as a witness?" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'signAsWitness', 'errors')}">
				        <g:checkBox name="signAsWitness" value="${fatherEngagementInstance?.signAsWitness}" />
				    </span>
				</div>
                        
			</div>
			<div class="buttons">
				<span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
				<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
			</div>
		</g:form>
	</div>
</body>
</html>
