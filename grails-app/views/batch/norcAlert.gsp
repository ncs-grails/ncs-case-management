<%@ page contentType="text/html"%>
<html>
<head>
<title>NORC Posting Mailing Alert - National Children's Study</title>
</head>
<body>
<h1 style="color: maroon;">NCS Production Report for: <g:formatDate
	date="${referenceDate}" format="MM/dd/yyyy" /></h1>
<hr />

<g:if test="${customizable}">
	<g:form action="norcAlert">
		<g:datePicker name="referenceDate" value="${referenceDate}"
			precision="day" />
		<g:submitButton name="update" value="Update" />
	</g:form>
	<g:form action="sendNorcAlert">
		<g:hiddenField name="referenceDate" value="date.struct" />
		<g:hiddenField name="referenceDate_day"
			value="${formatDate(date:referenceDate, format:'d')}" />
		<g:hiddenField name="referenceDate_month"
			value="${formatDate(date:referenceDate, format:'M')}" />
		<g:hiddenField name="referenceDate_year"
			value="${formatDate(date:referenceDate, format:'yyyy')}" />

		<g:submitButton action="sendNorcAlert" name="remoteDestination"
			value="Email Report" />
	</g:form>
	<div id="remoteDestination"></div>
</g:if>

<g:each var="batchInstance" in="${batchInstanceList}">
	<div style="border: solid maroon;">

	<h2 style="color: maroon;"><span class="b" id="studyName">
	${batchInstance?.primaryInstrument?.study?.name}
	</span> <span class="b" id="primaryInstrument">
	${batchInstance?.primaryInstrument?.name}
	</span></h2>
	<div class="clear"></div>

	<div class="dataitem">Date Run: <span class="b"><g:formatDate
		format="MM/dd/yyyy" date="${batchInstance?.dateCreated}" /></span></div>

	<div class="dataitem">Date on Letter: <g:if
		test="${batchInstance?.instrumentDate != null}">
		<span class="b" id="instrumentDate"><g:formatDate
			format="MM/dd/yyyy" date="${batchInstance?.instrumentDate}" /></span>
	</g:if> <g:else>
			&nbsp;
		  </g:else></div>
	<div class="dataitem">Run By: <span class="b" id="batchRunBy">
	${batchInstance?.batchRunBy}
	</span></div>

	<div class="clear"></div>

	<div class="dataitem">Resend?: <span class="b" id="isResend">
	${batchInstance?.primaryBatchInstrument?.isResend}
	</span></div>
	<div class="dataitem">Tracking Sent: <span class="b"
		id="trackingDocumentSent">
	${batchInstance?.trackingDocumentSent}
	</span></div>

	<hr />

	<!-- List the batches --> <g:each var="b" in="${[]}">

		<div class="clear"><span
			class="instrumentName blackBorder nowrap">
		${b?.primaryInstrument?.name}
		</span> <span class="isInitial blackBorder nowrap">
		${b?.primaryBatchInstrument?.isInitial}
		</span> <span class="instrumentVersion nowrap"> Version: <span
			class="version blackBorder nowrap">
		${b?.primaryBatchInstrument?.version} </span> </span></div>

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
			class="batch-id fixedFont">B${b.id} </span></div>

		</div>

		<hr />
	</g:each>


	<div class="dataitem mt1">Mailed Documents (excluding tracking
	and sample): <span style="font-size: 1.1em; font-weight: bold;">
	${batchInstance?.items?.size()}
	</span></div>

	<hr />

	</div>
</g:each>

<hr />

<div id="footer" style="font-size: 0.6em; color: gray;"><span>This
report run from "ncs-case-management".</span></div>

</body>
</html>
