

<%@ page import="edu.umn.ncs.DataSetType" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'dataSetType.label', default: 'DataSetType')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span> <span class="menuButton"><g:link class="create" action="create">
	<g:message code="default.new.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.show.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if>
<div class="dialog">

                        <div class="prop">
                            <span class="name"><g:message code="dataSetType.id.label" default="Id" /></span>
                            
                            <span class="value">${fieldValue(bean: dataSetTypeInstance, field: "id")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="dataSetType.name.label" default="Name" /></span>
                            
                            <span class="value">${fieldValue(bean: dataSetTypeInstance, field: "name")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="dataSetType.code.label" default="Code" /></span>
                            
                            <span class="value">${fieldValue(bean: dataSetTypeInstance, field: "code")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="dataSetType.closure.label" default="Closure" /></span>
                            
                            <span class="value">${fieldValue(bean: dataSetTypeInstance, field: "closure")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="dataSetType.sqlQuery.label" default="Sql Query" /></span>
                            
                            <span class="value">${fieldValue(bean: dataSetTypeInstance, field: "sqlQuery")}</span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="dataSetType.closureTested.label" default="Closure Tested" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${dataSetTypeInstance?.closureTested}" /></span>
                            
                        </div>
                    
                        <div class="prop">
                            <span class="name"><g:message code="dataSetType.sqlQueryTested.label" default="Sql Query Tested" /></span>
                            
                            <span class="value"><g:formatBoolean boolean="${dataSetTypeInstance?.sqlQueryTested}" /></span>
                            
                        </div>
                    
</div>
<div class="buttons"><g:form>
	<g:hiddenField name="id" value="${dataSetTypeInstance?.id}" />
	<span class="button"><g:actionSubmit class="edit" action="edit"
		value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	<span class="button"><g:actionSubmit class="delete"
		action="delete"
		value="${message(code: 'default.button.delete.label', default: 'Delete')}"
		onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
</g:form></div>
</div>
</body>
</html>
