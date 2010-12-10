
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Auto Document Generation - National Children's Study</title>
    <meta name="layout" content="ncs" />
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
      <span class="menuButton"><g:link class="show" action="generation" event="return">Back to Document Generation</g:link></span>
    </div>
    <div class="body">

      <h1>Batch Details</h1>

      <h2>${batchCreationConfigInstance?.name}</h2>

      <fieldset class="maroonBorder">
        <legend class="m1">Data Sources To Save...</legend>
        <g:each var="ds" in="${batchCreationConfigInstance?.documents}">
          <g:if test="${ds.mergeSourceFile != null}">
            <p>Right-Click and choose "Save Link As..."</p>
            <g:link action="downloadDataset" class="csv" params="${ [ 'batch.id':batchInstance?.id, 'batchCreationDocument.id': ds?.id ] }" >
              ${ds?.mergeSourceFile}
            </g:link>
          </g:if>
        </g:each>
      </fieldset>


      <fieldset class="maroonBorder">
        <legend style="margin-left: 0.5em;">Documents to open...</legend>
        <ul>
          <g:each var="doc" in="${batchCreationConfigInstance?.documents}">
            <li><a href="#">${doc.documentLocation}</a></li>
          </g:each>
        </ul>
      </fieldset>


      <div class="buttons">
        <span class="button"><g:link controller="documentGeneration" action="batchReport" id="${batchInstance?.id}">Open the batch report...</g:link></span>
      </div>

      <div>
        <span class="button"><g:link controller="documentGeneration" action="batchAnalysis" id="${batchInstance?.id}">Batch Analysis Report</g:link></span>
      </div>

    </div>
  </body>
</html>
