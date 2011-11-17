<%@ page import="edu.umn.ncs.DataSetType" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'dataSetType.label', default: 'DataSetType')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
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
<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
</g:if> <g:hasErrors bean="${dataSetTypeInstance}">
	<div class="errors"><g:renderErrors bean="${dataSetTypeInstance}"
		as="list" /></div>
</g:hasErrors> <g:form method="post" >
	<g:hiddenField name="id" value="${dataSetTypeInstance?.id}" />
	<g:hiddenField name="version" value="${dataSetTypeInstance?.version}" />
	<div class="dialog">
	
		<div class="prop">
			<span class="name">
			  <label for="name"><g:message code="dataSetType.name.label" default="Name" /></label>
			</span>
			<span class="value ${hasErrors(bean: dataSetTypeInstance, field: 'name', 'errors')}">
				<g:textField name="name" value="${dataSetTypeInstance?.name}" />
			</span>
		</div>
	
		<div class="prop">
			<span class="name">
			  <label for="code"><g:message code="dataSetType.code.label" default="Code" /></label>
			</span>
			<span class="value ${hasErrors(bean: dataSetTypeInstance, field: 'code', 'errors')}">
				<g:textField name="code" maxlength="16" value="${dataSetTypeInstance?.code}" />
			</span>
		</div>
	
		<div class="prop">
			<span class="name">
			  <label for="closure"><g:message code="dataSetType.closure.label" default="Closure" /></label>
			</span>
			<div class="prop-description">
			  <p>this is the closure containing the groovy code that adds the required fields to the dataset. It MUST take a dataSet as a parameter, and return the altered dataset. If this is not provided, the closure or equivalent must exist in the respective service that builds the data source.</p>
			  <p>
			  <strong>Example:</strong> 
			  <pre>{ dataSet -&gt;
  dataSet.collect{record -&gt;
    def trackedItemInstance = TrackedItem.read(record.itemId)
    def personInstance = trackedItemInstance.person
    record.newField = "my new data"
  }
  dataSet
}
</pre>
			  </div>
			  </p>
			<span class="value ${hasErrors(bean: dataSetTypeInstance, field: 'closure', 'errors')}">
				<g:textArea name="closure" cols="120" rows="12" value="${dataSetTypeInstance?.closure}" style="font-family: monospace;" />
			</span>
		</div>
		<div class="prop">
			<span class="name">
			  <label for="closureTested"><g:message code="dataSetType.closureTested.label" default="Closure Tested?" /></label>
			  <g:formatBoolean true="Yes" false="No" boolean="${dataSetTypeInstance?.closureTested}" />
			</span>
		</div>
	
		<div class="prop">
			<span class="name">
			  <label for="sqlQuery"><g:message code="dataSetType.sqlQuery.label" default="Sql Query" /></label>
			</span>
			<div class="prop-description">
			  <p>This is the field containing the sql query
			   that adds the required fields to the dataset. It MUST take
				:batchId as a parameter, and return the tracked_item as well as additional fields.</p>
				<p>Example:
				<pre>SELECT ti.id AS tracked_item, b.app_created
FROM batch b INNER JOIN
	tracked_item ti ON b.id = ti.batch_id
WHERE (b.id = :batchId)
				</pre>
				Adds a column named "app_created" to the dataset.</p>
			</div>
			<span class="value ${hasErrors(bean: dataSetTypeInstance, field: 'sqlQuery', 'errors')}">
				<g:textArea name="sqlQuery" cols="120" rows="12" value="${dataSetTypeInstance?.sqlQuery}" style="font-family: monospace;" />
			</span>
		</div>
		<div class="prop">
			<span class="name">
			  <label for="sqlQueryTested"><g:message code="dataSetType.sqlQueryTested.label" default="Sql Query Tested?" /></label>
			  <g:formatBoolean true="Yes" false="No" boolean="${dataSetTypeInstance?.sqlQueryTested}" />
			</span>
		</div>
                        
	</div>

	<fieldset class="maroonBorder">
		<legend>Used by Documents:</legend>
		<ul>
			<g:each var="d" in="${batchCreationDocumentInstanceList}">
			<li><g:link controller="batchCreationConfig" action="edit" id="${d.batchCreationConfig.id}">${d}</g:link></li>
			</g:each>
		</ul>
	</fieldset>

	<div class="buttons"><span class="button"><g:actionSubmit
		class="save" action="update"
		value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
	<span class="button"><g:actionSubmit class="delete"
		action="delete"
		value="${message(code: 'default.button.delete.label', default: 'Delete')}"
		onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	</div>
</g:form></div>
</body>
</html>
