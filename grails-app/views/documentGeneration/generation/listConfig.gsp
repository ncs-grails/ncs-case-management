
<%@ page import="edu.umn.ncs.BatchCreationConfig"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'batchCreationConfig.label', default: 'Document Generation')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div class="nav">
		<span class="menuButton">
			<a class="home" href="${createLink(uri: '/')}">
				<g:message code="default.home.label" />
			</a>
		</span>
		<span class="menuButton"><g:link class="list" controller="documentGeneration" action="listBundles">List all Bundles</g:link></span>
	</div>
<div class="body">
<h1><g:message code="default.list.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">
	${flash.message}
	</div>
</g:if>

<g:form action="generation">
	<div id="searchForm"><g:textField name="q" value="${params.q}" />
	<g:submitButton name="search" value="Search" /></div>
</g:form>

<g:if test="${batchCreationConfigInstanceList.size() > 0}">
	<fieldset class="maroonBorder">
          <legend>Bundles matching your search criteria</legend>

	<div class="list">
	<table>
		<thead>
			<tr>
				<g:sortableColumn property="instrument.study.nickName"
					params="${[q: params?.q]}"
					title="${message(code: 'study.nickName.label', default: 'Study')}" />

				<g:sortableColumn property="instrument.name"
					params="${[q: params?.q]}"
					title="${message(code: 'instrument.name.label', default: 'Instrument')}" />

				<g:sortableColumn property="name" params="${[q: params?.q]}"
					title="${message(code: 'batchCreationConfig.name.label', default: 'Name')}" />

			</tr>
		</thead>
		<tbody>
			<g:each in="${batchCreationConfigInstanceList}" status="i"
				var="batchCreationConfigInstance">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

					<td><g:link action="generation" event="load"
						id="${batchCreationConfigInstance.id}">
						${batchCreationConfigInstance?.instrument?.study?.name}
					</g:link></td>

					<td><g:link action="generation" event="load"
						id="${batchCreationConfigInstance.id}">
						${batchCreationConfigInstance?.instrument?.name}
					</g:link></td>

					<td><g:link action="generation" event="load"
						id="${batchCreationConfigInstance.id}">
						${fieldValue(bean: batchCreationConfigInstance, field: "name")}
					</g:link></td>

				</tr>
			</g:each>
		</tbody>
	</table>
	</div>
	</fieldset>
</g:if>
<fieldset class="maroonBorder">
	<legend>Recently Generated Bundles</legend>
	<div class="list">
		<table>
			<thead>
				<tr>
					<g:sortableColumn property="instrument.study.nickName"
					params="${[q: params?.q]}"
					title="${message(code: 'study.nickName.label', default: 'Study')}" />

					<g:sortableColumn property="instrument.name"
					params="${[q: params?.q]}"
					title="${message(code: 'instrument.name.label', default: 'Instrument')}" />

					<g:sortableColumn property="name" params="${[q: params?.q]}"
					title="${message(code: 'batchCreationConfig.name.label', default: 'Name')}" />

				</tr>
			</thead>
			<tbody>
				<g:each in="${batchCreationConfigRecentList}" status="i" var="batchCreationConfigInstance">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

					<td>
						<g:link action="generation" event="load" id="${batchCreationConfigInstance.id}">
						${batchCreationConfigInstance?.instrument?.study?.name}
						</g:link>
					</td>

					<td>
					<g:link action="generation" event="load" id="${batchCreationConfigInstance.id}">
						${batchCreationConfigInstance?.instrument?.name}
						</g:link>
					</td>

					<td>
						<g:link action="generation" event="load" id="${batchCreationConfigInstance.id}">
						${fieldValue(bean: batchCreationConfigInstance, field: "name")}
						</g:link>
					</td>
				</tr>
				</g:each>
			</tbody>
		</table>
	</div>
</fieldset>

</div>
</body>
</html>
