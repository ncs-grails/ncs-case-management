<div class="prop">
	<span class="name">
		<label for="trackedItem.id"><g:message code="eligibilityQuestionnaire.trackedItem.label" /></label>
	</span>
	<span class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'trackedItem', 'errors')}">
		<g:hiddenField name="trackedItem.id" maxlength="12" value="${eligibilityQuestionnaireInstance?.trackedItem?.id}" />
		<g:textField disabled="disabled" name="trackedItemDisabled" maxlength="12" value="${eligibilityQuestionnaireInstance?.trackedItem?.id}" />
	</span>
</div>


<table>
	<thead> <tr>
			<th class="name"><label for="title"><g:message code="eligibilityQuestionnaire.title.label" /></label> </th>
			<th class="name"><label for="firstName"><g:message code="eligibilityQuestionnaire.firstName.label" /></label> </th>
			<th class="name"><label for="middleName"><g:message code="eligibilityQuestionnaire.middleName.label" /></label> </th>
			<th class="name"><label for="lastName"><g:message code="eligibilityQuestionnaire.lastName.label" /></label> </th>
			<th class="name"><label for="suffix"><g:message code="eligibilityQuestionnaire.suffix.label" /></label> </th>
	</tr> </thead>
	<tbody> <tr>
			<td class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'title', 'errors')}">
				<g:textField class="capitalize nodots" name="title" size="6" maxlength="10" value="${eligibilityQuestionnaireInstance?.title}" />
			</td>
			<td class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'firstName', 'errors')}">
				<g:textField class="capitalize" name="firstName" maxlength="30" value="${eligibilityQuestionnaireInstance?.firstName}" />
			</td>
			<td class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'middleName', 'errors')}">
				<g:textField class="capitalize" name="middleName" maxlength="20" value="${eligibilityQuestionnaireInstance?.middleName}" />
			</td>
			<td class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'lastName', 'errors')}">
				<g:textField class="capitalize" name="lastName" maxlength="30" value="${eligibilityQuestionnaireInstance?.lastName}" />
			</td>
			<td class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'suffix', 'errors')}">
				<g:textField class="capitalize" name="suffix" size="6"  maxlength="10" value="${eligibilityQuestionnaireInstance?.suffix}" />
			</td>
	</tr> </tbody>
</table>

<div class="prop">
	<span class="name">
		<label for="gender"><g:message code="eligibilityQuestionnaire.gender.label" /></label>
	</span>
	<span class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'gender', 'errors')}">
		<g:select name="gender.id" from="${edu.umn.ncs.Gender.list()}" optionKey="id" value="${eligibilityQuestionnaireInstance?.gender?.id}" noSelection="['null': '']" />
	</span>
</div>

<g:if test="${eligibilityQuestionnaireInstance?.useExistingStreetAddress}">
<div class="prop">
	<span class="name">
		<label for="useExistingStreetAddress.id"><g:message code="eligibilityQuestionnaire.useExistingStreetAddress.label" /></label>
	</span>
	<span class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'useExistingStreetAddress', 'errors')}">
		<g:checkBox name="useExistingStreetAddress.id" 
		checked="${eligibilityQuestionnaireInstance?.useExistingStreetAddress}" 
		value="${eligibilityQuestionnaireInstance?.useExistingStreetAddress?.id}" />
		${eligibilityQuestionnaireInstance?.useExistingStreetAddress?.address},
		${eligibilityQuestionnaireInstance?.useExistingStreetAddress?.cityStateZip}
	</span>
</div>
</g:if>

<div id="addressFields">
	<table>
		<!-- Address -->
		<thead> <tr>
				<th class="name" colspan="3"><label for="address"><g:message code="eligibilityQuestionnaire.address.label" /></label></th>
		</tr> </thead>
		<tbody> <tr>
				<td colspan="3" class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'address', 'errors')}">
					<g:textField name="address" size="64" maxlength="64" value="${eligibilityQuestionnaireInstance?.address}" />
				</td>
		</tr> </tbody>

		<!-- Address (Line 2) -->
		<%--
		<thead> <tr>
				<th class="name" colspan="3"><label for="address2"><g:message code="eligibilityQuestionnaire.address2.label" default="Address2" /></label></th>
		</tr> </thead>
		<tbody> <tr>
				<td colspan="3" class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'address2', 'errors')}">
					<g:textField disabled="disabled" name="address2" size="64" maxlength="64" value="${eligibilityQuestionnaireInstance?.address2}" />
				</td>
		</tr> </tbody>
		--%>

		<!-- City, State, Postal Code -->

		<thead> <tr>
				<th class="name"><label for="city"><g:message code="eligibilityQuestionnaire.city.label" /></label></th>
				<th class="name"><label for="state"><g:message code="eligibilityQuestionnaire.state.label" /></label></th>
				<th class="usPostalCode name"><label for="zipCode"><g:message code="eligibilityQuestionnaire.zipCode.label" /></label>
					+
					<label for="zip4"><g:message code="eligibilityQuestionnaire.zip4.label" default="Zip4" /></label></th>
				<th class="nonUsPostalCode name"><label for="internationalPostalCode"><g:message code="eligibilityQuestionnaire.internationalPostalCode.label" /></label></th>
		</tr> </thead>
		<tbody> <tr>
				<td class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'city', 'errors')}">
					<g:textField name="city" maxlength="30" value="${eligibilityQuestionnaireInstance?.city}" />
				</td>
				<td class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'state', 'errors')}">
					<g:textField name="state" size="4" maxlength="2" value="${eligibilityQuestionnaireInstance?.state}" />
				</td>
				<td class="usPostalCode value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'zipCode', 'errors')}">
					<g:textField name="zipCode" maxlength="5" size="7" value="${eligibilityQuestionnaireInstance?.zipCode}" />
					-
					<g:textField name="zip4" maxlength="4" size="6" value="${eligibilityQuestionnaireInstance?.zip4}" />
				</td>
				<td class="nonUsPostalCode value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'internationalPostalCode', 'errors')}">
					<g:textField name="internationalPostalCode" maxlength="16" value="${eligibilityQuestionnaireInstance?.internationalPostalCode}" />
				</td>
		</tr> </tbody>
	</table>

	<div class="prop">
		<span class="name">
			<label for="country"><g:message code="eligibilityQuestionnaire.country.label" /></label>
		</span>
		<span class="value ${hasErrors(bean: eligibilityQuestionnaireInstance, field: 'country', 'errors')}">
			<g:select name="country.id" from="${edu.umn.ncs.Country.list()}" optionKey="id" value="${eligibilityQuestionnaireInstance?.country?.id}" noSelection="['null': '']" />
		</span>
	</div>

<div class="prop usPostalCode">
	<span class="name">
		<label for="allowAddressLookup"><g:message code="address.lookup.allowAddressLookup.label" /></label>
	</span>
	<span class="value">
		<input type="checkbox" id="allowAddressLookup" name="allowAddressLookup" checked="checked" />
	</span>
</div>

	<div class="message" id="errorsOut">no address changes detected</div>
</div>

<g:hiddenField name="countryUs" value="${edu.umn.ncs.Country.findByAbbreviation('us')?.id}" />
<g:hiddenField name="standardized" value="${eligibilityQuestionnaireInstance?.standardized}" />
<g:hiddenField name="uspsDeliverable" value="${eligibilityQuestionnaireInstance?.uspsDeliverable}" />
