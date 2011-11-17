<html>
    <head>
		<g:javascript library="prototype" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="ncs" />
		<g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
		<title>Report Parameters</title>
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'reports.css')}" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}#report-tab"><g:message code="default.list.label" args="[entityName]" /></a></span>
        </div>
        <div class="body">
            <h1>${reportInstance.title}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportInstance}" as="list" />
            </div>
            </g:hasErrors>
            <div id="exportMessage" class="info-box hidden"></div>

            <g:if test="${format}">
	            <g:form action="exportBirtReportToFile">
	                <g:hiddenField name="id" value="${reportInstance.id}" />
	                <g:hiddenField name="format" value="${format}" />
	                <div class="dialog">
                       	<g:each var="reportParamInstance" in="${reportParams}" >
		                	<div class="parameter-row">
								<div class="parameter-box lefty">
		                            <span>${reportParamInstance?.promptText}...</span>
								</div>	                	
								<div class="parameter-box lefty">
	                                <g:select
	                               		name="${reportParamInstance.name}"
	                               		from="${reportParamInstance.listEntries}"
	                               		optionValue="label"
	                               		optionKey="value"
	                               		value="" />
								</div>	                	
		                	</div>
						</g:each>
	                </div>
	                <div class="buttons full-width lefty">
	                	<g:if test="${format=='pdf'}">
	                    	<span class="button"><g:submitButton name="exportBirtReportToFile" class="list" value="View PDF" /></span>
						</g:if>
	                	<g:if test="${format=='xls'}">
		                    <span class="button"><g:submitButton name="exportBirtReportToFile" class="list" value="Excel" /></span>
						</g:if>
	                </div>
	            </g:form>
	        </g:if>
            <g:else>
	            <g:form action="showBirtReport">
	                <g:hiddenField name="id" value="${reportInstance.id}" />
	                <div class="dialog">
                       	<g:each var="reportParamInstance" in="${reportParams}" >
		                	<div class="parameter-row">
								<div class="parameter-box lefty">
		                            <span>${reportParamInstance?.promptText}...</span>
								</div>	                	
								<div class="parameter-box lefty">
	                                <g:select
	                               		name="${reportParamInstance.name}"
	                               		from="${reportParamInstance.listEntries}"
	                               		optionValue="label"
	                               		optionKey="value"
	                               		value="" />
								</div>	                	
		                	</div>
						</g:each>
	                </div>
	                <div class="buttons full-width lefty">
	                    <span class="button"><g:submitButton name="showBirtReport" class="list" value="View Report" /></span>
	                </div>
	            </g:form>
	        </g:else>	        
        </div>
    </body>
</html>
