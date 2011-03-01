

<%@ page import="edu.umn.ncs.Study" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
        <title>Create a Report</title>
        <g:javascript src="reports.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Create a Report</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportInstance}" as="list" />
            </div>
            </g:hasErrors>
            <div class="info-box hidden"></div>

            <g:uploadForm action="save" >
                <g:hiddenField name="userCreated" value="${loggedInUserInfo(field:'username')}" />
                <g:hiddenField name="userUpdated" value="${loggedInUserInfo(field:'username')}" />
                <g:set var="reportList" value="${reportList}" />
                <g:hiddenField name="reportList" value="${reportList}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                    <label for="study"><g:message code="report.study.label" default="Study" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'study', 'errors')}">
                                    <g:select
                                   		name="study.id"
                                   		from="${Study.list()}"
                                   		optionKey="id"
                                   		value="${reportInstance?.study}" />
                                </td>
                            </tr>
                        
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
                                    <g:textField class="bigTextField" name="subtitle" value="${reportInstance?.subtitle}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name label-column">
                                    <label for="description"><g:message code="report.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'description', 'errors')}">
                                    <g:textArea name="description" cols="75" rows="3" value="${reportInstance?.description}" />
                                </td>
                            </tr>
                        
                            <%-- <tr id="designedReport" class="prop">
                                <td valign="top" class="name">
                                    <label for="designedName"><g:message code="report.designedName.label" default="Designed Report Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportInstance, field: 'designedName', 'errors')}">
                                    <g:select
                                   		name="designedName"
                                   		from="${reportList}"
                                   		value="${reportInstance?.designedName}" />
                                </td>
                            </tr> --%>
                        
                            <tr id="designedReport" class="prop">
                                <td valign="top" class="name label-column">
                                    <label for="designedName"><g:message code="report.designedName.label" default="Upload Designed Report" /></label>
                                </td>
                                <td valign="top" class="value">                
									<input type="file" id="reportFile" name="reportFile" />
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
                        
                        	<g:each var="reportParamInstance" in="${reportParamInstanceList}" status="i" >
	                            <%-- <tr class="prop label-column <g:if test='${i > 0}'>hidden</g:if>">--%>
	                            <tr class="prop label-column hidden">
	                            	<g:if test='${i > 0}'><td></td></g:if>
	                            	<g:else>
		                                <td valign="top" class="name">
		                                    <label for="reportParams"><g:message code="report.reportParams.label" default="Report Parameters" /></label>
		                                </td>
	                                </g:else>
	                                <td valign="top" class="value">
	                                    Name <g:textField name="${'reportParams' + i + '.name'}" value="" />
	                                    Value <g:textField name="${'reportParams' + i + '.value'}" value="" />
	                                </td>
	                            </tr>
							</g:each>                 
							
							<%--<tr>
								<td></td>
								<td><input type="button" value="Add Parameter" /></td>
							</tr>  --%>       
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" onclick="return validateReportEntry();" /></span>
                </div>
            </g:uploadForm>
        </div>
    </body>
</html>
