<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="ncs" />
  <g:set var="entityName" value="${message(code: 'batchCreationConfig.label', default: 'BatchCreationConfig')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <script type="text/javascript">
	$(function() {
		$("#tabs").tabs();
	});
  </script>
</head>
<body>
  <div class="nav">
	<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
	<span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
	<h1><g:message code="default.create.label" args="[entityName]" /></h1>
	<g:if test="${flash.message}">
	  <div class="message">${flash.message}</div>
	</g:if>
	<g:hasErrors bean="${batchCreationConfigInstance}">
	  <div class="errors">
		<g:renderErrors bean="${batchCreationConfigInstance}" as="list" />
	  </div>
	</g:hasErrors>
	<g:form action="save" method="post" >
	  <div class="dialog">
		<g:include action="form" model="${[batchCreationConfigInstance: batchCreationConfigInstance]}" />
	  </div>
	  <div class="buttons">
		<span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
		<span class="button"><g:actionSubmit class="list" action="list" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" /></span>
	  </div>
	</g:form>
  </div>
</body>
</html>
