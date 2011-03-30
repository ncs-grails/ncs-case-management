
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
        <title>Report List</title>
        <g:javascript src="reports.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">Create Report</g:link></span>
			<span class="filter-list"><input type="text" name="filter" value="" id="filter" /></span>
        </div>
        <div class="body">
            <h1>Report List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table id="list-table" class="tablesorter">
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="title" title="${message(code: 'report.title.label', default: 'Title')}" />
                        
                            <g:sortableColumn property="enabled" title="${message(code: 'report.enabled.label', default: 'Enabled')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'report.name.label', default: 'Designed Name')}" />
                        
                            <g:sortableColumn property="useQuery" title="${message(code: 'report.useQuery.label', default: 'SQL Only')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'report.dateCreated.label', default: 'Date Created')}" />
                        
                            <g:sortableColumn property="userCreated" title="${message(code: 'report.userCreated.label', default: 'Created By')}" />
                        
                            <g:sortableColumn property="dateUpdated" title="${message(code: 'report.dateUpdated.label', default: 'Date Updated')}" />
                        
                            <g:sortableColumn property="userUpdated" title="${message(code: 'report.userUpdated.label', default: 'Updated By')}" />

							<th>Action</th>                       

							<th>Export</th>                       
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${reportInstanceList}" status="i" var="reportInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="one-line"><g:link action="show" id="${reportInstance.id}">${fieldValue(bean: reportInstance, field: "title")}</g:link></td>
                        
                            <td><g:if test="${reportInstance.enabled}">Yes</g:if><g:else>No</g:else></td>
                        
                            <td><g:if test="${reportInstance.useQuery}">NA</g:if><g:else>${fieldValue(bean: reportInstance, field: "designedName")}</g:else></td>
                        
                            <td><g:if test="${reportInstance.useQuery}">Yes</g:if><g:else>No</g:else></td>
                        
                            <td><g:formatDate date="${reportInstance.dateCreated} format="MM/dd/yyyy h:mm a" /></td>
                        
                            <td>${fieldValue(bean: reportInstance, field: "userCreated")}</td>
                        
                            <td><g:formatDate date="${reportInstance.dateUpdated} format="MM/dd/yyyy h:mm a" /></td>
                        
                            <td>${fieldValue(bean: reportInstance, field: "userUpdated")}</td>
							
							<td>
					            <g:link action="showReport" id="${reportInstance.id}" target="_blank">View</g:link>
							</td>
							
                            <td>
                            	<span class="actions">
                            		<g:if test="${reportInstance.useQuery}">
                            			<g:pdfLink pdfController="localNcsPdf" pdfAction="exportReportByQueryToPdf" pdfId="${reportInstance.id}" filename="${reportInstance?.title.replaceAll(' ','_') + '.pdf'}" icon="true"></g:pdfLink> <g:link action="exportReportByQueryToFile" id="${reportInstance.id}" params="[format:'csv']"><img src="${resource(dir:'images',file:'excel_16x16.png')}" class="export-img" title="CSV" alt="CSV" /></g:link>                            			
                            		</g:if>
                            		<g:else>
                            			<g:link action="exportBirtReport" id="${reportInstance.id}" params="[format:'pdf']"><img src="${resource(dir:'images',file:'pdf_button.png')}" class="export-img" title="PDF" alt="PDF" /></g:link> <g:link action="exportBirtReport" id="${reportInstance.id}" params="[format:'xls']"><img src="${resource(dir:'images',file:'excel_16x16.png')}" class="export-img" title="Excel" alt="Excel" /></g:link>
                            		</g:else>
                            	</span>
                            </td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${reportInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
