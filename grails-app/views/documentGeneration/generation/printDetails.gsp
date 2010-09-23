
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
              <a href="#">${ds.mergeSourceFile}</a>
              <div class="dataitem">
                <div class="datavalue">
                <label for="usePerson">
                  <g:message code="batchCreationDocument.usePerson.label" default="Use Person Datset" />
                </label>
                <span class="p1"><g:checkBox name="usePerson" value="${ds.usePerson}" /></span>
                </div>
              </div>
              <div class="dataitem">
                <div class="datavalue">
                  <label for="useDwelling">
                    <g:message code="batchCreationDocument.useDwelling.label" default="Use Dwelling Unit Datset" />
                  </label>
                  <span class="p1"></span><g:checkBox name="useDwelling" value="${ds.useDwelling}" />
                </div>
              </div>
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
          <span class="button"><g:link action="batchReport" id="${batchInstance?.id}">Open the batch report...</g:link></span>
        </div>
  </div>
  </body>
</html>
