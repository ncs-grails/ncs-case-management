<g:setProvider library="jquery" />
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
    		</div>
    	</g:form>
    </div>	
	<div class="info-header">Showing ${result?.incentiveInstanceTotal} incentive(s)
		<g:if test="${result?.filterId == 5}">
			<g:link class="print" action="printableIncentiveList" id="${result?.interviewer}" target="_blank">Printable List</g:link>
   		</g:if>
    </div>
	
    <table>
        <thead>
            <tr>
            
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
        <g:each in="${result?.incentiveInstanceList}" status="i" var="incentiveInstance">
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
    <g:paginate total="${result?.incentiveInstanceTotal}" />
</div>

