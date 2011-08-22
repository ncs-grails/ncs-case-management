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

		<h1>Dwelling Unit Details</h1>

		<fieldset class="maroonBorder"><legend class="m1">Dwelling
		Unit: ${dwellingUnitInstance.id}</legend>
		<p>
		${dwellingUnitInstance.address.address}<br />
		${dwellingUnitInstance.address.cityStateZip}<br />
		${dwellingUnitInstance.address.country?.name}
		</p>
		</fieldset>
		
		<fieldset class="maroonBorder">
		<legend class="m1">Norc Info</legend>
		<g:if test="${dwellingUnitLinkInstance}">
				  NORC SU ID: ${dwellingUnitLinkInstance.norcSuId}
		</g:if></fieldset>

		<fieldset class="maroonBorder">
		<legend class="m1">Household(s)</legend>
		<g:if test="${householdInstanceList}">
			<g:each var="hh" in="${householdInstanceList}">
				<fieldset class="maroonBorder">
				<legend class="m1">
					Household ID: ${hh.id} 
				</legend>
				<h3>Members</h3>
				<ul>
				<g:each var="p" in="${hh.people}">
					<li>Person ID: ${p.id}, <g:link controller="person" action="show" id="${p.id}">${p.fullName}</g:link></li>
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
				<th>Date On</th>
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
				<td><g:formatDate format="MM/dd/yyyy" date="${i.batch.instrumentDate}" /></td>
				<td><g:formatDate format="MM/dd/yyyy" date="${i.batch.mailDate}" /></td>
				<td>${i.batch.format}</td>
				<td>${i.result?.result?.name}</td>
				<td>${i.result?.receivedDate}</td>
			</tr>
			<g:each var="al" in="${resultHistoryList.findAll{it.trackedItem.id == i.id} }">
				<tr style="background-color: #DDD; font-style: italic;">
					<td></td>
					<td></td>
					<td></td>
					<td colspan="3">Old Result, transacted out by:</td>
					<td>${al.username}</td>
					<td>${al.oldResult.name}</td>
					<td>on <g:formatDate date="${al.dateCreated}" format="MM/dd/yyyy"/></td>
				</tr>
				
			</g:each>
		</g:each>
		</tbody>
	</table>

</fieldset>
</div>
</body>
</html>
