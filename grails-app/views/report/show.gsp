
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
        <title>Show Report</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
           	<span class="menuButton"><g:link class="list" action="showReport" id="${reportInstance.id}" target="_blank">Show Report</g:link></span>
        </div>
        <div class="body">
            <h1>${reportInstance?.title} Report</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    	<%--
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "id")}</td>
                            
                        </tr> --%>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.title.label" default="Title" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "title")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.subtitle.label" default="Subtitle" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "subtitle")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.description.label" default="Description" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "description")}</td>
                            
                        </tr>
                    
						<g:if test="${reportInstance?.useQuery}">                    	
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="report.query.label" default="Query" /></td>
	                            
	                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "query")}</td>
	                            
	                        </tr>
						</g:if>
						<g:else>						                    	
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="report.designedName.label" default="Designed Report Name" /></td>
	                            
	                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "designedName")}</td>
	                            
	                        </tr>
                    	</g:else>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.enabled.label" default="Enabled" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${reportInstance?.enabled}" /></td>
                            
                        </tr>
                    
                        <%--<tr class="prop">
                            <td valign="top" class="name"><g:message code="report.reportParams.label" default="Report Params" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${reportInstance.reportParams}" var="r">
                                    <li>${r?.name}</li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr> --%>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.dateCreated.label" default="Date Created" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${reportInstance?.dateCreated}" format="MM/dd/yyyy h:mm a" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.userCreated.label" default="Created By" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "userCreated")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.lastUpdated.label" default="Last Updated" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${reportInstance?.lastUpdated}" format="MM/dd/yyyy h:mm a" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="report.userUpdated.label" default="Updated By" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportInstance, field: "userUpdated")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${reportInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
