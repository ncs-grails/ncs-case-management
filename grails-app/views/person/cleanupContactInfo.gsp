<%@ page contentType="text/html;charset=UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<title>Lookup - National Children's Study</title>
		<script>
			function showSpinner() {
				document.getElementById('spinner').style.display = 'block';
				return true;
			}
	
			function hideSpinner() {
				document.getElementById('spinner').style.display = 'none';
				return true;
			}
		</script>
	</head>
	<body>
		<div class="nav">
			<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
			<span class="menuButton"><g:link class="lookup" controller="lookup">Lookup</g:link></span>
		</div>
		<div id="pageBody" class="body">
		
			<h1>Contact Info Cleanup</h1>
			
			<g:form name="findContactInfoForm" >
				<span>Find people with multiple <g:select name="type" from="${type}" optionKey="id" optionValue="value" /></span> <g:submitToRemote action="findContactInfo" value="Find" update="contactInfo" before="showSpinner()" after="hideSpinner()" />
			</g:form>
			
			<div id="contactInfo">
			
			</div>
		</div>
	</body>
</html>
