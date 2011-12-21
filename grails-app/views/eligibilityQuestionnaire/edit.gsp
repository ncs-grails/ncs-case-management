<%@ page import="edu.umn.ncs.instruments.EligibilityQuestionnaire" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="ncs" />
		<g:set var="entityName" value="${message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		<g:javascript src="eligibilityQuestionnaire.js" />
		<g:javascript plugin="address-lookup-zpfour" src="address-lookup-zp4.js" />
		<g:addressLookupOnChange certifiedOut="standardized" />
	</head>
	<body>
		<div class="nav">
			<span class="menuButton">
				<a class="home" href="${createLink(uri: '/')}">
					<g:message code="default.home.label" />
				</a>
			</span>
			<span class="menuButton">
				<g:link class="list" controller="dataEntry">
				<g:message code="dataEntry.index.label" args="[entityName]" />
				</g:link>
			</span>
		</div>
		<div class="body">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
			</g:if> <g:hasErrors bean="${eligibilityQuestionnaireInstance}">
			<div class="errors">
				<g:renderErrors bean="${eligibilityQuestionnaireInstance}" as="list" />
			</div>
			</g:hasErrors> <g:form method="post" autocomplete="off" >
			<g:hiddenField name="id" value="${eligibilityQuestionnaireInstance?.id}" />
			<g:hiddenField name="version" value="${eligibilityQuestionnaireInstance?.version}" />
			<div class="dialog">
				<g:render template="form" bean="${eligibilityQuestionnaireInstance}" var="eligibilityQuestionnaireInstance" />
			</div>
			<div class="buttons">
				<span class="button">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</span>
			</div>
			</g:form>
		</div>
	</body>
</html>
