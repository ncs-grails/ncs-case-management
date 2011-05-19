<%@ page contentType="text/html;charset=UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Lookup - National Children's Study</title>
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

		
		<!-- Appointments -->
		<fieldset class="maroonBorder">
			<legend class="m1">Appointments</legend>
			<g:include controller="appointment" action="list" params="${[ person: [id: personInstance.id ] ] }" />
		</fieldset>

		<g:each var="pa" in="${personInstance.streetAddresses}">
			<fieldset class="maroonBorder"><legend class="m1">Addresses</legend>
				<h2>Address: ${pa.id}</h2>
				<p>
				${pa.streetAddress.address}<br />
				${pa.streetAddress.cityStateZip}<br />
				${pa.streetAddress.country?.name}
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
		<legend class="m1">Household(s)</legend>
		<g:if test="${householdInstanceList}">
			<g:each var="hh" in="${householdInstanceList}">
				<fieldset class="maroonBorder">
				<legend class="m1">
					${hh.dwelling.address.address}, 
					<g:link controller="dwellingUnit" action="show" id="${hh.dwelling.id}">
					Dwelling Unit ID: ${hh.dwelling.id}
				</g:link></legend>
				<h3>Members</h3>
				<ul>
				<g:each var="p" in="${hh.people}">
					<li><g:link controller="person" action="show" id="${p.id}">${p.fullName}</g:link></li>
				</g:each>
				</ul>
				</fieldset>
			</g:each>
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
