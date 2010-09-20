<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Auto Document Generation - National Children's Study</title>
    <meta name="layout" content="ncs" />
     <g:set var="entityName" value="${message(code: 'documentGeneration.label', default: 'DocumentGeneration')}" />
  </head>
  <body>
    <div class="nav">
          <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
          <span class="menuButton"><g:link class="show" action="generation" event="return">Back to Document Generation</g:link></span>
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
        <br/>${batchCreationConfigInstance?.name}
      </h3>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>

      <g:form method="post" controller="documentGeneration" action="generation">
          <g:hiddenField name="batchCreationConfigInstance.id" value="${batchCreationConfigId}" />
          <div class="prop">
            <span class="name">
              <label for="sourceId"><g:message code="BatchCreationQueueSource.label" default="Source:" /></label>
            </span>
            <span class="value">
              <g:select name="sourceId" from="${edu.umn.ncs.BatchCreationQueueSource.list()}" optionKey="id" value="" />
            </span>

            <span class="name">
              <label for="sourceValue">ID</label>
            </span>
            <span class="value">
              <g:textField name="sourceValue" value="" />
            </span>
          </div>

          <div class="buttons">
            <span class="button"><g:submitToRemote class="save" value="Search" url="[controller:'documentGeneration',action:'find']" update="[success:'manualGenerationQueue']" /></span>
          </div>

        <fieldset class="maroonBorder">
          <legend style="margin-left: 0.5em;">Entered items</legend>
              <div id="manualGenerationQueue">
                    <p class="highlightYellow">Enter at least one of the above items</p>
              </div>
        </fieldset>
        <div class="buttons">
          <span class="button"><g:submitButton name="generateDocuments" value="Generate Document(s)" /></span>
        </div>
      </g:form>
    </div>
  </body>
</html>
