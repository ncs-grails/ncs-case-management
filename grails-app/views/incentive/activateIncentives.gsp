<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery" />

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="layout" content="ncs" />
	<title>Activate Incentives</title>
	<g:javascript src="incentive.js" />
	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'incentive.css')}" />
	<script type="text/javascript">
		$(document).ready(function() {
			$('#receiptNumber').focus();
		});
	</script>
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
    	<span class="menuButton"><g:link class="list" action="list">Incentive List</g:link></span>
    	<span class="menuButton"><g:link class="create" action="checkout">Checkout</g:link></span>
        <span class="menuButton"><g:link class="list" action="checkin">Check In</g:link></span>
    	<span class="menuButton"><g:link class="create" action="batchCreate">Scan a Batch</g:link></span>
    	<span class="menuButton"><a href="https://wiki.umn.edu/NcsInternal/IncentiveTrackingUserGuide" target="_blank" class="infoButton" title="help" >Help</a></span>
	</div>
	<div class="body container_12">
		<h1>Activate Incentives by Receipt Number</h1>
		
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if> 
        <g:hasErrors bean="${incentiveInstance}">
        <div class="errors">
            <g:renderErrors bean="${incentiveInstance}" as="list" />
        </div>
        </g:hasErrors>

		<g:form action="activateByReceiptNumber" name="incentiveForm">
			<g:hiddenField name="receiptNumberInstance" value="${receiptNumber}" />
		</g:form>
		
		<div class="prop">
			<span class="name"> <label>Receipt Number</label> </span>
			<span class="value"> <input type="text" size="40" id="receiptNumber" name="receiptNumber" value="" /> </span>
		</div>

		<h3 class="scanned-header"></h3>
		<div id="resultLog" class="grid_12">
		</div>
		
	</div>
</body>
</html>
