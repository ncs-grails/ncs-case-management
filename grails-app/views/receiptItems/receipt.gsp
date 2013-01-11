<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery" />

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'receipt.label', default: 'Receipt Items')}" />
	<title><g:message code="default.list.label" args="[entityName]" /></title>
	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'receiptItems.css')}" />
	<g:javascript src="receiptItems.js" />
</head>

<body>

	<div class="nav">
		<span class="menuButton">
			<a class="home" href="${createLink(uri: '/')}">
				<g:message code="default.home.label" />
			</a>
		</span>
	</div>

	<div class="body container_12">
	
		<h1>Receipt Items</h1>

		<g:if test="${flash.message}">
			<div class="message">
				${flash.message}
			</div>
		</g:if>
	
		<g:form action="receipt">
			<div class="prop">
				<span class="name">
					<label for="receiptDate">
						Receipt	Date: <g:formatDate format="yyyy-MM-dd" date="${receiptDate}" />
					</label>
				</span>
				<span class="value">
					<g:datePicker name="receiptDate" value="${receiptDate}" precision="minute" />
					<g:submitButton name="submit" value="Change Receipt Date" />
				</span>
			</div>
		</g:form>

		<g:form action="receiptItem" name="receiptForm">
			<g:hiddenField name="receiptDateInstance" value="${receiptDate}" />
		</g:form>

		<div class="prop">
			<span class="name">
				<label for="item">
					<g:message code="receiptItems.item.label" default="Item to receipt" />
				</label>
			</span>
			<span class="value">
				<input type="text" size="15" id="barcode" name="barcode" value="" /> 
			</span>
		</div>

		<fieldset class="maroonBorder">
			<legend>Receipting Status</legend>
			<div class="list">
				<div id="resultLog" class="grid_12"></div>
			</div>
		</fieldset>

	</div>

</body>

</html>
