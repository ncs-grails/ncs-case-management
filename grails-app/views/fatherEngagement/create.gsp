


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
		<span class="menuButton"><g:link class="create" action="create">Enter Father Engagement Form</g:link></span>
	</div>
	<div class="body">
		<h1>Enter Father Engagement Form</h1>
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<g:hasErrors bean="${fatherEngagementInstance}">
			<div class="errors"><g:renderErrors bean="${fatherEngagementInstance}" as="list" /></div>
		</g:hasErrors>

		<div id="searching" ><strong>Searching</strong> <img src="../images/spinner.gif" title="spinner" /></div>
		
		<div id="notFound" class="errors">
			<label id="errorLabel">${result?.errorText}</label>
		</div>
		
		<div id="formContainer" >		
			<g:form>	
				<div class="prop">
				    <span class="name">
				        <label for="trackedItemId">Tracked Item ID</label>
				    </span>
				    <span class="value">
				    	<g:textField name="trackedItemId" value="" />
				    	<g:submitToRemote value="Find"
				    		url="[controller:'fatherEngagement', action:'updatePersonInfo']"
				    		update="formContainer"
				    		onLoading="showSearching()"
				    		onComplete="hideSearching()"
				    		onSuccess=""
				    		onFailure="showFailure()" />
				    </span>		
				</div>
			</g:form>
		</div>				
	</div>
</body>
</html>
