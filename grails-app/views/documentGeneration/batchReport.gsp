<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Batch Report - National Children's Study</title>
  </head>
  <body>
    <h1>Batch Report</h1>

	<p id="mailingAddress">
	  <span class="name">Ms. Donna DesMarais</span><br/>
	  
	  <span class="address">McNamara Alumni Center, Suite 350</span><br/>

	  <span class="address2">200 Oak St SE</span><br/>

	  <span class="city">Minneapolis</span>, <span class="state">MN</span>
	  <span class="zipCode">55455</span>-<span class="zip4">2008</span>
	</p>

	<hr/>

	<div id="studyName">${batchInstance?.primaryInstrument?.study?.name}</div>
	<div id="peices">${batchInstance.items.size}</div>

  </body>
</html>
