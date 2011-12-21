

<%@ page import="edu.umn.ncs.instruments.EligibilityQuestionnaire" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div class="nav">
		<span class="menuButton">
			<a class="home" href="${createLink(uri: '/')}">
				<g:message code="default.home.label" />
			</a>
		</span>
	</div>
<div class="body">
<h1><g:message code="default.list.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if>
<div class="list">
<table>
	<thead>
		<tr>
			
                            <g:sortableColumn property="id" title="${message(code: 'eligibilityQuestionnaire.id.label', default: 'Id')}" />
                        
                            <th><g:message code="eligibilityQuestionnaire.trackedItem.label" default="Tracked Item" /></th>
                        
                            <g:sortableColumn property="title" title="${message(code: 'eligibilityQuestionnaire.title.label', default: 'Title')}" />
                        
                            <g:sortableColumn property="firstName" title="${message(code: 'eligibilityQuestionnaire.firstName.label', default: 'First Name')}" />
                        
                            <g:sortableColumn property="middleName" title="${message(code: 'eligibilityQuestionnaire.middleName.label', default: 'Middle Name')}" />
                        
                            <g:sortableColumn property="lastName" title="${message(code: 'eligibilityQuestionnaire.lastName.label', default: 'Last Name')}" />
                        
		</tr>
	</thead>
	<tbody>
		<g:each in="${eligibilityQuestionnaireInstanceList}" status="i" var="eligibilityQuestionnaireInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				
                            <td><g:link action="show" id="${eligibilityQuestionnaireInstance.id}">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: eligibilityQuestionnaireInstance, field: "trackedItem")}</td>
                        
                            <td>${fieldValue(bean: eligibilityQuestionnaireInstance, field: "title")}</td>
                        
                            <td>${fieldValue(bean: eligibilityQuestionnaireInstance, field: "firstName")}</td>
                        
                            <td>${fieldValue(bean: eligibilityQuestionnaireInstance, field: "middleName")}</td>
                        
                            <td>${fieldValue(bean: eligibilityQuestionnaireInstance, field: "lastName")}</td>
                        
			</tr>
		</g:each>
	</tbody>
</table>
</div>
<div class="paginateButtons"><g:paginate
	total="${eligibilityQuestionnaireInstanceTotal}" /></div>
</div>
</body>
</html>
