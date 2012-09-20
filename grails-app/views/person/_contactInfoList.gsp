<g:javascript src="contact-info.js" />

<g:if test="${results}">
	<g:hiddenField name="type" value="${type}" />
	
	<div id="infoMessage" class="message"></div>
	<div class="errors"></div>
	
	<strong>Found ${results.size()} people with multiple ${type} records</strong>
	<g:each in="${results}" var="personInstance" status="i">
		<g:render template="contactInfoForm" model="${['personInstance':personInstance, 'type':type]}" />
	</g:each>
</g:if>
<g:else>No people found with multiple ${type} records</g:else>