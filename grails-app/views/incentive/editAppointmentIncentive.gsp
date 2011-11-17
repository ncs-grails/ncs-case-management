<%@ page import="edu.umn.ncs.Incentive" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'incentive.label', default: 'Incentive')}" />
        <title>Edit Appointment Incentive</title>
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'incentive.css')}" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="edit" controller="appointment" action="edit" id="${appointmentIncentiveInstance?.appointment?.id}">
            	Back To Appointment</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Appointment Incentive for <g:link controller="person" action="show" id="${appointmentIncentiveInstance?.appointment?.person?.id}">${appointmentIncentiveInstance?.appointment?.person}</g:link></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${incentiveInstance}">
            <div class="errors">
                <g:renderErrors bean="${incentiveInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${incentiveInstance?.id}" />
                <g:hiddenField name="version" value="${incentiveInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="incentive.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'type', 'errors')}">
                                    <g:select name="type.id" from="${edu.umn.ncs.IncentiveType.list()}" optionKey="id" optionValue="name" value="${incentiveInstance?.type?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="amount"><g:message code="incentive.amount.label" default="Amount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'amount', 'errors')}">
                                    <g:textField name="amount" value="${g.formatNumber(number:incentiveInstance.amount, minFractionDigits:2)}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="accepted"><g:message code="incentive.accepted.label" default="Accepted" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'accepted', 'errors')}">
                                    <g:checkBox name="accepted" value="${incentiveInstance?.accepted}" />
                                </td>
                            </tr>
                                                
                            <%--<tr class="prop">
                                <td valign="top" class="name">
                                  <label for="checkGenerated"><g:message code="incentive.checkGenerated.label" default="Check Generated" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'checkGenerated', 'errors')}">
                                    <g:checkBox name="checkGenerated" value="${incentiveInstance?.checkGenerated}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="checkNumber"><g:message code="incentive.checkNumber.label" default="Check Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'checkNumber', 'errors')}">
                                    <g:textField name="checkNumber" value="${fieldValue(bean: incentiveInstance, field: 'checkNumber')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="paymentStarted"><g:message code="incentive.paymentStarted.label" default="Payment Started" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'paymentStarted', 'errors')}">
                                    <g:checkBox name="paymentStarted" value="${incentiveInstance?.paymentStarted}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="pva"><g:message code="incentive.pva.label" default="Pva" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'pva', 'errors')}">
                                    <g:textField name="pva" value="${incentiveInstance?.pva}" />
                                </td>
                            </tr> --%>
                                                    
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
                    <span class="button"><g:actionSubmit class="save" action="updateAppointmentIncentive" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
