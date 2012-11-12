
    <%-- STREET ADDRESSES --%>
    <g:if test="${type == 'address'}">
        <g:each var="pa" in="${personInstance.streetAddresses.findAll{ it.active }?.sort{ it.preferredOrder} }" status="i">
            <h2><span class="preferredOrder">#${i + 1} - </span>${pa.streetAddress.address}</h2>
            <p> ${pa.streetAddress.cityStateZip}<br />
                ${pa.streetAddress.country?.name} </p>
        </g:each>
	</g:if>

    <g:if test="${type == 'phone'}">
		<g:each var="pn" in="${personInstance.phoneNumbers.findAll{ it.active }?.sort{ it.preferredOrder} }" status="i">
			<h2><span class="preferredOrder">#${i + 1} - </span>${pn.phoneType.toString().capitalize()}</h2>
			<p>${pn.phoneNumber}</p>
		</g:each>
    </g:if>

    <g:if test="${type == 'email'}">
    </g:if>
