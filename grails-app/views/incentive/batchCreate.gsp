
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="edu.umn.ncs.IncentiveType" %>
<g:setProvider library="jquery" />

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'giftCard.label', default: 'Scan Gift Cards')}" />
	<title>Scan Incentives</title>
	<g:javascript src="incentive.js" />
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
    	<span class="menuButton"><g:link class="list" action="list">Incentive List</g:link></span>
    	<span class="menuButton"><g:link class="list" action="checkout">Checkout</g:link></span>
        <span class="menuButton"><g:link class="list" action="checkin">Check In</g:link></span>
	</div>
	<div class="body container_12">
		<h1>Scan Incentives</h1>
		
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<g:form action="receipt">
			<div class="prop">
				<fieldset class="gift-card-box maroonBorder">
					<legend>Incentive Details</legend>
					<div class="gift-card-row"><label for="receiptNumber">Receipt Number</label> <g:textField name="receiptNumber" value="" size="40" /></div>
					<div class="gift-card-row"><label for="incentiveType">Incentive Type</label> <g:select id="incentiveTypeInstance" name="type.id" from="${IncentiveType.list()}" optionKey="id" optionValue="name" value="${incentiveInstance?.type?.id}" noSelection="['null': '-- Please choose --']" /><em style="color:red;"> *</em></div>
					<div class="gift-card-row"><label for="amount">Value</label> <g:textField name="amount" value="" size="5" />   <em>(must be a number)</em></div>
					<div class="gift-card-row"><em style="color:red;">* Required</em></div>
				</fieldset>
			</div>
		</g:form>
		<g:form action="saveIncentiveBatch" name="incentiveForm" method="post" >
			<g:hiddenField name="receiptNumberInstance" value="${receiptNumber}" />
			<g:hiddenField name="incentiveTypeInstance" value="${incentiveType}" />
			<g:hiddenField name="amountInstance" value="${amount}" />
		</g:form>
		
		<div class="prop">
			<span class="name"> <label for="item"><g:message code="incentive.barcode.label" default="Incentive Barcode" /></label> </span>
			<span class="value"> <input type="text" size="40" id="barcode" name="barcode" value="" /> </span>
		</div>
		
		<h3 class="scanned-header">Scanned Incentives</h3>
		<div id="resultLog" class="grid_12">
		</div>
	</div>
</body>
</html>