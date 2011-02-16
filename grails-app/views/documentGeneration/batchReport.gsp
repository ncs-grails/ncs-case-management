<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="edu.umn.ncs.BatchCreationConfig"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${resource(dir:'css',file:'global.css')}" />
<title>Batch Report - National Children's Study</title>
</head>
<body>

<div
	style="border-bottom: thin solid black; color: maroon; font-variant: small-caps; font-size: 1.5em;">
University of Minnesota</div>
<div style="clear: both;">
<div class="letterheadBlock"
	style="font-weight: bold; margin-left: 7em; text-align: center;">
National Children's Study</div>
</div>

<!-- If we wanted letterhead, it could go here. -->
<!-- <g:include controller="instrument" action="letterhead" /> -->

<!-- PRIMARY BATCH INFORMATION -->
<h1 class="batchReport">Batch Report</h1>


<p id="mailingAddress"><span class="name">Ms. Donna DesMarais</span><br />
<span class="address">McNamara Alumni Center, Suite 350</span><br />
<span class="address2">200 Oak St SE</span><br />
<span class="city">Minneapolis</span>, <span class="state">MN</span> <span
	class="zipCode">55455</span>-<span class="zip4">2008</span><br />
<span class="mcode b"># 2004</span></p>

<hr />

<div class="dataitem">
<div class="datavalue w50"><span class="b" id="studyName">
${batchInstance?.primaryInstrument?.study?.name}
</span></div>
<div class="datavalue w50"><span class="b" id="primaryInstrument">
${batchInstance?.primaryInstrument?.name}
</span></div>
</div>
<div class="clear"></div>

<div class="dataitem">
<div class="datavalue w20">Date Run:</div>
<div class="datavalue w30"><span class="b"><g:formatDate
	format="MM/dd/yyyy" date="${batchInstance?.dateCreated}" /></span></div>
<div class="datavalue w50">Date Called Campus Courier: ________</div>
</div>
<div class="clear"></div>

<div class="dataitem">
<div class="datavalue w20">Date on Letter:</div>
<div class="datavalue w30"><g:if
	test="${batchInstance?.instrumentDate != null}">
	<span class="b" id="instrumentDate"><g:formatDate
		format="MM/dd/yyyy" date="${batchInstance?.instrumentDate}" /></span>
</g:if> <g:else>
          &nbsp;
        </g:else></div>
<div class="datavalue w20">Run By:</div>
<div class="datavalue w30"><span class="b" id="batchRunBy">
${batchInstance?.batchRunBy}
</span></div>
</div>

<div class="clear"></div>

<div class="dataitem">
<div class="datavalue w20">Resend?:</div>
<div class="datavalue w30"><span class="b" id="isResend">
${batchInstance?.primaryBatchInstrument?.isResend}
</span></div>
<div class="datavalue w20">Tracking Sent:</div>
<div class="datavalue w30"><span class="b"
	id="trackingDocumentSent">
${batchInstance?.trackingDocumentSent}
</span></div>
</div>
<div class="clear"></div>

<hr />

<!-- List the batches -->
<g:each var="b" in="${batchInstanceList}">

	<div class="clear">

	<div class="instrumentName blackBorder nowrap">
	${b?.primaryInstrument?.name}
	</div>

	<div class="isInitial blackBorder nowrap">
	${b?.primaryBatchInstrument?.isInitial}
	</div>

	<div class="instrumentVersion nowrap">Version:
	<div class="version blackBorder nowrap">
	${b?.primaryBatchInstrument?.version}
	</div>
	</div>
	</div>

	<div class="indent clear"><!-- BATCH ATTACHMENTS --> <g:each
		var="a" in="${b?.instruments.find{ !it?.isPrimary }}">
		<div class="attachment">
		<div class="instrument">
		${a.instrument?.name}
		</div>
		<div class="instrumentVersion">Version:
		<div class="version  blackBorder ">
		${a?.version}
		</div>
		</div>
		</div>
	</g:each>

	<div class="mode blackBorder nowrap"><span class="direction">
	${b.direction}
	</span> <span class="format">
	${b.format}
	</span></div>
	<div class="batchId nowrap">Batch ID: <span
		class="batch-id fixedFont">B${b.id} </span> <!-- TODO: --> <span
		class="batch-id-barcode barcode"> <img
		src="${createLink(controller:'barcode', action:'png', id:'B' + b.id)}" />
	</span></div>
	</div>

	<hr />

	<div class="dataitem mt1">
	<div class="datavalue w70 ">Sample Documents :</div>
	<div class="datavalue w30"><span
		style="font-size: 1em; font-weight: bold; text-align: right;">
	${sampleDocumentsTotal}
	</span></div>
	</div>
	<div class="clear"></div>

	<div class="dataitem mt1">
	<div class="datavalue w70 bb1">Tracking Documents Mailed :</div>
	<div class="datavalue w30"><span
		style="font-size: 1.1em; font-weight: bold;">
	${notSampleTrackingDocumentsTotal}
	</span></div>
	</div>
	<div class="clear"></div>

	<div class="dataitem mt1">
	<div class="datavalue w70 bb1">Mailed Document (excluding
	tracking and sample):</div>
	<div class="datavalue w30"><span
		style="font-size: 1.1em; font-weight: bold;">
	${batchInstance?.items?.size()}
	</span></div>
	</div>
	<div class="clear"></div>

	<hr />

	<div class="dataitem mt1">
	<div class="datavalue w70 bb1">Total Mailed :</div>
	<div class="datavalue w30"><span
		style="font-size: 1.1em; font-weight: bold;">
	${batchInstance?.items?.size() + notSampleTrackingDocumentsTotal}
	</span></div>
	</div>
	<div class="clear"></div>

	<div class="dataitem mt1">
	<div class="datavalue w70 bb1 b">Date Mailed:</div>
	</div>
	<div class="clear"></div>

	<hr />



	<!-- 	<div id="manualForm">
	  <p style="margin-top: 1em; width: 100%;">
		Sample Documents :<span style="font-size:1.5em; font-weight: bold; text-align:right;">${sampleDocumentsTotal}</span></p>
	  <p style="margin-top: 1em; width: 100%;">
		Tracking Documents Mailed :<span style="font-size:1.5em; font-weight: bold;">${notSampleTrackingDocumentsTotal}</span></p>
	  <p style="border-bottom: thin solid black; margin-top: 1em; width: 100%;">
		Mailed Document excluding tracking or sample):<span style="font-size:1.5em; font-weight: bold;">${batchInstance?.items?.size()}</span></p>
	  <p style="margin-top: 1em; width: 100%;">
		Total Mailed :<span style="font-size:1.5em; font-weight: bold;">${batchInstance?.items?.size() + notSampleTrackingDocumentsTotal}</span></p>
          <p style="border-bottom: thin solid black; margin-top: 1em; width: 100%;">
            <span style="font-size:1em; font-weight: bold;">Date Mailed:</span>
          </p>

	</div>
 -->


</g:each>


<!-- PRINT FOOTER INFO -->
<div id="footer">
<div id="reportGenerated">Report Generated: <span id="now">
${new Date()}
</span></div>
<p style="font-size: 1.5em; font-weight: bold; font-style: italic;">
* Please fill in mail date and fax this to me when completed at (612)
625-4363</p>
</div>
</body>
</html>
