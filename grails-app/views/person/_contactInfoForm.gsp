<g:javascript src="contact-info.js" />

<div id="${'contactInfoBox' + personInstance.id}" class="contactInfoBox">
	<g:form name="${'contactInfoForm_' + personInstance.id}" >
		<g:hiddenField name="type" value="${type}" />
		<div><strong>${personInstance}</strong></div>
		<table>
			<thead>						
				<th>Active</th>
				<th>Type</th>
				<th></th>
				<th>${type}</th>
				<th>End Date</th>
			</thead>
			<tbody>
				<%-- STREET ADDRESSES --%>
				<g:if test="${type == 'address'}">
					<g:each in="${personInstance.streetAddresses.sort { preferredOrder } }" var="contactInfoInstance" status="count" >
						<tr>
							<td>
								<g:hiddenField name="${'id_' +  contactInfoInstance.id}" value="${contactInfoInstance.id}" />
								<g:checkBox name="${'active_' + contactInfoInstance.id}" checked="${contactInfoInstance.active}" />
							</td>
							<td>${contactInfoInstance.addressType}</td>
							<td><g:if test="${contactInfoInstance.preferredOrder == 0}">*</g:if></td>
							<td>
								<div>${contactInfoInstance.streetAddress.address}</div>
								${ contactInfoInstance.streetAddress.address2 ? '<div>' + contactInfoInstance.streetAddress.address2 + '</div> ': '' }
								<div>${contactInfoInstance.streetAddress.cityStateZip}</div>
							</td>
							<td>
								<input size="12" id="${'datepicker_' + contactInfoInstance.id}" name="${'endDate_' + contactInfoInstance.id}" type="text" value="<g:formatDate date='${contactInfoInstance.endDate ?: new Date()}' format='MM/dd/yyyy' />" />
							</td>
						</tr>
					</g:each>
				</g:if>
				<g:if test="${type == 'phone'}">
					<g:each in="${personInstance.phoneNumbers.sort { preferredOrder } }" var="contactInfoInstance" status="count" >
						<tr>
							<td>
								<g:checkBox name="${'active_' + contactInfoInstance.id}" checked="${contactInfoInstance.active}" />
							</td>
							<td>${contactInfoInstance.phoneType}</td>
							<td><g:if test="${contactInfoInstance.preferredOrder == 0}">*</g:if></td>
							<td>
								<div>${contactInfoInstance.phoneNumber}</div>
							</td>
							<td>
								<input size="12" id="${'datepicker_' + contactInfoInstance.id}" name="${'endDate_' + contactInfoInstance.id}" type="text" value="<g:formatDate date='${contactInfoInstance.endDate}' format='MM/dd/yyyy' />" />
							</td>
						</tr>
					</g:each>
				</g:if>
				<g:if test="${type == 'email'}">
					<g:each in="${personInstance.emailAddresses.sort { preferredOrder } }" var="contactInfoInstance" status="count" >
						<tr>
							<td>
								<g:checkBox name="${'active_' + contactInfoInstance.id}" checked="${contactInfoInstance.active}" />
							</td>
							<td>${contactInfoInstance.emailType}</td>
							<td><g:if test="${contactInfoInstance.preferredOrder == 0}">*</g:if></td>
							<td>
								<div>${contactInfoInstance.emailAddress}</div>
							</td>
							<td>
								<input size="12" id="${'datepicker_' + contactInfoInstance.id}" name="${'endDate_' + contactInfoInstance.id}" type="text" value="<g:formatDate date='${contactInfoInstance.endDate}' format='MM/dd/yyyy' />" />
							</td>
						</tr>
					</g:each>
				</g:if>
			</tbody>
		</table>
		<g:submitToRemote action="updateContactInfo" value="Save" update="${'contactInfoBox' + personInstance.id}" onSuccess="updateMessage()" />
	</g:form>
</div>
