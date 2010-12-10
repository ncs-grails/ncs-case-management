<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
  <g:set var="entityName" value="${message(code: 'batchCreationConfig.label', default: 'Mailings')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    <span class="menuButton"><g:link class="list" event="return" action="generation">Choose a Different Mailing Type</g:link></span>
  </div>
  <div class="body">
    <h1>
${batchCreationConfigInstance?.instrument?.study?.name}
${batchCreationConfigInstance?.instrument?.name}
    </h1>
    <h3>
${batchCreationConfigInstance?.direction?.name.capitalize()}
${batchCreationConfigInstance?.isInitial?.name.capitalize()}
${batchCreationConfigInstance?.format?.name.capitalize()}
      <br/>
${batchCreationConfigInstance?.name}
    </h3>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

    <fieldset class="maroonBorder">
      <legend style="margin-left: 0.5em;">Recently Generated Batches</legend>

	  <div class="message">
              Nothing to generate for ${batchCreationConfigInstance.instrument}.
	  </div>

	</fieldset>
  </div>
</body>
</html>
