<%@ page contentType="text/html;charset=UTF-8" %>
<g:setProvider library="jquery"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
  <title>Counts by Segment and Group</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    <span class="menuButton"><g:link action="generation" event="loadRecentBatches" id="${batchCreationConfigInstance?.id}"params="[q:'adv']">${batchCreationConfigInstance?.name}</g:link></span>
  </div>
  <div class="body">
    <h1>Counts by Segment and Group</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

    <fieldset class="maroonBorder">
      <legend>${batchCreationConfigInstance?.name}</legend>
        <div class="list">
          <table>
            <thead>
              <tr>

              <g:sortableColumn property="segment id" title="${message(code: 'batchAnalysis.segment_id.label', default: 'Segment ID')}" />
              <g:sortableColumn property="name" title="${message(code: 'batchAnalysis.group.label', default: 'Group')}" />
              <g:sortableColumn property="segment ratio" title="${message(code: 'batchAnalysis.segment_ratio.label', default: 'Segment Ratio')}" />
              <g:sortableColumn property="group ratio" title="${message(code: 'batchAnalysis.group_ratio.label', default: 'Group Ratio')}" />
              <g:sortableColumn property="sent" title="${message(code: 'batchAnalysis.sent.label', default: 'Sent')}" />

            </tr>
            </thead>
            <tbody>
            <g:each in="${advLetterSentInstanceList}" status="i" var="advLetterSentInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td>${advLetterSentInstance.segment_id}</td>
                <td>${advLetterSentInstance.name}</td>
                <td>${advLetterSentInstance.segmentRatio}</td>
                <td>${advLetterSentInstance.groupRatio}</td>
                <td>${advLetterSentInstance.sent}</td>

              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
      </fieldset>



    <div class="paginateButtons">
      <g:paginate total="${advLetterSentInstanceTotal}" />
    </div>
  </div>
</body>
</html>