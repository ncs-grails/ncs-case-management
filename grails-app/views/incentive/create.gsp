<%@ page import="edu.umn.ncs.Incentive" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'incentive.label', default: 'Incentive')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="edit" controller="appointment" action="edit" id="${incentiveInstance?.appointment?.id}">
            	Back To Appointment</g:link></span>
        </div>
        <div class="body">
            <h1>Incentive for ${incentiveInstance?.appointment?.type}</h1>
            <h2>
            	<g:formatDate date="${incentiveInstance?.appointment?.startTime}" format="MM/dd/yyyy hh:mm a" />, 
            	${incentiveInstance?.appointment?.person}
            	${incentiveInstance?.appointment?.dwellingUnit}
           	</h2>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${incentiveInstance}">
	            <div class="errors">
	                <g:renderErrors bean="${incentiveInstance}" as="list" />
	            </div>
            </g:hasErrors>
            <g:form action="save" >
            	<g:hiddenField name="appointment.id" value="${incentiveInstance?.appointment?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="incentive.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'type', 'errors')}">
                                    <g:select name="type.id" from="${edu.umn.ncs.IncentiveType.list()}" optionKey="id" value="${incentiveInstance?.type?.id}"  />
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="amount"><g:message code="incentive.amount.label" default="Amount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'amount', 'errors')}">
                                    <g:textField name="amount" value="${fieldValue(bean: incentiveInstance, field: 'amount')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="comments"><g:message code="incentive.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'comments', 'errors')}">
                                    <g:textArea name="comments" value="${incentiveInstance?.comments}" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
