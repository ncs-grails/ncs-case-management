
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
        <title>Show Report</title>
        <g:javascript src="reports.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
			<span class="filter-list"><input type="text" name="filter" value="" id="filter" /></span>
        </div>
        <div class="body">
        	<g:if test="${reportInstance?.underConstruction}"><span class="under-construction"><img src="${resource(dir:'images',file:'under_construction_icon-red_50x42.png')}" class="under-construction-img" title="Under construction" alt="Under construction" />  UNDER CONSTRUCTION</span></g:if> 
            <h1>${reportInstance?.study?.name} ${reportInstance?.title} Report <g:link action="exportReportByQueryToFile" id="${reportInstance.id}" params="[format:'csv']"><img src="${resource(dir:'images',file:'csv_17x16.gif')}" class="export-img" title="CSV" alt="CSV" /></g:link></h1>
            <h3>${reportInstance?.subtitle}</h3>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
            <g:ifAnyGranted role="ROLE_NCS_IT">
	            <div class="buttons">
	                <g:form>
	                    <g:hiddenField name="id" value="${reportInstance?.id}" />
	                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	                </g:form>
	            </div>
			</g:ifAnyGranted>
			
            <div class="dialog">
                <table id="list-table" class="tablesorter">
                    <thead>
                        <tr>
                        	<g:each var="headerInstance" in="${headerList}" >
								<th>${headerInstance}</th>
							</g:each>
                        </tr>
                    </thead>
                    <tbody class="zebra">
                    <g:each in="${recordList}" status="i" var="recordInstance">
                        <tr>
                        	<g:each var="headerInstance" in="${headerList}" >
                        		
								<td>
									<g:if test="${headerInstance.toLowerCase().contains('date')}">
										<g:if test="${headerInstance.toLowerCase().contains('updated_by')}">
											${recordInstance[headerInstance]}
										</g:if>
										<g:else><g:formatDate date="${recordInstance[headerInstance]}" format="MM/dd/yyyy" /></g:else>
									</g:if>
									<g:else>${recordInstance[headerInstance]}</g:else>
								</td>
							</g:each>                        
                        </tr>
                    </g:each>
                    
                    </tbody>
                </table>
            </div>
            <g:ifAnyGranted role="ROLE_NCS_IT">
	            <div class="buttons">
	                <g:form>
	                    <g:hiddenField name="id" value="${reportInstance?.id}" />
	                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	                </g:form>
	            </div>
            </g:ifAnyGranted>
        </div>
    </body>
</html>
