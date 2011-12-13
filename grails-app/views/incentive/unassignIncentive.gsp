
<%@ page import="edu.umn.ncs.AppointmentIncentive" %>
<g:setProvider library="jquery" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'incentive.label', default: 'Incentive')}" />
        <title>Unassign Incentive</title>
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'incentive.css')}" />
		<g:javascript src="incentive.js" />
		<script type="text/javascript">
			$(document).ready(function() {
				$('#code').focus();
			});
		</script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="https://secure.ncs.umn.edu/ncs-case-management"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list" >Incentive List</g:link></span>
            <span class="menuButton"><g:link class="give" action="assignIncentive">Assign to Item</g:link></span>
            <span class="menuButton"><g:link class="create" action="checkout">Checkout</g:link></span>
            <span class="menuButton"><g:link class="create" action="checkin">Check In</g:link></span>
	    	<span class="menuButton"><g:link class="create" action="batchCreate">Scan a Batch</g:link></span>
            <span class="menuButton drop-down"><g:link class="more" action="">More</g:link>
	            <ul class="drop-down-menu">
			        <li><span class="menuButton"><g:link class="create" action="activateIncentives">Activate Incentives</g:link></span></li>
			    	<li><span class="menuButton"><a href="https://wiki.umn.edu/NcsInternal/IncentiveTrackingUserGuide" target="_blank" class="infoButton" title="help" >Help</a></span></li>
				</ul>
			</span>
        </div>
        <div class="body">
            <h1>Unassign Incentive</h1>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${incentiveInstance}">
	            <div class="errors">
	                <g:renderErrors bean="${incentiveInstance}" as="list" />
	            </div>
            </g:hasErrors>
            
			<div id="searching" ><strong>Searching</strong> <img src="../images/spinner.gif" title="spinner" /></div>
            
			<div id="formContainer" >	
				<g:if test="${result?.errorText}">	
            		<div id="data-errors" class="errors">${result?.errorText}</div>
				</g:if>
				
	            <g:form name="incentiveForm" action="unassignIncentiveFromItem">
				          
		            <div class="dialog">
						<div class="scan-box">
							<div class="scan-row">
		                        <label for="type"><g:message code="incentive.type.label" default="Scan Incentive Barcode" /></label>
							</div>
		                       <div>
		                        <g:textField name="incentiveBarcode" value="${result?.incentiveBarcode}" />
		                       </div>
						</div>
		
		            </div>
	            </g:form>
	            
            </div>
                      
        </div>
    </body>
</html>
