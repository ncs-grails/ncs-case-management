<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery" />

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="layout" content="ncs" />
	<g:set var="entityName" value="${message(code: 'giftCard.label', default: 'Scan Gift Cards')}" />
	<title>Checkout Incentive</title>
	<g:javascript src="incentive.js" />
	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'incentive.css')}" />
	<script type="text/javascript">
		$(document).ready(function() {
			$('#checkoutBarcode').focus();
		});
	</script>
</head>
<body>
	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
    	<span class="menuButton"><g:link class="list" action="list">Incentive List</g:link></span>
        <span class="menuButton"><g:link class="create" action="assignIncentive">Assign to Item</g:link></span>
        <span class="menuButton"><g:link class="list" action="checkin">Check In</g:link></span>
    	<span class="menuButton"><g:link class="create" action="batchCreate">Scan a Batch</g:link></span>
    	<span class="menuButton"><a href="https://wiki.umn.edu/NcsInternal/IncentiveTrackingUserGuide" target="_blank" class="infoButton" title="help" >Help</a></span>
	</div>
	<div class="body container_12">
		<h1>Check Out Incentive</h1>

		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if> 
		<g:form action="receipt">
			<div class="prop">
				<fieldset class="gift-card-box maroonBorder">
					<legend>Incentive Details</legend>
					<p>If you have already added the incentives to the inventory, you <strong>do not have to</strong>
					choose any of the "Incentive Details" options.  You only need to choose an "Interviewer" and then
					scan in the barcodes on the incentives.</p>
		
					<div class="gift-card-row"><label for="receiptNumber">Receipt Number</label> <g:textField name="receiptNumber" value="" size="40" /></div>
					<div class="gift-card-row"><label for="incentiveType">Incentive Type</label> <g:select id="incentiveTypeInstance" name="type.id" from="${edu.umn.ncs.IncentiveType.list()}" optionKey="id" optionValue="name" value="${incentiveInstance?.type?.id}" noSelection="['null': '-- Please choose --']" /><em style="color:red;"> *</em></div>
					<div class="gift-card-row"><label for="amount">Value</label> <g:textField name="amount" value="" size="5" /></div>
					<div class="gift-card-row"><I>Details for incentives that have not been pre-scanned (if necessary).</I></div>
					<div class="gift-card-row"><em style="color:red;">* Required</em></div>
				</fieldset>
			</div>
		</g:form> 
		<g:form action="checkoutIncentive" name="giftCardForm">
			<g:hiddenField name="receiptNumberInstance" value="${receiptNumber}" />
			<g:hiddenField name="incentiveTypeInstance" value="${incentiveType}" />
			<g:hiddenField name="amountInstance" value="${amount}" />
			<g:hiddenField name="checkedOutToInstance" value="${checkedOutTo}" />
		</g:form>
		
		<div class="prop">
			<span class="name"> <label for="checkedOutTo">Interviewer</label></span>
			<span class="value">
				<g:select id="checkedOutTo" 
					name="checkedOutTo" 
					from="${memberInstanceList}" 
					value="${username}"
		        	optionKey="username"
		        	optionValue="displayName"
		        	noSelection="['':'--Please choose--']" /><em style="color:red;"> *</em>
			</span><br /><br />
			<span class="name"> <label for="item"><g:message code="giftCard.code.label" default="Incentive Barcode" /></label> </span>
			<span class="value"> <input type="text" size="40" id="checkoutBarcode" name="checkoutBarcode" value="" /> </span>
		</div>
		
		<h3 class="scanned-header">Checked Out Incentives</h3>
		<div id="resultLog" class="grid_12">
		</div>
	</div>
</body>
</html>
