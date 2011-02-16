

<%@ page import="edu.umn.ncs.BatchCreationDocument"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'batchCreationDocument.label', default: 'BatchCreationDocument')}" />
<title><g:message code="default.create.label"
	args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
<span class="menuButton"><g:link class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.create.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">
	${flash.message}
	</div>
</g:if> <g:hasErrors bean="${batchCreationDocumentInstance}">
	<div class="errors"><g:renderErrors
		bean="${batchCreationDocumentInstance}" as="list" /></div>
</g:hasErrors> <g:form action="save" method="post">
	<div class="dialog">
	<table>
		<tbody>

			<tr class="prop">
				<td valign="top" class="name"><label for="documentLocation"><g:message
					code="batchCreationDocument.documentLocation.label"
					default="Document Location" /></label></td>
				<td valign="top"
					class="value ${hasErrors(bean: batchCreationDocumentInstance, field: 'documentLocation', 'errors')}">
				<g:textArea name="documentLocation" cols="40" rows="5"
					value="${batchCreationDocumentInstance?.documentLocation}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="mergeSourceQuery"><g:message
					code="batchCreationDocument.mergeSourceQuery.label"
					default="Merge Source Query" /></label></td>
				<td valign="top"
					class="value ${hasErrors(bean: batchCreationDocumentInstance, field: 'mergeSourceQuery', 'errors')}">
				<g:textArea name="mergeSourceQuery" cols="40" rows="5"
					value="${batchCreationDocumentInstance?.mergeSourceQuery}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="mergeSourceFile"><g:message
					code="batchCreationDocument.mergeSourceFile.label"
					default="Merge Source File" /></label></td>
				<td valign="top"
					class="value ${hasErrors(bean: batchCreationDocumentInstance, field: 'mergeSourceFile', 'errors')}">
				<g:textArea name="mergeSourceFile" cols="40" rows="5"
					value="${batchCreationDocumentInstance?.mergeSourceFile}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="batchCreationConfig"><g:message
					code="batchCreationDocument.batchCreationConfig.label"
					default="Batch Creation Config" /></label></td>
				<td valign="top"
					class="value ${hasErrors(bean: batchCreationDocumentInstance, field: 'batchCreationConfig', 'errors')}">
				<g:select name="batchCreationConfig.id"
					from="${edu.umn.ncs.BatchCreationConfig.list()}" optionKey="id"
					value="${batchCreationDocumentInstance?.batchCreationConfig?.id}" />
				</td>
			</tr>

		</tbody>
	</table>
	</div>
	<div class="buttons"><span class="button"><g:submitButton
		name="create" class="save"
		value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
	</div>
</g:form></div>
</body>
</html>
