
<%@ page contentType="text/html;charset=UTF-8"%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Auto Document Generation - National Children's Study</title>
  <meta name="layout" content="ncs" />
    <g:javascript src="documentGeneration.js"/>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
  <span class="menuButton">
    <g:link class="show" action="generation" event="return">Back to Document Generation</g:link>
  </span>
</div>
<div class="body">

<h1>Batch Details</h1>

<h2>${batchCreationConfigInstance?.name}</h2>

<fieldset class="maroonBorder">
	<legend class="m1">1. Data Sources To Save...</legend>
		<div class="list">
			<table>
                <tbody>
                <g:each var="ds" status="i" in="${batchCreationConfigInstance?.documents.sort{it?.id}}">
                	<g:if test="${ds.mergeSourceFile != null}">
	                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                    	<td class="pad">
	                    		<g:link action="downloadDataset" class="csv" params="${ [ 'batch.id':batchInstance?.id, 'batchCreationDocument.id': ds?.id ] }">
	                    			${ds?.mergeSourceFile}
	                    		</g:link>
	                    	</td>
	                    </tr>
                    </g:if>
                </g:each>
                </tbody>		
			</table>
		</div>
</fieldset>	

<fieldset class="maroonBorder">
	<legend style="margin-left: 0.5em;">2. Documents to download...</legend>
	<div class="list">		
		<table>
			<tbody>
				<g:each var="doc" status="i" in="${batchCreationConfigInstance?.documents.sort{it?.id}}">
					<g:if test="${doc.documentLocation}">
						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<td>
								<g:link controller="documentGeneration" action="downloadDocument" id="${doc.id}" class="pad">
									n:/Production Documents/${doc.documentLocation}
								</g:link>
							</td>
						</tr>
					</g:if>
				</g:each>
			</tbody>
		</table>
	</div>
</fieldset>

<fieldset class="maroonBorder">
	<legend style="margin-left: 0.5em;">3. Print the Batch Report</legend>
	<g:link class="report" controller="documentGeneration" action="batchReport" id="${batchInstance?.id}">Open the batch report...</g:link>
</fieldset>

</div>
</body>
</html>
