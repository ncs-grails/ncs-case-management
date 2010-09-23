<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${resource(dir:'css',file:'global.css')}" />
    <title>Batch Report - National Children's Study</title>
  </head>
  <body>

    <!-- PRIMARY BATCH INFORMATION -->
    <h1 class="batchReport">Batch Report</h1>

    <p id="mailingAddress">
      <span class="name">Ms. Donna DesMarais</span><br/>
      <span class="address">McNamara Alumni Center, Suite 350</span><br/>
      <span class="address2">200 Oak St SE</span><br/>
      <span class="city">Minneapolis</span>, <span class="state">MN</span>
      <span class="zipCode">55455</span>-<span class="zip4">2008</span>
    </p>

    <hr/>

    <div class="dataitem">
      <div class="datavalue w50l"><span class="b" id="studyName">${batchInstance?.primaryInstrument?.study?.name}</span></div>
      <div class="datavalue w50r">Pieces: <span class="b" id="pieces">${batchInstance?.items?.size()}</span></div>
    </div>

    <div class="dataitem">
      <div class="datavalue w50l">Date Run: <span class="b"><g:formatDate format="MM/dd/yyyy" date="${batchInstance?.dateCreated}"/></span></div>
      <div class="datavalue w50r"><span class="b" id="primaryInstrument">${batchInstance?.primaryInstrument?.name}</span></div>
    </div>

    <div class="dataitem">
      <div class="datavalue w30">Run By: <span class="b" id="batchRunBy">${batchInstance?.batchRunBy}</span></div>
      <div class="datavalue w50">Date on Letter: <span class="b" id="instrumentDate"><g:formatDate format="MM/dd/yyyy" date="${batchInstance?.instrumentDate}"/></span></div>
      <div class="datavalue w20">Resend?: <span class="b" id="isResend">${batchInstance?.primaryBatchInstrument?.isResend}</span></div>
    </div>

    <div class="clear"></div>
    <hr/>

    <div class="dataitem">
      <div class="datavalue w30">Tracking Sent: <span class="b" id="trackingDocumentSent">${batchInstance?.trackingDocumentSent}</span></div>
      <div class="datavalue w30"><span>&nbsp;</span></div>
      <div class="datavalue w30">Mailed:<span class="b" id="mailDate"><g:formatDate format="MM/dd/yyyy" date="${batchInstance?.mailDate}"/></span></div>
    </div>

    <div class="clear"></div>
    <hr/>



    <!-- PRIMARY BATCH ATTACHMENTS -->

  <g:each var="a" in="${batchInstance?.instruments.find{ !it?.isPrimary }}">
    <div class="attachment">
      <div class="instrument">${a.instrument?.name}</div>
      <div class="instrumentVersion"> Version:
        <span class="version">${a?.version}
        </span>
      </div>
      <div class="isInitial">${a.isInitial}</div>
      <div class="isResend">${a.isResend}</div>
    </div>
  </g:each>

  <!-- CHILD / SISTER BATCHES -->

  <div class="dataitem">
    <div class="datavalue">
      CHILD Batches
    </div>
  </div>
  <g:each var="b" in="${batchInstanceList}">
    <div class="dataitem">
      <div class="datavalue w50"><span> ${b?.primaryInstrument?.name}</span></div>
    </div>
    <div class="clear"></div>
  </g:each>


  <hr/>
  <!-- PRINT FOOTER INFO -->
  <div id="footer">
    <div id="reportGenerated">Report Generated:
      <span id="now">${new Date()}</span>
    </div>
  </div>
</body>
</html>
