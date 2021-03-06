
<%@ page import="edu.umn.ncs.Report" %>
<html>
<head>
<title>Case Management - National Children's Study</title>
<meta name="layout" content="ncs" />
<style type="text/css" media="screen">
#nav {
	margin-top: 20px;
	margin-left: 30px;
	width: 228px;
	float: left;
}

.homePagePanel * {
	margin: 0px;
}

.homePagePanel .panelBody ul {
	list-style-type: none;
	margin-bottom: 10px;
}

.homePagePanel .panelBody h1 {
	text-transform: uppercase;
	font-size: 1.1em;
	margin-bottom: 10px;
}
.homePagePanel .panelBody h2 {
	font-size: 1.1em;
	margin-bottom: 10px;
	font-weight:bold;
}
.homePagePanel .panelBody {
	background: url(images/leftnav_midstretch.png) repeat-y top;
	margin: 0px;
	padding: 15px;
}
.homePagePanel-big .panelBody {
	background: url(images/leftnav_midstretch_680x1.png) repeat-y top;
	margin: 0px;
	padding: 15px;
}
.homePagePanel .panelBtm {
	background: url(images/leftnav_btm.png) no-repeat top;
	height: 20px;
	margin: 0px;
}

.homePagePanel .panelTop {
	background: url(images/leftnav_top.png) no-repeat top;
	height: 11px;
	margin: 0px;
}

h2 {
	margin-top: 15px;
	margin-bottom: 15px;
	font-size: 1.2em;
}

#pageBody {
	margin-left: 280px;
	margin-right: 20px;
}
</style>
</head>
<body>
<div class="nav"><span class="logoutButton"><g:link
	class="create" controller="logout" action="index">Logout</g:link></span></div>

<div id="nav">
<sec:ifAnyGranted roles="ROLE_NCS_IT">
	<div class="homePagePanel">
	<div class="panelTop"></div>
	<div class="panelBody">
		<h2>Report Administration</h2>
		<ul>
			<li><g:link controller="report">Report List</g:link></li>
			<li></span><g:link controller="report" action="create">Create a Report</g:link></li>
		</ul>
	</div>
	<div class="panelBtm"></div>
	</div>
</sec:ifAnyGranted>

<div class="homePagePanel">
<div class="panelTop"></div>
<div class="panelBody">
<h1>Application Status</h1>
<ul>
	<li>App version: <g:meta name="app.version"></g:meta></li>
	<li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
	<li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}
	</li>
	<li>JVM version: ${System.getProperty('java.version')}
	</li>
	<li>Controllers: ${grailsApplication.controllerClasses.size()}
	</li>
	<li>Domains: ${grailsApplication.domainClasses.size()}
	</li>
	<li>Services: ${grailsApplication.serviceClasses.size()}
	</li>
	<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}
	</li>
</ul>
<h1>Installed Plugins</h1>
<ul>
	<g:set var="pluginManager"
		value="${applicationContext.getBean('pluginManager')}"></g:set>

	<g:each var="plugin" in="${pluginManager.allPlugins}">
		<li>
		${plugin.name} - ${plugin.version}
		</li>
	</g:each>

</ul>
</div>
<div class="panelBtm"></div>
</div>
</div>
<div id="pageBody">
<h1>Welcome to the National Children's Study</h1>

<p>This application is available for generating and tracking study
documents and tracking the location of study participants.</p>

<div id="pageList" class="dialog"><sec:ifAnyGranted
	role="ROLE_NCS_IT">

	<h2>Configuration</h2>
	<dl class="menu">
		<dt><g:link controller="instrument" action="list">Instruments</g:link></dt>
		<dd>Manage the instruments used to interact with study subjects.
		Instruments include such items as invitations, surveys, phone calls,
		events, etc...</dd>
		<dt><g:link controller="instrumentHistory" action="list">Instrument Version Tracking</g:link></dt>
		<dd>Manage instrument comments, revisions, and approvals.</dd>
		<dt><g:link controller="batchCreationConfig" action="list">Configure Batch Generation</g:link></dt>
		<dd>Configure the way that batches of instruments are generated
		in the system so that they can be properly tracked</dd>
		<dt><g:link controller="mailingSchedule" action="list">Tweak Mailing Schedule</g:link></dt>
		<dd>Manage mailing dates and quota for the instrument.</dd>
		<dt><g:link controller="batch" action="list">Batch Management</</g:link></dt>
		<dd>Manage batch attributes and items.</dd>
	</dl>
</sec:ifAnyGranted>

<h2>Data Creation / Entry</h2>
<dl class="menu">
	<dt><g:link controller="documentGeneration">Document Generation</g:link></dt>
	<dd>Generate instrument batches either automatically or by hand.</dd>
	<dt><g:link controller="receiptItems" action="index">Receipt Items</g:link></dt>
	<dd>Receipt documents</dd>
	<dt><g:link controller="batch" action="entry">Mail Date Entry</g:link></dt>
	<dd>Enter mail dates for generated batches</dd>
</dl>

		<h2>Reporting</h2>
		<dl class="menu">
			<dt><g:link controller="lookup">Lookup</g:link></dt>
			<dd>Search for dwelling units, or people and view information
			about them</dd>

			<dt><g:link controller="batch" action="listByDate">List of Generated Batches By Month</g:link></dt>
			<dd>
				Provides a list of the batches for the selected month with an option to update A&M Date, Mail Date, Printing Services Date and tracking Return Date.
			</dd>
			
			<g:each var="reportInstance" in="${Report.list()}">
				<g:if test="${reportInstance?.enabled}">
					<g:if test="${reportInstance?.adminsOnly}">
						<sec:ifAnyGranted roles="ROLE_NCS_IT">
							<dt>
								<g:if test="${reportInstance?.underConstruction}">
									<img src="${resource(dir:'images',file:'under_construction_icon-red_30x25.png')}" class="under-construction-img" title="Under construction" alt="Under construction" />
								</g:if>
								<g:link controller="report" action="showReport" id="${reportInstance.id}" target="_blank">${reportInstance?.title}</g:link>
								<g:if test="${reportInstance.useQuery}">
									<g:pdfLink pdfController="localNcsPdf" pdfAction="exportReportByQueryToPdf" pdfId="${reportInstance.id}" filename="${reportInstance?.title.replaceAll(' ','_') + '.pdf'}" icon="true"></g:pdfLink>
									<g:link controller="report" action="exportReportByQueryToFile" id="${reportInstance.id}" params="[format:'csv']">
										<img src="${resource(dir:'images',file:'excel_16x16.png')}" class="export-csv-img" title="CSV" alt="CSV" />
									</g:link>
								</g:if>
								<g:else>
									<g:link controller="report" action="exportBirtReport" id="${reportInstance.id}" params="[format:'pdf']">
										<img src="${resource(dir:'images',file:'pdf_button.png')}" class="export-img" title="Export to PDF" alt="Export to PDF" />
									</g:link>
									<g:link controller="report" action="exportBirtReport" id="${reportInstance.id}" params="[format:'xls']">
										<img src="${resource(dir:'images',file:'excel_16x16.png')}" class="export-img" title="Export to XLS" alt="Export to XLS" />
									</g:link>
								</g:else>
							</dt>
							<dd>
								<g:if test="${reportInstance?.subtitle}">
									${reportInstance?.subtitle}
								</g:if>
								<g:else>
									${reportInstance?.description}
								</g:else>
							</dd>	
						</sec:ifAnyGranted>		
					</g:if>
					<g:else>
						<dt>
							<g:if test="${reportInstance?.underConstruction}">
								<img src="${resource(dir:'images',file:'under_construction_icon-red_30x25.png')}" class="under-construction-img" title="Under construction" alt="Under construction" />
							</g:if>
							<g:link controller="report" action="showReport" id="${reportInstance.id}" target="_blank">${reportInstance?.title}</g:link>
							<g:if test="${reportInstance.useQuery}">
									<g:pdfLink pdfController="localNcsPdf" pdfAction="exportReportByQueryToPdf" pdfId="${reportInstance.id}" filename="${reportInstance?.title.replaceAll(' ','_') + '.pdf'}" icon="true"></g:pdfLink>
								<g:link controller="report" action="exportReportByQueryToFile" id="${reportInstance.id}" params="[format:'csv']">
									<img src="${resource(dir:'images',file:'excel_16x16.png')}" class="export-csv-img" title="CSV" alt="CSV" />
								</g:link>
							</g:if>
							<g:else>
								<g:link controller="report" action="exportBirtReport" id="${reportInstance.id}" params="[format:'pdf']">
									<img src="${resource(dir:'images',file:'pdf_button.png')}" class="export-img" title="Export to PDF" alt="Export to PDF" />
								</g:link>
								<sec:ifAnyGranted roles="ROLE_NCS_IT">
									<g:link controller="report" action="exportBirtReport" id="${reportInstance.id}" params="[format:'xls']">
										<img src="${resource(dir:'images',file:'excel_16x16.png')}" class="export-img" title="Export to XLS" alt="Export to XLS" />
									</g:link>
								</sec:ifAnyGranted>		
							</g:else>
						</dt>
						<dd>
							<g:if test="${reportInstance?.subtitle}">
								${reportInstance?.subtitle} 
							</g:if>
							<g:else>
								${reportInstance?.description}
							</g:else>
						</dd>	
					</g:else>			
				</g:if>
			</g:each>
        </dl>

		<%--<dl class="menu highlight-box">
			<sec:ifAnyGranted roles="ROLE_NCS_IT">
			<h2>Report Administration (admins only)</h2>
				<dt><g:link controller="report">Report List</g:link></dt>
				<dd>
					List of created reports
				</dd>
				<dt><g:link controller="report" action="create">Create a Report</g:link></dt>
				<dd>
					Upload a BIRT report or create a SQL only report
				</dd>
			</sec:ifAnyGranted>
       </dl> --%>
        
      </div>
    </div>
  </body>
</html>
