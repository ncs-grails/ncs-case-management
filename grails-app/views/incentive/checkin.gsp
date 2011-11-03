<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery" />

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'giftCard.label', default: 'Scan Gift Cards')}" />
	<title>Check In Incentives</title>
	<g:javascript src="incentive.js" />
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
    	<span class="menuButton"><g:link class="list" action="list">Incentive List</g:link></span>
    	<span class="menuButton"><g:link class="create" action="batchCreate">Scan a Batch</g:link></span>
    	<span class="menuButton"><g:link class="create" action="checkout">Checkout</g:link></span>
	</div>
	<div class="body container_12">
		<h1>Check In Incentives</h1>
		
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if> 
        <g:hasErrors bean="${incentiveInstance}">
        <div class="errors">
            <g:renderErrors bean="${incentiveInstance}" as="list" />
        </div>
        </g:hasErrors>
		
		<g:form action="checkinIncentive" name="incentiveForm">
			<g:hiddenField name="receiptNumberInstance" value="${receiptNumber}" />
			<g:hiddenField name="incentiveTypeInstance" value="${incentiveType}" />
			<g:hiddenField name="amountInstance" value="${amount}" />
		</g:form>
		
		<div class="prop">
			<span class="name"> <label for="item"><g:message code="incentive.barcode.label" default="Incentive Barcode" /></label> </span>
			<span class="value"> <input type="text" size="40" id="checkinBarcode" name="checkinBarcode" value="" /> </span>
		</div>
		
		<h3 class="scanned-header">Checked In Incentives</h3>
		<div id="resultLog" class="grid_12">
		</div>
	</div>
</body>
</html>