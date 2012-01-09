<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="ncs" />
		<g:set var="entityName" value="${message(code: 'demog.label')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<g:javascript plugin="address-lookup-zpfour" src="address-lookup-zp4.js" />
		<g:addressLookupOnChange certifiedOut="standardized" />
	</head>
	<body>
		<div class="nav">
			<span class="manuButton">
				<a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.message" /></a>
			</span>
		</div>
		<div class="body">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<h2>Please enter a Person ID</h2>
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			<fieldset class="maroonBorder">
				<legend class="m1">Person ID</legend>
				<g:remoteField controller="demog" action="search" update="resultsDiv" name="id" />
			</fieldset>

			<fieldset class="maroonBorder">
				<legend class="m1">Search Result</legend>
				<div id="resultsDiv">
					<g:include controller="demog" action="search" id="${personInstance?.id}" />
				</div>
			</fieldset>	
		</div>
	</body>
</html>
