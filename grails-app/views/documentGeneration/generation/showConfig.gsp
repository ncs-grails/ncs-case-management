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
    <span class="menuButton"><g:link class="list" event="return" action="generation">Choose a Different Mailing Type</g:link></span>
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

          <table class="batchList">
            <thead>
              <tr>
                <th><input type="radio" disabled="disabled" /></th>
                <th>${message(code: 'study.nickName.label', default: 'Study')}</th>
                <th>${message(code: 'instrument.name.label', default: 'Instrument')}</th>
                <th>Peices</th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${batchCreationConfigInstance?.batches.find{it.master == null}}" status="i" var="batchInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                <td><g:radio id="batchId-${batchInstance.id}" name="batch.id" value="${batchInstance.id}" /></td>

              <td><label for="batchId-${batchInstance.id}">${batchInstance?.primaryInstrument?.study?.name}</label></td>

              <td><label for="batchId-${batchInstance.id}">${batchInstance?.primaryInstrument?.name}</label></td>

              <td><label for="batchId-${batchInstance.id}">${batchInstance?.pieces}</label></td>
              </tr>
            </g:each>
            </tbody>
          </table>

          <!-- Reprint Button -->
          <g:actionSubmit action="printDetails" value="Reprint Document(s)" />
          <!-- Batch Report Button -->
          <g:actionSubmit action="batchReport" value="View Batch Report" />
        </g:form>
      </div>
    </fieldset>
    <fieldset class="maroonBorder">
      <legend style="margin-left: 0.5em;">Generate a New Batch</legend>


      <g:form action="generation">
        <g:hiddenField name="id" value="${batchCreationConfigInstance?.id}" />
        <p><!-- Max Items to Generate -->
        <g:radio id="useMaxPieces-false" name="useMaxPieces" value="false" checked="${!useMaxPieces}" />
        <label for="useMaxPieces-false">All Available</label>

        <g:radio name="useMaxPieces" value="true" checked="${useMaxPieces}" />
        <label for="useMaxPieces-true">No more than</label>

        <g:textField name="maxPieces" value="${batchCreationConfigInstance.maxPieces}" /> peices.
        </p>

        <g:if test="${batchCreationConfigInstance?.autoSetMailDate}">
          <p><!-- Mail Date -->
          <g:checkBox name="autoSetMailDate" value="${batchCreationConfigInstance?.autoSetMailDate}" />
          <label for="autoSetMailDate">${message(code: 'batchCreationConfig.autoSetMailDate.label', default: 'Set Mail Date')}:</label>
          <g:datePicker name="mailDate" value="${params.mailDate}" precision="day" />
          </p>
        </g:if>

        <!-- Go Button -->
        <g:if test="${batchCreationConfigInstance?.automaticSelection}" >
          <g:submitButton name="autoGenerate" value="Generate New Batch Automatically" />
        </g:if>
        <g:if test="${batchCreationConfigInstance?.optionalSelection}" >
          <g:submitButton name="optionalGenerate" value="Generate New Batch by Selection" />
        </g:if>
        <g:if test="${batchCreationConfigInstance?.manualSelection}" >
          <g:submitButton name="manualGenerateAction" value="Generate New Batch Manually" />
        </g:if>
      </g:form>

    </fieldset>
  </div>
</body>
</html>
