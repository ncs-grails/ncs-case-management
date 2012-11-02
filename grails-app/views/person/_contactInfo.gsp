
    <%-- STREET ADDRESSES --%>
    <g:if test="${type == 'address'}">
        <g:each var="pa" in="${personInstance.streetAddresses.findAll{ it.active }?.sort{ it.preferredOrder} }" status="i">
            <h2><span class="preferredOrder">#${i + 1} - </span>${pa.streetAddress.address}</h2>
            <p> ${pa.streetAddress.cityStateZip}<br />
                ${pa.streetAddress.country?.name} </p>
        </g:each>
    </g:if>
    <g:if test="${type == 'phone'}">
    </g:if>
    <g:if test="${type == 'email'}">
    </g:if>
