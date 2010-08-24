
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
	<span class="menuButton"><g:link class="list" action="list">Choose a Different Mailing Type</g:link></span>
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
	  <div class="list">

		<g:form method="post" controller="documentGeneration" >
                  <g:hiddenField name="batchCreationConfig.id" value="${batchCreationConfigInstance?.id}" />

		  <table>
			<thead>
			  <tr>
				<th><input type="radio" disabled="disabled" /></th>
				<th>${message(code: 'study.nickName.label', default: 'Study')}</th>
				<th>${message(code: 'instrument.name.label', default: 'Instrument')}</th>
				<th>Peices</th>
			  </tr>
			</thead>
			<tbody>
			<g:each in="${batchInstanceList}" status="i" var="batchInstance">
			  <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td><g:radio id="batch.id-${batchInstance.id}" name="batch.id" value="${batchInstance.id}" /></td>

			  <td><label for="batch.id-${batchInstance.id}">${batchInstance?.primaryInstrument?.study?.name}</label></td>

			  <td><label for="batch.id-${batchInstance.id}">${batchInstance?.primaryInstrument?.name}</label></td>

			  <td><label for="batch.id-${batchInstance.id}">${batchInstance?.pieces}</label></td>
			  </tr>
			</g:each>
			</tbody>
		  </table>




		  <!-- Reprint Button -->
		  <g:actionSubmit action="reGenerate" value="Reprint Document(s)" />
		  <!-- Batch Report Button -->
		  <g:actionSubmit action="batchReport" value="View Batch Report" />
		</g:form>
	  </div>
	</fieldset>
	<fieldset class="maroonBorder">
	  <legend style="margin-left: 0.5em;">Generate a New Batch</legend>


		<g:form action="autoGenerate" id="${batchCreationConfigInstance?.id}">

		  <p><!-- Max Items to Generate -->
		  <g:radio id="useMaxPieces-false" name="useMaxPieces" value="false" checked="${!useMaxPieces}" />
		  <label for="useMaxPieces-false">All Available</label>

		  <g:radio name="useMaxPieces" value="true" checked="${useMaxPieces}" />
		  <label for="useMaxPieces-true">No more than</label>

		  <g:textField name="maxPieces" value="${batchCreationConfigInstance.maxPieces}" /> peices.
		  </p>

		  <p> <!-- Batch Date -->
		  <g:checkBox name="autoSetMailDate" value="${batchCreationConfigInstance?.autoSetMailDate}" />
		  <label for="autoSetMailDate">${message(code: 'batchCreationConfig.autoSetMailDate.label', default: 'Set Mail Date')}:</label>
		  <g:datePicker name="mailDate" value="${params.mailDate}" precision="day" />
		  </p>


		  <!-- Go Button -->
		  <g:actionSubmit action="autoGenerate" value="Generate New Batch Automatically" />
		  <g:actionSubmit action="manualGenerate" value="Generate New Batch Manually" />
		</g:form>

	</fieldset>
  </div>
</body>
</html>
