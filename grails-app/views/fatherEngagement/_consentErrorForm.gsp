<div id="notFound" class="errors">
	<label id="errorLabel">${result?.errorText}</label>
</div>
<g:if test="${result?.person}">
	<div>
		<g:link controller="fatherEngagement" action="edit" id="${result?.person?.id}">Edit Form</g:link>	
	</div>
</g:if>
<g:else>
	<g:form>	
		<div class="prop">
		    <span class="name">
		        <label for="trackedItemId">Tracked Item ID</label>
		    </span>
		    <span class="value">
		    	<g:textField name="trackedItemId" value="" />
		    	<g:submitToRemote value="Find"
		    		url="[controller:'fatherEngagement', action:'updatePersonInfo']"
		    		update="formContainer"
		    		onLoading="showSearching()"
		    		onComplete="hideSearching()"
		    		onSuccess=""
		    		onFailure="showFailure()" />
		    </span>		
		</div>
	</g:form>
</g:else>
			


			