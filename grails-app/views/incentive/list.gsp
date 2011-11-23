
<%@ page import="edu.umn.ncs.Appointment" %>
<%@ page import="edu.umn.ncs.AppointmentIncentive" %>
<%@ page import="edu.umn.ncs.IncentiveType" %>
<g:setProvider library="jquery" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'incentive.label', default: 'GiftCard')}" />
        <title>Incentives</title>
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'incentive.css')}" />
		<g:javascript src="incentive.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="give" action="assignIncentive">Assign to Item</g:link></span>
            <span class="menuButton"><g:link class="create" action="checkout">Checkout</g:link></span>
            <span class="menuButton"><g:link class="create" action="checkin">Check In</g:link></span>
	    	<span class="menuButton"><g:link class="create" action="batchCreate">Scan in new Incentives</g:link></span>
            <span class="menuButton"><g:link class="create" action="activateIncentives">Activate</g:link></span>
	    	<span class="menuButton"><a href="https://wiki.umn.edu/NcsInternal/IncentiveTrackingUserGuide" target="_blank" class="infoButton" title="help" >Help</a></span>
        </div>
        <div class="body">
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
			<div id="searching" ><strong>Searching</strong> <img src="../images/spinner.gif" title="spinner" /></div>
                        
            <div id="formContainer">
	            <div class="list">
		            <h1 id="incentive-list-header">All Incentives</h1>
	
		            <div class="filter-buttons">
		            <%--BUTTONS TO FILTER LIST - ALL (DEFAULT), DISTRIBUTED, CHECKED IN, CHECKED OUT, AND CHECKED OUT TO --%>					
		            	<g:form>
		            		<div class="filter-buttons-row">
		            			<label>View </label>
			                    <span class="button">
							    	<g:submitToRemote value="All"
							    		url="[controller:'incentive', action:'list', id:'1']"
							    		update="formContainer"
							    		onLoading="showSearching()"
							    		onComplete="hideSearching()"
							    		onSuccess=""
							    		onFailure="showFailure()" />
			                   </span>
			                    <span class="button">
			 				    	<g:submitToRemote value="Distributed"
							    		url="[controller:'incentive', action:'list', id:'2']"
							    		update="formContainer"
							    		onLoading="showSearching()"
							    		onComplete="hideSearching()"
							    		onSuccess=""
							    		onFailure="showFailure()" />
			                   </span>
			                   <span class="button">
							    	<g:submitToRemote value="Checked In"
							    		url="[controller:'incentive', action:'list', id:'3']"
							    		update="formContainer"
							    		onLoading="showSearching()"
							    		onComplete="hideSearching()"
							    		onSuccess=""
							    		onFailure="showFailure()" />
			                   </span>
			                   <span class="button">
							    	<g:submitToRemote value="Checked Out"
							    		url="[controller:'incentive', action:'list', id:'4']"
							    		update="formContainer"
							    		onLoading="showSearching()"
							    		onComplete="hideSearching()"
							    		onSuccess=""
							    		onFailure="showFailure()" />
			                    </span>
							</div>
		            		<div>
			                   <span class="button with-select">
							    	<g:submitToRemote value="Checked Out To"
							    		url="[controller:'incentive', action:'list', id:'5']"
							    		update="formContainer"
							    		onLoading="showSearching()"
							    		onComplete="hideSearching()"
							    		onSuccess=""
							    		onFailure="showFailure()" />
							        <g:select name="interviewer"
							        	from='${memberInstanceList}'
							        	optionKey="username"
							        	optionValue="displayName"
							        	value="${interviewer}" />
			                    </span>		                  
								<span class="button with-select">
							    	<g:submitToRemote value="With Receipt #"
							    		url="[controller:'incentive', action:'list', id:'6']"
							    		update="formContainer"
							    		onLoading="showSearching()"
							    		onComplete="hideSearching()"
							    		onSuccess=""
							    		onFailure="showFailure()" />
							        <g:textField name="receiptNumber" value="" />
			                    </span>		                  
		            		</div>
		            	</g:form>
		            </div>
	
	                <table>
	                    <thead>
	                        <tr >
	                        
	                            <g:sortableColumn property="id" title="${message(code: 'incentive.id.label', default: 'Id')}" />
	                            
	                            <g:sortableColumn property="type" title="${message(code: 'incentive.type.label', default: 'Type')}" />
	                            
	                            <g:sortableColumn property="amount" title="${message(code: 'incentive.amount.label', default: 'Value')}" />
	
	                            <g:sortableColumn property="barcode" title="${message(code: 'incentive.barcode.label', default: 'Barcode')}" />
	
	                            <g:sortableColumn property="receiptNumber" title="${message(code: 'incentive.receiptNumber.label', default: 'Receipt #')}" />

	                            <g:sortableColumn property="activated" title="${message(code: 'incentive.activated.label', default: 'Activated')}" />	
	
	                            <g:sortableColumn property="lastUpdated" title="${message(code: 'incentive.lastUpdated.label', default: 'Last Updated')}" />
	
	                            <th><strong>Status</strong></th>
								
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <g:each in="${incentiveInstanceList}" status="i" var="incentiveInstance">
	                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">                        
	                        
	                            <td><g:link action="edit" id="${incentiveInstance?.id}">${incentiveInstance?.id}</g:link></td>
	
	                            <td>${incentiveInstance?.type?.name}</td>
	
	                            <td><g:formatNumber number="${incentiveInstance?.amount}" type="currency" currencyCode="USD" /></td>
	
	                            <td>${incentiveInstance?.barcode}</td>
	
	                            <td>${incentiveInstance?.receiptNumber}</td>

	                            <td><g:formatBoolean boolean="${incentiveInstance?.activated}" true="Yes" false="No" /></td>
	
	                            <td><g:formatDate date="${incentiveInstance?.lastUpdated}" format="MM/dd/yyyy h:mm a" /></td>
	
	                            <td>
	                            	<g:if test="${incentiveInstance?.trackedItem}">
	                            		<strong>Distributed <g:formatDate date="${incentiveInstance?.incentiveDate}" format="MM/dd/yyyy" /></strong>	
	                            	</g:if>
	                            	<g:else>
	                            		<g:if test="${incentiveInstance?.checkedOut}">
	                            			<em style="color:red;">Checked out to ${incentiveInstance?.checkedOutToWhom} on <g:formatDate date="${incentiveInstance?.dateCheckedOut}" format="MM/dd/yyyy" /></em>
	                            		</g:if>
	                            		<g:else>
	                            			<em style="color:#666;">Currently Checked In</em>
	                            		</g:else>
									</g:else>
								</td>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
				</div>
	            <div class="paginateButtons">
	                <g:paginate total="${incentiveInstanceTotal}" />
	            </div>
            </div>
        </div>
    </body>
</html>
