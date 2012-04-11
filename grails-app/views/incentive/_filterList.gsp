<g:setProvider library="jquery" />
<g:javascript src="incentive.js" />
<div class="list">
	<h1 id="incentive-list-header">${result?.filterName}</h1>
    <div class="filter-buttons">
    <%--BUTTONS TO FILTER LIST - ALL (DEFAULT), DISTRIBUTED, CHECKED IN, CHECKED OUT, AND CHECKED OUT TO --%>
    	<g:form>
    		<div class="filter-buttons-row">
    			<label>View  </label>
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
				    	from='${result?.memberInstanceList}'
				    	optionKey="username"
				    	optionValue="displayName"
				    	value="${result?.interviewer}" />
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
				<%-- <g:if test="${result?.filterId != 1}">
					 <span class="button with-select">
				        <g:textField name="filter" class="searchBox" value="Search..." />
	                 </span>
                 </g:if>--%>		                  
    		</div>
    	</g:form>
    </div>	
    <g:form name="printIncentiveListForm" action="printableIncentiveList">
		<div class="info-header">Showing <span class="total-count">${result?.incentiveInstanceTotal}</span> incentive(s)
			<g:if test="${result?.filterId == 5}">
				<g:if test="${result?.incentiveInstanceTotal > 0}">
					<%-- <g:link class="print" action="printableIncentiveList" id="${result?.interviewer}" target="_blank">Printable List</g:link>--%>
						<g:hiddenField name="user" value="${result?.interviewer}"/>
						<g:hiddenField name="incentivesToPrintOrig" value="${result?.incentiveInstanceList.id}" />
						<g:hiddenField name="incentivesToPrint" value="${result?.incentiveInstanceList.id}" />
						<g:submitButton class="print" value="Printable List" name="printableIncentiveList" onclick="return checkSelectedIncentives();" />
						<span class="selectIncentivesToPrint">Unselect All</span>
						<span class="noSelectedIncentives">No Incentives have been selected to print</span>
		   		</g:if>
	   		</g:if>
	    </div>
    </g:form>
	
    <table id="list-table" class="tablesorter">
        <thead>
            <tr>
				<g:if test="${result?.filterId == 5}">
	                <th>Print</th>
				</g:if>
				
                <th>Id</th>
                
                <th>Type</th>
                
                <th>Value</th>

                <th>Barcode</th>

                <th>Receipt</th>

                <th>Activated</th>	

                <th>Updated</th>

                <th><strong>Status</strong></th>

            </tr>
        </thead>
        <tbody class="zebra">
        <g:each in="${result?.incentiveInstanceList}" status="i" var="incentiveInstance">
            <%-- <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">--%>  
            <tr>                        
            
				<g:if test="${result?.filterId == 5}">
	                <td><g:checkBox name="${'checkBox_' + incentiveInstance?.id}" value="${true}" /></td>
				</g:if>
				
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

