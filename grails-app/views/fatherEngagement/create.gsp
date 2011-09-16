


<%@ page import="edu.umn.ncs.FatherEngagement" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'fatherEngagement.label', default: 'FatherEngagement')}" />
	<title>Enter Father Engagement Form</title>
	<g:javascript src="father-engagement.js" />
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
		<span class="menuButton"><g:link class="list" action="list">Father Engagement Form List</g:link></span>
	</div>
	<div class="body">
		<h1>Enter Father Engagement Form</h1>
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<g:hasErrors bean="${fatherEngagementInstance}">
			<div class="errors"><g:renderErrors bean="${fatherEngagementInstance}" as="list" /></div>
		</g:hasErrors>
		
		<div id="errorDiv" class="errors">
			<label id="errorLabel"></label>
		</div>
		
		<g:form action="save" method="post" onsubmit="return validateForm();" >
			<div class="dialog">

				<div id="person-info" class="prop">
				    <span class="name">
				        <label for="personInfo">Participant Name (mother)</label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'trackedItem', 'errors')}">
				        <%--<label id="person-info-status" for="personInfo"></label> --%>
						<div id="resultLog" class="grid_12" ></div>
				    </span>
				</div>
				
				<div class="prop">
				    <span class="name">
				        <label for="trackedItem"><g:message code="fatherEngagement.trackedItem.label" default="Tracked Item ID" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'trackedItem', 'errors')}">
				    	<g:textField name="trackedItem" value="" />
				    </span>
				</div>
				
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
				        <g:datePicker name="interviewEndTime" precision="minute" default="none" noSelection="['null': '--']" />
				    </span>
				</div>
				
				<%--<div class="prop">
				    <span class="name">
				        <label for="interviewDate"><g:message code="fatherEngagement.interviewDate.label" default="Interview Date" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'interviewDate', 'errors')}">
				        <g:datePicker name="interviewDate" precision="day" value="${fatherEngagementInstance?.interviewDate}" noSelection="['': '']" />
				    </span>
				</div>  --%>
				
				<div class="prop">
				    <span class="name">
				        <label for="psuId"><g:message code="fatherEngagement.psuId.label" default="PSU Number" /></label>
				    </span>
				    <span class="value ${hasErrors(bean: fatherEngagementInstance, field: 'psuId', 'errors')}">
				        <g:textField name="psuId" value="20000048" />
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
				<span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
			</div>
		</g:form>
		
            
		<g:form action="getPersonInfo" name="personInfoForm">
			<g:hiddenField name="trackedItemInstance" value="${trackedItem}" />
		</g:form>
		
	</div>
</body>
</html>
