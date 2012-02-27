<%@ page import="edu.umn.ncs.BatchCreationConfig"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="ncs" />
		<g:set var="entityName" value="${message(code: 'batchCreationConfig.label', default: 'Mailings')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
		<g:javascript src="showConfig.js"/>
	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'documentGeneration.css')}" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'showConfig.css')}" />
	</head>

	<body>
		<div class="nav">
			<span class="menuButton">
				<a class="home" href="${createLink(uri: '/')}">
					<g:message code="default.home.label" />
				</a>
			</span>
			<span class="menuButton">
				<g:link class="list" action="generation">Document Generation</g:link>
			</span>
		</div>
		<div class="bundles">
			<h1> Document Generation Bundles </h1>

			<g:each var="bcc" in="${batchCreationConfigInstanceList}">
			<g:if test="${bcc.active}">
			<g:render template="/batchCreationConfig/info" bean="${bcc}" var="batchCreationConfigInstance" />
			</g:if>
			</g:each>
		</div>
	</body>
</html>

