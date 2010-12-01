<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>National Children's Study - Batch Details Entry</title>
  </head>
  <body>
    <h1>Batch Details Entry</h1>

    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

  <!-- Mail Date Entry Form -->
  <g:form action="entry">
	<g:datePicker value="${referenceDate}" />

	<!-- list of batches not mailed 2 weeks prior and up to the date -->
	<ul>
	<g:each var="b" in="sentBatchInstanceList">
	  <li>${b.id} - ${b.dateCreated}</li>
	</g:each>
	</ul>

	<ul>
	<g:each var="b" in="unsentBatchInstanceList">
	  <li>${b.id} - ${b.dateCreated}</li>
	</g:each>
	</ul>

	<!-- list of batches mailed on this date -->
  </g:form>
</body>
</html>
