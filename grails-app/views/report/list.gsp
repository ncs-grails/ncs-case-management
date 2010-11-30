<%@ page contentType="text/html;charset=UTF-8" %>
<g:setProvider library="jquery"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
  <g:set var="entityName" value="${message(code: 'Report.label', default: 'Report')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

    <fieldset class="maroonBorder">
      <legend>Advance Letter</legend>
        <div class="list">
          <table>
            <thead>
              <tr>

            <g:sortableColumn property="id" title="${message(code: 'Report.id.label', default: 'ID')}" />
            <g:sortableColumn property="name" title="${message(code: 'Report.name.label', default: 'Report')}" />

            </tr>
            </thead>
            <tbody>
            <g:each in="${reportInstanceList}" status="i" var="reportInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td><g:link action="batchAnalysis" id="${reportInstance.id}">${fieldValue(bean: reportInstance, field: "id")}</g:link></td>
                <td><g:link action="batchAnalysis" id="${reportInstance.id}">${reportInstance?.name}</g:link></td>

              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
      </fieldset>



    <div class="paginateButtons">
      <g:paginate total="${reportInstanceTotal}" />
    </div>
  </div>
</body>
</html>