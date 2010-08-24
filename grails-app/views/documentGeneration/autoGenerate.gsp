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
    <h1>Auto Document Generation</h1>

    <h2>${batchCreationConfigInstance?.name}</h2>

    <p>Query:
      <!-- margin: top right bottom left -->
      <pre style="margin: 0.5em 0em 0.5em 2em;">${batchCreationConfigInstance?.selectionQuery}</pre>
    </p>


    <p>The end.</p>
  </body>
</html>
