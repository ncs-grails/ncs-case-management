
<div class="errors">
	<label id="errorLabel">${result?.errorText}</label>
</div>
<div>
	<g:if test="${result?.incentiveId}">
			<g:link controller="incentive" action="edit" id="${result?.incentiveId}">Edit Incentive</g:link>	
	</g:if>
	<g:else>
		
		<g:form>
			<div class="dialog">
				<div class="scan-box">
					<div class="scan-row">
						<label for="type"><g:message code="incentive.type.label" default="Scan Incentive Barcode" /></label>
					</div>
					<div>
						<g:textField name="code" value="" />
					</div>
				</div>
				<div class="arrow-box">
					<img id="arrow-img" src="${resource(dir:'images',file:'go-next-grey_100x100.png')}" alt="Arrow" />
				</div>
				<div class="scan-box">
					<div class="scan-row">
						<label for="trackedItem"><g:message code="trackedItem.id.label" default="Scan Item Barcode" /></label>
					</div>
					<div>
						<g:textField name="trackedItemId" value="" />					
					</div>
				</div>
			
			</div>
		         	
			<div class="buttons">
				<span class="button">
			    	<g:submitToRemote value="Assign"
			    		url="[controller:'incentive', action:'assignIncentiveToItem']"
			    		update="formContainer"
			    		onLoading="showSearching()"
			    		onComplete="hideSearching()"
			    		onSuccess=""
			    		onFailure="showFailure()" />
				</span>
			</div>
		</g:form>
	</g:else>
</div>
			


			