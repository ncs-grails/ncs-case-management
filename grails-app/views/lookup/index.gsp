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
      <span class="menuButton"><g:link class="show" url="/">Back to Home</g:link></span>
    </div>
    <div class="body">

      <h1>NCS Lookup</h1>

      <h2>Please enter an ID or an Address</h2>
	  
      <fieldset class="maroonBorder">
        <legend class="m1">Lookup Search</legend>
		  <g:remoteField action="find" update="resultsDiv"  name="searchString" />
      </fieldset>

      <fieldset class="maroonBorder">
        <legend class="m1">Results</legend>
		<div id="resultsDiv"></div>
      </fieldset>

    </div>
  </body>
</html>
