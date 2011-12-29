<%@ page contentType="text/html;charset=UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Data Entry - National Children's Study</title>
		<meta name="layout" content="ncs" />
		<script type="text/javascript">
			$(document).ready(function() {
				$('input[name="id"]').focus();
			});
		</script>
	</head>
	<body>
		<div class="nav">
			<span class="menuButton">
				<a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a>
			</span>
		</div>
		<div class="body">

			<h1>NCS Data Entry</h1>

			<h2>Please enter a Tracked Item ID</h2>
			<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
			</g:if>
			<fieldset class="maroonBorder">
				<legend class="m1">Tracked Item ID</legend>
				<g:remoteField controller="dataEntry" action="search" update="resultsDiv" name="id" />
			</fieldset>

			<fieldset class="maroonBorder"><legend class="m1">Search Result</legend>
				<div id="resultsDiv"><g:include controller="dataEntry" action="search" id="${trackedItemInstance?.id}" /></div>
			</fieldset>

		</div>
	</body>
</html>

