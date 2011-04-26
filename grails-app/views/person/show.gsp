<%@ page contentType="text/html;charset=UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Auto Document Generation - National Children's Study</title>
		<meta name="layout" content="ncs" />
	</head>
	<body>
		<div class="nav">
			<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
			<span class="menuButton"><g:link class="lookup" controller="lookup">Lookup</g:link></span>
		</div>
		<div class="body">
		
		<h1>Person Details</h1>
		
		<fieldset class="maroonBorder"><legend class="m1">Person: ${personInstance.id}</legend>
			<p>
			${personInstance.title}
			${personInstance.firstName}
			${personInstance.middleName}
			${personInstance.lastName}
			${personInstance.suffix}
			</p>
		</fieldset>

		<g:each var="pa" in="${personInstance.streetAddresses}">
			<fieldset class="maroonBorder"><legend class="m1">Addresses</legend>
				<h2>Address: ${pa.id}</h2>
				<p>
				${pa.address.address}<br />
				${pa.address.cityStateZip}<br />
				${pa.address.country?.name}
				</p>
			</fieldset>
		</g:each>

		<fieldset class="maroonBorder">
		<legend class="m1">Norc Info</legend>
		<g:if test="${personLinkInstance}">
				  NORC SU ID: ${personLinkInstance.norcSuId}
		</g:if><g:else>None.</g:else>
		</fieldset>
		
		<fieldset class="maroonBorder">
		<legend class="m1">Dwelling Unit(s)</legend>
		<g:if test="${false}">
				  TODO
		</g:if>
		<g:else>None</g:else>
		</fieldset>
		
		<fieldset class="maroonBorder">
			<legend class="m1">Items Generated</legend>
			<g:if test="${ ! trackedItemInstanceList}">
			  No Items found
			</g:if>
			<table>
				<thead>
					<tr>
						<th>Item ID</th>
						<th>Study</th>
						<th>Instrument</th>
						<th>Generated</th>
						<th>Mailed</th>
						<th>Format</th>
						<th>Result</th>
						<th>Received</th>
					</tr>
				</thead>
				<tbody>
					<g:each var="i" in="${trackedItemInstanceList}">
					<tr>
						<td>${i.id}</td>
						<td>${i.batch.primaryInstrument.study}</td>
						<td>${i.batch.primaryInstrument}</td>
						<td><g:formatDate format="MM/dd/yyyy" date="${i.batch.dateCreated}" /></td>
						<td><g:formatDate format="MM/dd/yyyy" date="${i.batch.mailDate}" /></td>
						<td>${i.batch.format}</td>
						<td>${i.result?.result?.name}</td>
						<td>${i.result?.receivedDate}</td>
					</tr>
					</g:each>
				</tbody>
			</table>
		
		</fieldset>
		</div>
	</body>
</html>
