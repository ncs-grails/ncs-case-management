<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<%@ page import="org.joda.time.*" %>
  <html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>Recent Batches Status - National Children's Study</title>
      <meta name="layout" content="ncs" />
      <link rel="stylesheet" href="${resource(dir:'css',file:'global.css')}" />
      <g:javascript src="batch.js" />
  </head>
  <body>
    <div class="nav">
          <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    </div>
    <h1>Recent Batches Status</h1>

    <g:if test="${flash.message}">
          <div class="message">${flash.message}</div>
    </g:if>

    <!-- Mail Date Entry Form -->
    <g:form name="mailDateForm" action="listByDate">


          <fieldset class="maroonBorder">
            <legend style="margin-left: 0.5em;">Choose the month for the batches you would like to view:</legend>
            <label for="referenceDate">List Batches By Date: </label>
            <g:datePicker name="referenceDate" value="${referenceDate}" precision="month" />
          </fieldset>
    </g:form>

    <fieldset class="maroonBorder">
      <legend style="margin-left:0.5em">Batches generated 1 month prior to <g:formatDate date="${endDate}" format="MM-dd-yyyy"/></legend>

        <div class="list">
          <table style="font-size:0.85em;">
            <thead>
              <tr>
                <th>${message(code: 'batch.id.label', default: 'Batch ID')} </th>
                <th>${message(code: 'study.nickName.label', default: 'Study')} </th>
                <th>${message(code: 'instrument.name.label', default: 'Instrument')} </th>
                <th>${message(code: 'pieces.name.label', default: 'Pieces')} </th>
                <th>${message(code: 'addressingMailingDate.name.label', default: 'A&M Date')} </th>
                <th>${message(code: 'dateCreated.label', default: 'Date Created')} </th>
                <th>${message(code: 'instrumentDate.label', default: 'Instrument Date')} </th>
                <th>${message(code: 'mailDate.label', default: 'Mail Date')} </th>
                <th>${message(code: 'calledCampusCourierDate.label', default: 'Campus Courier')} </th>
                <th>${message(code: 'receiptDate.label', default: 'Tracking Return')} </th>
                <th>Edit</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${batchInstanceList}" status="i" var="b">
              
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                
                <td>${b?.id}</td>
                <td>${b?.primaryInstrument?.study}</td>
                <td>${b.primaryInstrument}</td>
                <td>${b.pieces}</td>
                <td><g:formatDate date="${b?.addressAndMailingDate}" format="M/d/yyyy"/></td>
                <td><g:formatDate date="${b?.dateCreated}" format="M/d/yyyy"/></td>
                <td><g:formatDate date="${b?.instrumentDate}" format="M/d/yyyy"/></td>
                <td><g:formatDate date="${b?.mailDate}" format="M/d/yyyy"/></td>
                <td><g:formatDate date="${b?.calledCampusCourierDate}" format="M/d/yyyy"/></td>
                <td><g:formatDate date="${b?.trackingReturnDate}" format="M/d/yyyy"/></td>
                <td><g:link action="edit" id="${b.id}">edit</g:link></td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
    </fieldset>

  </body>
</html>