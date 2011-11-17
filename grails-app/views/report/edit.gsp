<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript src="reports.js" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'reports.css')}" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportInstance}" as="list" />
            </div>
            </g:hasErrors>
            <div class="info-box hidden"></div>

            <g:form method="post" enctype="multipart/form-data" onsubmit="return validateReportEntry();" >
                <g:hiddenField name="id" value="${reportInstance?.id}" />
                <g:hiddenField name="version" value="${reportInstance?.version}" />
                <g:hiddenField name="userUpdated" value="${loggedInUserInfo(field:'username')}" />
                <g:set var="reportList" value="${reportList}" />
                <g:hiddenField name="reportList" value="${reportList}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                  <label for="title"><g:message code="report.title.label" default="Title" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'title', 'errors')}">
                                    <g:textField name="title" value="${reportInstance?.title}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                  <label for="subtitle"><g:message code="report.subtitle.label" default="Subtitle" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'subtitle', 'errors')}">
                                    <g:textField name="subtitle" value="${reportInstance?.subtitle}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                  <label for="description"><g:message code="report.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" cols="40" rows="5" value="${reportInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr id="designedReport" class="prop label-column">
                                <td valign="top" class="name">
                                    <label for="designedName"><g:message code="report.designedName.label" default="Designed Report Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'designedName', 'errors')}">
					                <g:hiddenField name="designedName" value="${reportInstance?.designedName}" />
                                	${reportInstance?.designedName}<br /><br />
									Change <input type="file" id="reportFile" name="reportFile" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                    <label for="useQuery"><g:message code="report.useQuery.label" default="Use Query" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'useQuery', 'errors')}">
                                    <g:checkBox name="useQuery" value="${reportInstance?.useQuery}" />
                                </td>
                            </tr>
                        
                            <tr id="trQuery" class="prop">
                                <td valign="top" class="name label-column">
                                    <label for="query"><g:message code="report.query.label" default="Query" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'query', 'errors')}">
                                    <g:textArea name="query" cols="75" rows="8" value="${reportInstance?.query}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                    <label for="underConstruction"><g:message code="report.underConstruction.label" default="Under Construction" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'underConstruction', 'errors')}">
                                    <g:checkBox name="underConstruction" value="${reportInstance?.underConstruction}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                  <label for="enabled"><g:message code="report.enabled.label" default="Enabled" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'enabled', 'errors')}">
                                    <g:checkBox name="enabled" value="${reportInstance?.enabled}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                  <label for="adminsOnly"><g:message code="report.adminsOnly.label" default="Admins Only" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'adminsOnly', 'errors')}">
                                    <g:checkBox name="adminsOnly" value="${reportInstance?.adminsOnly}" />
                                </td>
                            </tr>
                        
                            <%--<tr class="prop">
                                <td valign="top" class="name label-column">
                                  <label for="reportParams"><g:message code="report.reportParams.label" default="Report Parameters" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'reportParams', 'errors')}">
                                    
									<ul>
									<g:each in="${reportInstance?.reportParams?}" var="r">
									    <li>[${r?.name}] ${r?.value}</li>
									</g:each>
									</ul>

                                </td>
                            </tr> --%>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:link action="show" id="${reportInstance?.id}"><span class="cancel">Cancel</span></g:link>
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
