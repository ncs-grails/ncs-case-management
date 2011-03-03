

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'report.label', default: 'Report')}" />
        <title>Report Parameters</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>${reportInstance.title}</h1>
            <h3>Please select parameter values:</h3>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportInstance}" as="list" />
            </div>
            </g:hasErrors>
            <div class="info-box hidden"></div>

            <g:form action="showBirtReport" >
                <g:hiddenField name="id" value="${reportInstance.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        	<g:each var="reportParamInstance" in="${reportParams}" >
	                            <tr class="prop">
	                                <td valign="top" class="name label-column">
	                                    <label for="promptText">${reportParamInstance?.promptText}</label>
	                                </td>
	                                <td valign="top" class="value">
	                                    <g:select
	                                   		name="${reportParamInstance.name}"
	                                   		from="${reportParamInstance.listEntries}"
	                                   		optionValue="label"
	                                   		optionKey="value"
	                                   		value="" />
	                                </td>
	                            </tr>
							</g:each>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="showBirtReport" class="list" value="View Report" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
