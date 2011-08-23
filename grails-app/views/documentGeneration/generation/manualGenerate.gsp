<%@ page import="edu.umn.ncs.BatchCreationConfig"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery" />

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Manual Document Generation - National Children's Study</title>
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'documentGeneration.label', default: 'DocumentGeneration')}" />
	<g:javascript src="manualgenerate.js" />
	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'documentGeneration.css')}" />
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
<span class="menuButton"><g:link class="show" action="generation"
	event="return">Back to Document Generation</g:link></span></div>
<div class="body">
<h1>
${batchCreationConfigInstance?.instrument?.study?.name} ${batchCreationConfigInstance?.instrument?.name}
</h1>
<h3>
${batchCreationConfigInstance?.direction?.name.capitalize()} ${batchCreationConfigInstance?.isInitial?.name.capitalize()}
${batchCreationConfigInstance?.format?.name.capitalize()} <br />
${batchCreationConfigInstance?.name}
</h3>
<g:if test="${flash.message}">
	<div id="message" class="message">
	${flash.message}
	</div>
</g:if>
<div class="prop">

	<g:hiddenField name="findUrl" id="findUrl" value="${createLink(controller:'documentGeneration', action:'findItem', params:[:])}"/>

	<g:if test="${batchCreationConfigInstance.useParentItem}">
		<span class="name">
			<label for="sourceValue">Parent Item ID</label>
			<g:hiddenField name="useParentItem" id="useParentItem" value="${true}" />
			<g:hiddenField name="batchCreationConfig.id" id="batchCreationConfig.id" value="${batchCreationConfigInstance.id}"/>
			<g:hiddenField name="batchCreationQueueSource.id" id="batchCreationQueueSource.id" value="${0}"/>			
		</span>
	</g:if>
	
	<g:else>
		<span class="name">
			<label for="batchCreationQueueSource.id">
				<g:message code="BatchCreationQueueSource.label" default="Source:" />
				<g:hiddenField name="useParentItem" id="useParentItem" value="${false}" />
			</label>
		</span> 
		<span class="value">
		
			<g:select name="batchCreationQueueSource.id"
				id="batchCreationQueueSource.id"
				from="${edu.umn.ncs.BatchCreationQueueSource.list()}" optionKey="id"
				value="${edu.umn.ncs.BatchCreationQueueSource.findByName('dwellingUnit').id}" />
		</span>
		<span class="name">
			<label for="sourceValue">ID</label>
		</span>
	</g:else>
	
	
	<span class="value">
		<g:textField name="sourceValue" id="sourceValue" size="15" value="" />
	</span>
</div>

<fieldset class="maroonBorder">
<legend id="itemsLegend" style="margin-left: 0.5em;">Entered Items</legend>

	<div class="prop-q ubb">
		<span class="value-q s">ID</span> 
		<span class="value-q s">Source</span> 
		<span class="value-q l">Status</span>
	</div>

	<div id="manualGenerationQueue">
		<p id="deleteMe" class="highlightYellow">Enter at least one of the above items</p>
	</div>
</fieldset>

<g:form method="post" controller="documentGeneration" action="generation">

	<g:hiddenField name="batchCreationConfigInstance.id" value="${batchCreationConfigId}" />

	<div class="prop">
		<span class="name"> 
			<label for="reason">
				<g:message code="batchCreationConfig.defaultReason.label" default="Reason" />
			</label> 
		</span> 
		
		<span class="value"> 
			<g:textField name="reason" size="40" value="${batchCreationConfigInstance?.defaultReason}" /> 
		</span>
	</div>

	<div class="prop"><span class="name"> <label
		for="instructions"><g:message
		code="batchCreationConfig.defaultInstructions.label"
		default="Instructions" /></label> </span> <span class="value"> <g:textField
		name="instructions" size="40"
		value="${batchCreationConfigInstance?.defaultInstructions}" /> </span></div>

	<div class="prop"><span class="name"> <label
		for="comments"><g:message
		code="batchCreationConfig.defaultComments.label" default="Comments" /></label>
	</span> <span class="value"> <g:textField name="comments" size="40"
		value="${batchCreationConfigInstance?.defaultComments}" /> </span></div>

	<div class="buttons"><span class="button"><g:submitButton
		name="generateDocuments" value="Generate Document(s)" /></span></div>
</g:form>
</div>
</body>
</html>
