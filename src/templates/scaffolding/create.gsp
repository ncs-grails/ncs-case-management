
<% import grails.persistence.Event %>
<% import org.codehaus.groovy.grails.plugins.PluginManagerHolder %>
<%=packageName%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
<title><g:message code="default.create.label"
	args="[entityName]" /></title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="\${createLink(uri: '/')}"><g:message
	code="default.home.label" /></a></span> <span class="menuButton"><g:link
	class="list" action="list">
	<g:message code="default.list.label" args="[entityName]" />
</g:link></span></div>
<div class="body">
<h1><g:message code="default.create.label" args="[entityName]" /></h1>
<g:if test="\${flash.message}">
	<div class="message">\${flash.message}</div>
</g:if> <g:hasErrors bean="\${${propertyName}}">
	<div class="errors"><g:renderErrors bean="\${${propertyName}}"
		as="list" /></div>
</g:hasErrors> <g:form action="save" method="post"
	<%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
	<div class="dialog">
	<%  excludedProps = Event.allEvents.toList() << 'version' << 'id' << 'dateCreated' << 'lastUpdated'
                            props = domainClass.properties.findAll { !excludedProps.contains(it.name) }
                            Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                            display = true
                            boolean hasHibernate = PluginManagerHolder.pluginManager.hasGrailsPlugin('hibernate')
                            props.each { p ->
                                if (!Collection.class.isAssignableFrom(p.type)) {
                                    if (hasHibernate) {
                                        cp = domainClass.constrainedProperties[p.name]
                                        display = (cp ? cp.display : true)
                                    }
                                    if (display) { %>
                            <div class="prop">
                                <span class="name">
                                    <label for="${p.name}"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></label>
                                </span>
                                <span class="value \${hasErrors(bean: ${propertyName}, field: '${p.name}', 'errors')}">
                                    ${renderEditor(p)}
                                </span>
                            </div>
                        <%  }   }   } %>
	</div>
	<div class="buttons"><span class="button"><g:submitButton
		name="create" class="save"
		value="\${message(code: 'default.button.create.label', default: 'Create')}" /></span>
	</div>
</g:form></div>
</body>
</html>
