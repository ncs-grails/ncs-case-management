

<%@ page import="edu.umn.ncs.instruments.EligibilityQuestionnaire" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'eligibilityQuestionnaire.label', default: 'EligibilityQuestionnaire')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span> <span class="menuButton"><g:link class="create" action="create">
	<g:message code="default.new.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.show.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if>
<div class="dialog">

                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.id.label" default="Id" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "id")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.trackedItem.label" default="Tracked Item" /></span>
                            
                            <span class="value"><g:link controller="trackedItem" action="show" id="${eligibilityQuestionnaireInstance?.trackedItem?.id}">${eligibilityQuestionnaireInstance?.trackedItem?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.title.label" default="Title" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "title")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.firstName.label" default="First Name" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "firstName")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.middleName.label" default="Middle Name" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "middleName")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.lastName.label" default="Last Name" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "lastName")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.suffix.label" default="Suffix" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "suffix")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.gender.label" default="Gender" /></span>
                            
                            <span class="value"><g:link controller="gender" action="show" id="${eligibilityQuestionnaireInstance?.gender?.id}">${eligibilityQuestionnaireInstance?.gender?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.useExistingStreetAddress.label" default="Use Existing Street Address" /></span>
                            
                            <span class="value"><g:link controller="streetAddress" action="show" id="${eligibilityQuestionnaireInstance?.useExistingStreetAddress?.id}">${eligibilityQuestionnaireInstance?.useExistingStreetAddress?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.address.label" default="Address" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "address")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.address2.label" default="Address2" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "address2")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.city.label" default="City" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "city")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.state.label" default="State" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "state")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.zipCode.label" default="Zip Code" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "zipCode")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.zip4.label" default="Zip4" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "zip4")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.internationalPostalCode.label" default="International Postal Code" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "internationalPostalCode")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.country.label" default="Country" /></span>
                            
                            <span class="value"><g:link controller="country" action="show" id="${eligibilityQuestionnaireInstance?.country?.id}">${eligibilityQuestionnaireInstance?.country?.encodeAsHTML()}</g:link></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.standardized.label" default="Standardized" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${eligibilityQuestionnaireInstance?.standardized}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.uspsDeliverable.label" default="Usps Deliverable" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${eligibilityQuestionnaireInstance?.uspsDeliverable}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.dateCreated.label" default="Date Created" /></span>
                            
                            <span class="value"><g:formatDate date="${eligibilityQuestionnaireInstance?.dateCreated}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.userCreated.label" default="User Created" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "userCreated")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.appCreated.label" default="App Created" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "appCreated")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.lastUpdated.label" default="Last Updated" /></span>
                            
                            <span class="value"><g:formatDate date="${eligibilityQuestionnaireInstance?.lastUpdated}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="eligibilityQuestionnaire.userUpdated.label" default="User Updated" /></span>
                            
                            <span class="value">${fieldValue(bean: eligibilityQuestionnaireInstance, field: "userUpdated")}</span>
                            
                        </div>
                    
</div>
<div class="buttons"><g:form>
	<g:hiddenField name="id" value="${eligibilityQuestionnaireInstance?.id}" />
	<span class="button"><g:actionSubmit class="edit" action="edit"
		value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	<span class="button"><g:actionSubmit class="delete"
		action="delete"
		value="${message(code: 'default.button.delete.label', default: 'Delete')}"
		onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
</g:form></div>
</div>
</body>
</html>
