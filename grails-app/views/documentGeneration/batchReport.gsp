<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${resource(dir:'css',file:'global.css')}" />
    <title>Batch Report - National Children's Study</title>
  </head>
  <body>

	<!-- If we wanted letterhead, it could go here. -->
	<g:include controller="instrument" action="letterhead" />

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

    <!-- List the batches -->
  <g:each var="b" in="${batchInstanceList}">

    <div class="clear">

      <div class="instrumentName blackBorder nowrap">${b?.primaryInstrument?.name}</div>

      <div class="isInitial blackBorder nowrap">${b?.primaryBatchInstrument?.isInitial}</div>

      <div class="instrumentVersion nowrap"> Version:
        <div class="version blackBorder nowrap">${b?.primaryBatchInstrument?.version}
        </div>
      </div>
    </div>

    <div class="indent clear">

      <!-- BATCH ATTACHMENTS -->
      <g:each var="a" in="${b?.instruments.find{ !it?.isPrimary }}">
        <div class="attachment">
          <div class="instrument">${a.instrument?.name}</div>
          <div class="instrumentVersion"> Version:
            <div class="version  blackBorder ">${a?.version}
            </div>
          </div>
        </div>
      </g:each>

      <div class="mode blackBorder nowrap">
        <span class="direction">${b.direction}</span>
        <span class="format">${b.format}</span>
      </div>
      <div class="batchId nowrap">Batch ID:
        <span class="batch-id fixedFont">
		  B${b.id}
        </span>
        <!-- TODO: -->
        <span class="batch-id-barcode barcode">
		  <img src="${createLink(controller:'barcode', action:'png', id:'B' + b.id)}" />
		</span>
      </div>
    </div>

  <hr/>

	<div id="manualForm">
	  <p style="border-bottom: thin solid black; margin-top: 1em; width: 100%;">
		Tracking Documents Mailed :</p>
	  <p style="border-bottom: thin solid black; margin-top: 1em; width: 100%;">
		Peices Mailed Documents :
		<span style="font-size:1.5em; font-weight: bold;">${batchInstance?.items?.size()}</span></p>
	  <p style="border-bottom: thin solid black; margin-top: 1em; width: 100%;">
		Total Mailed :</p>
	  <p style="border-bottom: thin solid black; margin-top: 1em; width: 100%;">
		Sample Docs :</p>
	</div>

    <p/>
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
