<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

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

    <h3>Data Sources To Save...</h3>

    <ul>
      <g:each var="doc" in="${batchCreationConfigInstance?.documents}">
        <li><g:link ${doc.mergeSourceFile}</li>
      </g:each>
    </ul>

    <h3>Documents to open...</h3>

	<g:link action="batchReport" id="${batchInstance?.id}">
	  Open the batch report...
	</g:link>
  </div>
  </body>
</html>
