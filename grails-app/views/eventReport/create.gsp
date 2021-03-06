

<%@ page import="edu.umn.ncs.EventReport" %>
<%@ page import="edu.umn.ncs.EventOfInterest" %>
<%@ page import="edu.umn.ncs.Study" %>
<html>
    <head>
        <%--<gui:resources components="['tabView','datePicker']"/> --%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'eventReport.label', default: 'EventReport')}" />
        <title>Event of Interest Report</title>
        <%--<g:javascript library="prototype" /> --%>
		<g:javascript src="ncs-event.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="index" params="${ ['person.id':eventReportInstance?.person?.id] }">
            	Event Report List
            </g:link></span>
        </div>
        <div class="body">
        	
            <h1>New Event of Interest Report for ${eventReportInstance?.person}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${eventReportInstance}">
            <div class="errors">
                <g:renderErrors bean="${eventReportInstance}" as="list" />
            </div>
            </g:hasErrors>
                        
            <g:form action="save" >
                <div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	            	<g:hiddenField name="person.id" value="${eventReportInstance?.person?.id}" />
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="eventSource"><g:message code="eventReport.eventSource.label" default="Event Source" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventReportInstance, field: 'eventSource', 'errors')}">
                                    <g:select name="eventSource.id" from="${edu.umn.ncs.EventSource.list()}" optionKey="id" value="${eventReportInstance?.eventSource?.id}"  />
					                <span id="eventSourceOther">
					                	<label for="eventSourceOther"><g:message code="eventReport.eventSourceOther.label" default="Explain Other:" /></label>
					                	<g:textField name="eventSourceOther" value="${eventReportInstance?.eventSourceOther}" />
				               		</span>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="contactDate"><g:message code="eventReport.contactDate.label" default="Contact Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventReportInstance, field: 'contactDate', 'errors')}">
                                    <%--<g:datePicker name="contactDate" precision="day" value="${eventReportInstance?.contactDate}"  /> --%>
                                    <%-- <gui:datePicker
                                    	id="contactDate"
                                    	formatString="MM/dd/yyyy" />--%>
                                    <input size="12" id="datepicker" name="contactDate" type="text" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="filledOutBy"><g:message code="eventReport.filledOutBy.label" default="Filled Out By" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventReportInstance, field: 'filledOutBy', 'errors')}">
                                    <g:textField name="filledOutBy" value="${eventReportInstance?.filledOutBy}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label size="12" for="filledOutDate"><g:message code="eventReport.filledOutDate.label" default="Filled Out Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: eventReportInstance, field: 'filledOutDate', 'errors')}">
                                    <%--<g:datePicker name="filledOutDate" precision="day" value="${eventReportInstance?.filledOutDate}"  /> --%>
                                    <%--<gui:datePicker
                                    	id="filledOutDate"
                                    	formatString="MM/dd/yyyy" /> --%>
                                    <input size="12" id="filledOutDatepicker" name="filledOutDate" type="text" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
				            
                <div class="buttons">
                    <span class="button"><g:submitButton name="Create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /> Create report and then enter events of interest...</span>
                </div>
            </g:form>
        </div>
    </body>
</html>
