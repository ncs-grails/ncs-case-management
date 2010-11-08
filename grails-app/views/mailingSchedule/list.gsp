
<%@ page import="edu.umn.ncs.MailingSchedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
  <g:set var="entityName" value="${message(code: 'mailingSchedule.label', default: 'Mailing Schedule')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>


    <g:form action="list" method="post" >
      <div class="dialog">
        <div class="prop">
          <span class="name">
            <label for="instrument"><g:message code="instrumentInstance.label" default="Instrument" /></label>
          </span>
          <span class="value ${hasErrors(bean: instrumentInstance, field: 'id', 'errors')}">
            <g:select name="instrument.id" from="${instrumentInstanceList}" optionKey="id" value="${instrumentInstance?.id}"  />
            <g:submitButton name="list" class="save" value="View" />
          </span>
        </div>
      </div>
    </g:form>

    <div class="list">
      <table>
        <thead>
          <tr>

        <th style="font-weight:bold;color: #333333;"><g:message code="mailingSchedule.instrument.label" default="Instrument" /></th>

        <g:sortableColumn property="checkpointDate" title="${message(code: 'mailingSchedule.checkpointDate.label', default: 'Checkpoint Date')}" />

        <g:sortableColumn property="quota" title="${message(code: 'mailingSchedule.quota.label', default: 'Cumulative Quota')}" />

        <th style="font-weight:bold;color: #333333;">Remove?</th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${mailingScheduleInstanceList}" status="i" var="mailingScheduleInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td>${fieldValue(bean: mailingScheduleInstance, field: "instrument")}</td>

          <td><g:link action="edit" id="${mailingScheduleInstance.id}"><g:formatDate  format="yyyy-MM-dd" date="${mailingScheduleInstance.checkpointDate}" /></g:link></td>

          <td><g:link action="edit" id="${mailingScheduleInstance.id}">${fieldValue(bean: mailingScheduleInstance, field: "quota")}</g:link></td>

          <td><g:link action="delete" id="${mailingScheduleInstance.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
            Remove</g:link></td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <g:form action="save" method="post" >
      <div class="dialog">
        <g:hiddenField name="instrument.id" value="${instrumentInstance?.id}" />

        <div class="prop">
          <span class="name">
            Add New:
          </span>
          <span class="value ${hasErrors(bean: mailingScheduleInstance, field: 'checkpointDate', 'errors')}">
            <label for="checkpointDate">Date:</label>
            <g:datePicker name="checkpointDate" precision="day" value="${mailingScheduleInstance?.checkpointDate}"  />
          </span>

          <span class="value ${hasErrors(bean: mailingScheduleInstance, field: 'quota', 'errors')}">
            <label for="quota"><g:message code="mailingSchedule.quota.label" default="Cumulative Quota" />:</label>
            <g:textField name="quota" value="${fieldValue(bean: mailingScheduleInstance, field: 'quota')}" />
          </span>
          <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
        </div>

      </div>
    </g:form>
  </div>
</body>
</html>
