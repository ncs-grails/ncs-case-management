<%@ page import="edu.umn.ncs.TrackedItem" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'trackedItem.label', default: 'Tracked Item')}" />
	<title><g:message code="default.edit.label" args="[entityName]" /></title>
	<g:javascript src="trackedItem.js"/>
</head>

<body>
	<div class="nav">
		<span class="menuButton">
			<a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a>
		</span> 
		<span class="menuButton">
			<g:set var="batch_id" value="${trackedItemInstance?.batchId}"/>
			<g:link class="list" controller="batch" action="edit" params="[id: trackedItemInstance?.batchId]">
				<g:message code="default.edit.label" args="['Batch']" />
			</g:link>
		</span> 
	</div>
	
	<div class="body">
		<h1><g:message code="default.edit.label" args="[entityName]" /> ID: ${trackedItemInstance?.id}</h1>
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if> 
			
			<g:if test="${message}">
				<div class="message">${message}</div>
			</g:if> 			
			
			<g:hasErrors bean="${trackedItemInstance}">
				<div class="errors">
					<g:renderErrors bean="${trackedItemInstance}" as="list" />
				</div>
			</g:hasErrors> 
			
			<g:form method="post" name="editTrackedItem">
				<g:hiddenField name="id" value="${trackedItemInstance?.id}" />
				<g:hiddenField name="version" value="${trackedItemInstance?.version}" />
				<div class="dialog">
	
			       <div class="prop">
			           <span class="name">
			             <label for="studyYear"><g:message code="trackedItem.studyYear.label" default="Study Year" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'studyYear', 'errors')}">
			               <g:textField name="studyYear" value="${fieldValue(bean: trackedItemInstance, field: 'studyYear')}" />
			           </span>
			       </div>
			   
			       <div class="prop">
			           <span class="name">
			             <label for="expiration"><g:message code="trackedItem.expiration.label" default="Expiration" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'expiration', 'errors')}">
						   <%--
						   <g:datePicker name="expiration" precision="day" value="${trackedItemInstance?.expiration}" noSelection="['': '']" />
						   --%>
			               <label><g:formatDate date="${trackedItemInstance?.expiration}" format="M/d/yyyy"/></label>
			           </span>
			       </div>
			   
			       <div class="prop">
			           <span class="name">
			             <label for="parentItem"><g:message code="trackedItem.parentItem.label" default="Parent Item" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'parentItem', 'errors')}">
			           	<g:textField name="parentItem.id" value="${trackedItemInstance?.parentItem?.id}"/>
			           </span>
			       </div>
			   
			       <div class="prop">
			           <span class="name">
			             <label for="streetAddress"><g:message code="trackedItem.streetAddress.label" default="Street Address" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'streetAddress', 'errors')}">
			               <label>${trackedItemInstance?.streetAddress?.address} ${trackedItemInstance?.streetAddress?.cityStateZip}</label>
			           </span>
			       </div>
		
		
			   <fieldset class="maroonBorder">
			   <legend>To delete result select blank item in RECEIPT DATE & RESULT drop-down lists</legend>
			       <div class="prop">
			           <span class="name">
			             <label for="receiptDate">Receipt Date</label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'result', 'errors')}">
			               <g:datePicker name="receiptDate" value="${trackedItemInstance?.result?.receivedDate}" precision="day" years="[2011,2012]" default="none" noSelection="['': '']"/>
			           </span>	       
			       
			           <span class="name">
			             <label for="result"><g:message code="trackedItem.result.label" default="Result" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'result', 'errors')}">
			               <g:select name="result.id" from="${edu.umn.ncs.Result.list().sort{it.name}}" optionKey="id" value="${trackedItemInstance?.result?.result?.id}" optionValue="${{it?.name}}" noSelection="['null': '']" />
			           </span>
			           
			           <!-- 
						<span class="buttons">
							<g:actionSubmit class="delete" action="deleteItemResult" value="Delete Result" />
						</span>
						  -->			           
			       </div>
			   </fieldset>
			       
			       
			   
			       <div class="prop">
			           <span class="name">
			             <label for="dwellingUnit"><g:message code="trackedItem.dwellingUnit.label" default="Dwelling Unit" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'dwellingUnit', 'errors')}">
			               <label>Dwelling Unit ID: ${trackedItemInstance?.dwellingUnit?.id}, ${trackedItemInstance?.dwellingUnit?.address?.address} ${trackedItemInstance?.dwellingUnit?.address?.cityStateZip}</label>
			           </span>
			       </div>
			   
			       <div class="prop">
			           <span class="name">
			             <label for="person"><g:message code="trackedItem.person.label" default="Person" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'person', 'errors')}">
			               <label>Person ID: ${trackedItemInstance?.person?.id} ${trackedItemInstance?.person?.fullName}</label>
			           </span>
			       </div>
			   
			       <div class="prop">
			           <span class="name">
			             <label for="household"><g:message code="trackedItem.household.label" default="Household" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'household', 'errors')}">
			               <%--  <g:select name="household.id" from="${edu.umn.ncs.Household.list()}" optionKey="id" value="${trackedItemInstance?.household?.id}" noSelection="['null': '']" />  --%>
			               <label>${trackedItemInstance?.household?.name}</label>
			           </span>
			       </div>
			   
			       <div class="prop">
			           <span class="name">
			             <label for="batch"><g:message code="trackedItem.batch.label" default="Batch" /></label>
			           </span>
			           <span class="value ${hasErrors(bean: trackedItemInstance, field: 'batch', 'errors')}">
			               <label>Batch ID: ${trackedItemInstance?.batch?.id}, ${trackedItemInstance?.batch?.primaryInstrument?.name}</label>
			           </span>
			       </div>            
			</div>
			<div class="buttons">
				<span class="button">
				<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" onClick="return confirmUpdateItem(${trackedItemInstance?.studyYear}, ${trackedItemInstance?.parentItem?.id}, ${trackedItemInstance?.result?.receivedDate}, ${trackedItemInstance?.result?.result?.id});"/>
					<!-- <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" onClick="return confirmUpdateItem(${trackedItemInstance?.studyYear}, ${trackedItemInstance?.parentItem?.id}, ${trackedItemInstance?.result?.receivedDate}, ${trackedItemInstance?.result?.result?.id});"/> -->
				</span>
			</div>
		</g:form>
</div>
</body>
</html>
