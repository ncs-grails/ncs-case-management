<g:javascript src="contact-info.js" />

<div id="${'contactInfoBox' + type}" class="contactInfoBox">

	<g:if test="${flash.message}">
		<div id="updateMessage" class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.default}"/></div>
	</g:if>

	<g:form name="${'contactInfoForm_' + personInstance.id}" >
	
		<g:hiddenField name="id" value="${personInstance.id}" />
		<g:hiddenField name="type" value="${type}" />
		<g:set var="buttonId" value="${type + 'Button'}" />
		<%--<div><strong>${personInstance}</strong></div>--%>
		<table>
			<thead>						
				<th>Active</th>
				<th>Type</th>
				<th></th>
				<th>${type}</th>
				<th>Date Created</th>
				<th title="Entering an End Date will make this entry inactive">End Date</th>
			</thead>
			<tbody>
				<%-- STREET ADDRESSES --%>
				<g:if test="${type == 'address'}">
					<g:set var="editButtonId" value="editAddressButton" />
					<g:each in="${personInstance.streetAddresses.sort { a,b -> b.dateCreated <=> a.dateCreated ?: a.preferredOrder <=> b.preferredOrder } }" 
						var="contactInfoInstance" 
						status="count" >
						<tr class="${ count.mod(2) == 0 ? 'odd': 'even' }">
							<td>
								<g:hiddenField name="${'id_' +  contactInfoInstance.id}" value="${contactInfoInstance.id}" />
								<g:hiddenField name="${'active_' + contactInfoInstance.id}" value="${contactInfoInstance.active}" />
								<g:formatBoolean boolean="${contactInfoInstance.active}" false="No" true="Yes" />
							</td>
							<td>${contactInfoInstance.addressType}</td>
							<td>
								<g:if test="${contactInfoInstance.active}">
									<g:if test="${contactInfoInstance.preferredOrder == 0}">*
									</g:if>
								</g:if>
							</td>
							<td>
								<div>${contactInfoInstance.streetAddress.address}</div>
								${ contactInfoInstance.streetAddress.address2 ? '<div>' + contactInfoInstance.streetAddress.address2 + '</div> ': '' }
								<div>${contactInfoInstance.streetAddress.cityStateZip}</div>
							</td>
							<td><g:formatDate date="${contactInfoInstance.dateCreated}" format="MM/dd/yyyy" /></td>
							<td>
								<input 
									size="12" 
									id="${'datepicker_' + contactInfoInstance.id}" 
									name="${'endDate_' + contactInfoInstance.id}" 
									type="text" 
									title="Entering an End Date will make this entry inactive" 
									value="<g:formatDate date='${contactInfoInstance.endDate}' format='MM/dd/yyyy' />" />
								</td>
						</tr>
					</g:each>
				</g:if>

				<%-- PHONE NUMBERS --%>
				<g:if test="${type == 'phone'}">
					<g:each in="${personInstance.phoneNumbers.sort { preferredOrder } }" 
						var="contactInfoInstance" 
						status="count" >
						<tr class="${ count.mod(2) == 0 ? 'odd': 'even' }">
							<td>
								<g:hiddenField name="${'id_' +  contactInfoInstance.id}" value="${contactInfoInstance.id}" />
								<g:hiddenField name="${'active_' + contactInfoInstance.id}" value="${contactInfoInstance.active}" />
								<g:formatBoolean boolean="${contactInfoInstance.active}" false="No" true="Yes" />
							</td>
							<td>${contactInfoInstance.phoneType}</td>
							<td><g:if test="${contactInfoInstance.preferredOrder == 0}">*</g:if></td>
							<td><div>${contactInfoInstance.phoneNumber}</div></td>
							<td><g:formatDate date="${contactInfoInstance.dateCreated}" format="MM/dd/yyyy" /></td>
							<td>
								<input 
									size="12" 
									id="${'datepicker_' + contactInfoInstance.id}" 
									name="${'endDate_' + contactInfoInstance.id}" 
									type="text" 
									value="<g:formatDate date='${contactInfoInstance.endDate}' format='MM/dd/yyyy' />" />
							</td>
						</tr>
					</g:each>
				</g:if>

				<%-- EMAIL ADDRESSES --%>
				<g:if test="${type == 'email'}">
					<g:each in="${personInstance.emailAddresses.sort { preferredOrder } }" 
						var="contactInfoInstance" 
						status="count" >
						<tr class="${ count.mod(2) == 0 ? 'odd': 'even' }">
							<td>
								<g:hiddenField name="${'id_' +  contactInfoInstance.id}" value="${contactInfoInstance.id}" />
								<g:hiddenField name="${'active_' + contactInfoInstance.id}" value="${contactInfoInstance.active}" />
								<g:formatBoolean boolean="${contactInfoInstance.active}" false="No" true="Yes" />
							</td>
							<td>${contactInfoInstance.emailType}</td>
							<td><g:if test="${contactInfoInstance.preferredOrder == 0}">*</g:if></td>
							<td><div>${contactInfoInstance.emailAddress}</div></td>
							<td><g:formatDate date="${contactInfoInstance.dateCreated}" format="MM/dd/yyyy" /></td>
							<td>
								<input 
									size="12" 
									id="${'datepicker_' + contactInfoInstance.id}" 
									name="${'endDate_' + contactInfoInstance.id}" 
									type="text" 
									value="<g:formatDate date='${contactInfoInstance.endDate}' format='MM/dd/yyyy' />" />
							</td>
						</tr>
					</g:each>
				</g:if>
			</tbody>
		</table>

		<div>* Preferred</div>

		<span class="buttons">
			<g:submitToRemote 
				url="[ controller:'person', action:'updateContactInfo' ]" 
				class="save" 
				value="Save" 
				update="${'contactInfoBox' + type}" 
				onSuccess="updateMessage()" 
				onComplete="showElementById('${buttonId}')" />
			<g:submitToRemote 
				url="[ controller:'person', action:'cancelContactUpdate' ]" 
				class="cancel" 
				value="Done" 
				update="${'contactInfoBox' + type}" 	
				onComplete="showElementById('${buttonId}')" />
		</span>

    </g:form>

</div>
