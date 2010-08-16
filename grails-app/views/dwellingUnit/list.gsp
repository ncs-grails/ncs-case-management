<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dwelling Units</title>
	<meta name="layout" content="ncs" />
  </head>
  <body>
    <h1>Dwelling Units</h1>
	<hr/>
	<g:each var="du" in="${dwellingUnitList}">
	  <div class="dwellingUnit">
		<p>
		  <span>${du.id}</span>
		  <span class="address">${du.address?.address}</span>
		  <br/>
		  <span class="city">${du.address?.city}</span>
		  <span class="state">${du.address?.state}</span>
		  <span class="zipcode">${du.address?.zipCode}</span>
		</p>
	  </div>
	</g:each>
	<hr/>

	<!-- DISPLAY ERROR MESSAGES -->
	<g:if test="${flash.message}"><div class="message">${flash.message}</div></g:if>
	<g:hasErrors bean="${dwellingUnitInstance}"><div class="errors"><g:renderErrors bean="${dwellingUnitInstance}" as="list" /></div></g:hasErrors>


	<g:form class="ncs" action="quickAdd">

	  <label for="address.address">Address</label>
	  <g:textField name="address.address" value="${dwellingUnitInstance?.address?.address}" />

	  <label for="address.address">City</label>
	  <g:textField name="address.city" value="${dwellingUnitInstance?.address?.city}" />

	  <label for="address.state">State</label>
	  <g:textField name="address.state" value="${dwellingUnitInstance?.address?.state}" />

	  <label for="address.zipCode">Zip code</label>
	  <g:textField name="address.zipCode" value="${dwellingUnitInstance?.address?.zipCode}" />

	  <label for="address.zipCode">Zip code</label>
	  <g:textField name="address.zip4" value="${dwellingUnitInstance?.address?.zip4}" />

	  <g:submitButton name="save" value="Save" />
	</g:form>

	<p><br/><br/></p>
</body>
</html>
