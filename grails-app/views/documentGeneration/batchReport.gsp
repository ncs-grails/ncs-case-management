<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.umn.ncs.BatchCreationConfig" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Batch Report - National Children's Study</title>
  </head>
  <body>
	<!-- PRIMARY BATCH INFORMATION -->
	<div id="header">
	  <h1>Batch Report</h1>

	  <p id="mailingAddress">
		<span class="name">Ms. Donna DesMarais</span><br/>

		<span class="address">McNamara Alumni Center, Suite 350</span><br/>

		<span class="address2">200 Oak St SE</span><br/>

		<span class="city">Minneapolis</span>, <span class="state">MN</span>
		<span class="zipCode">55455</span>-<span class="zip4">2008</span>
	  </p>

	  <hr/>

	  <div id="studyName">${batchInstance?.primaryInstrument?.study?.name}</div>

	  <div id="peiceCount">Pieces: <span id="peices">${batchInstance?.items?.size()}</span></div>

	  <div id="runBy">Run By: <span id="batchRunBy">${batchInstance?.batchRunBy}</span></div>

	  <div id="dateOnLetter">Date on Letter: <span id="instrumentDate">${batchInstance?.instrumentDate}</span></div>

	  <div id="resend">Resend?: <span id="isResend">${batchInstance?.primaryBatchInstrument?.isResend}</span></div>

	  <hr/>

	  <div id="trackingSent">Tracking Sent:
		<span id="trackingDocumentSent">${batchInstance?.trackingDocumentSent}
		</span>
	  </div>

	  <div id="mailed">Mailed?:
		<span id="mailDate">${batchInstance?.mailDate}
		</span>
	  </div>
	</div>

	<div id="content">

	  <!-- PRIMARY BATCH ATTACHMENTS -->

	  <g:each var="a" in="${batchInstance?.instruments.find{ ! it.isPrimary } }">
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

	</div>

	<!-- PRINT FOOTER INFO -->
	<div id="footer">
	  <div id="reportGenerated">Report Generated:
		<span id="now">${new Date()}
		</span>
	  </div>
	</div>

  </body>
</html>
