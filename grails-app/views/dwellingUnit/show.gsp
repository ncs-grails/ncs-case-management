<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Auto Document Generation - National Children's Study</title>
<meta name="layout" content="ncs" />
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
<span class="menuButton"><g:link class="show" url="/">Back to Home</g:link></span>
</div>
<div class="body">

<h1>Dwelling Unit Details</h1>

<fieldset class="maroonBorder"><legend class="m1">Dwelling
Unit</legend>
<p>Dwelling Unit ID: ${dwellingUnitInstance.id}
</p>
<p>
${dwellingUnitInstance.address.address}<br />
${dwellingUnitInstance.address.cityStateZip}<br />
${dwellingUnitInstance.address.country?.name}
</p>
</fieldset>

<fieldset class="maroonBorder"><legend class="m1">Norc
Info</legend> <g:if test="${dwellingUnitLinkInstance}">
		  NORC SU ID: ${dwellingUnitLinkInstance.norcSuId}
</g:if></fieldset>

<fieldset class="maroonBorder"><legend class="m1">Items
Generated</legend> <g:if test="${ ! trackedItemInstanceList}">
		  No Items found
		</g:if> <g:each var="i" in="${trackedItemInstanceList}">
	<p>Item: ${i.id}, this needs more stuff...</p>
</g:each></fieldset>
</div>
</body>
</html>
