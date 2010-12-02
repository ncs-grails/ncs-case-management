<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Batch Details Entry - National Children's Study</title>
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'global.css')}" />
  <g:javascript src="batch.js" />
</head>
<body>
  <div class="nav">
	<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
  </div>
  <h1>Batch Mail Date Entry</h1>

  <g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
  </g:if>

  <!-- Mail Date Entry Form -->
  <g:form name="batchIdForm" action="entry">
	
	<div>
	  <label for="id">Scan in batch report here: </label>
	  <g:textField style="border-style: solid; border-width: thin; margin: 1px;" value="" name="id" id="id" />
	  <g:hiddenField name="referenceDate" value="date.struct" />
	  <g:hiddenField name="referenceDate_day" value="${formatDate(date:referenceDate, format:'d')}" />
	  <g:hiddenField name="referenceDate_month" value="${formatDate(date:referenceDate, format:'M')}" />
	  <g:hiddenField name="referenceDate_year" value="${formatDate(date:referenceDate, format:'yyyy')}" />
	</div>
  </g:form>

  <g:form name="mailDateForm" action="entry">

	<fieldset class="maroonBorder">
	  <legend style="margin-left: 0.5em;">Choose the mail date for the batch you are scanning</legend>
	  <label for="referenceDate">Mail date: </label>
	  <g:datePicker name="referenceDate" value="${referenceDate}" precision="day" />
	</fieldset>

  </g:form>

  <!-- list of batches mailed on this date -->
  <fieldset class="maroonBorder">
	<legend style="margin-left: 0.5em;">Batches mailed on
	  <g:formatDate format="yyyy-MM-dd" date="${referenceDate}"/></legend>

	<ul>
	  <g:if test="${ ! sentBatchInstanceList }">
		<li>None</li>
	  </g:if>
	  <g:each var="b" in="${sentBatchInstanceList}">
		<li>${b.id} - 
		  ${b.primaryInstrument.study} ${b.primaryInstrument} 
		  generated on
		  <g:formatDate format="yyyy-MM-dd" date="${b.dateCreated}"/>
		</li>
	  </g:each>
	</ul>
  </fieldset>

  <!-- list of batches not mailed 2 weeks prior and up to the date -->
  <fieldset class="maroonBorder">
	<legend style="margin-left: 0.5em;">Batches generated, but not mailed 2
		  weeks prior to <g:formatDate format="yyyy-MM-dd" date="${referenceDate}"/></legend>

	<ul>
	  <g:if test="${ ! unsentBatchInstanceList }">
		<li>None</li>
	  </g:if>
	  <g:each var="b" in="${unsentBatchInstanceList}">
		<li>${b.id} -
		  ${b.primaryInstrument.study} ${b.primaryInstrument}
		  generated on
		  <g:formatDate format="yyyy-MM-dd" date="${b.dateCreated}"/>
		</li>
	  </g:each>
	</ul>
  </fieldset>

</body>
</html>
