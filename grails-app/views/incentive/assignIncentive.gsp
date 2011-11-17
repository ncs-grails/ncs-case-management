
<%@ page import="edu.umn.ncs.AppointmentIncentive" %>
<g:setProvider library="jquery" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'incentive.label', default: 'Incentive')}" />
        <title>Assign Incentive</title>
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
            <span class="menuButton"><g:link class="create" action="checkout">Checkout</g:link></span>
            <span class="menuButton"><g:link class="create" action="checkin">Check In</g:link></span>
	    	<span class="menuButton"><g:link class="create" action="batchCreate">Scan a Batch</g:link></span>
        </div>
        <div class="body">
            <h1>Assign Incentive to Item</h1>
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
				
	            <g:form name="incentiveForm" action="assignIncentiveToItem">
				          
		            <div class="dialog">
						<div class="scan-box">
							<div class="scan-row">
		                        <label for="type"><g:message code="incentive.type.label" default="Scan Incentive Barcode" /></label>
							</div>
		                       <div>
		                        <g:textField name="code" value="${result?.incentiveBarcode}" />
		                       </div>
						</div>
						<div class="arrow-box">
							<img id="arrow-img" src="${resource(dir:'images',file:'go-next-grey_100x100.png')}" alt="Arrow" />
						</div>
						<div class="scan-box">
							<div class="scan-row">
			                    <label for="person"><g:message code="person.id.label" default="Scan Item Barcode" /></label>
							</div>
		                       <div>
			                    <g:textField name="trackedItemId" value="${result?.trackedItemId}" />					
		                       </div>
						</div>
		
		            </div>
	            	
	                <%--<div class="buttons">
	                    <span class="button">
					    	<g:submitToRemote value="Next"
					    		url="[controller:'incentive', action:'assignIncentiveToItem']"
					    		update="formContainer"
					    		onLoading="showSearching()"
					    		onComplete="hideSearching()"
					    		onSuccess=""
					    		onFailure="showFailure()" />
	                    </span>
	                </div> --%>
	            </g:form>
	            
            </div>
                      
        </div>
    </body>
</html>
