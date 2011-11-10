<%@ page import="edu.umn.ncs.Incentive" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'incentive.label', default: 'Incentive')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
	    	<span class="menuButton"><g:link class="list" action="list">Incentive List</g:link></span>
            <g:if test="${appointmentIncentiveInstance}">
            	<span class="menuButton"><g:link class="edit" controller="appointment" action="edit" id="${appointmentIncentiveInstance?.appointment?.id}">
            	Back To Appointment</g:link></span>
            </g:if>
	    	<span class="menuButton"><g:link class="list" action="transactionLog" id="${incentiveInstance?.id}" >Transaction Log</g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
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
                                  <label for="trackedItem"><g:message code="incentive.trackedItem.label" default="Tracked Item" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'trackedItem', 'errors')}">
                                    <g:if test="${incentiveInstance?.trackedItem}">
                                    	<g:link controller="trackedItem" action="show" id="${incentiveInstance?.trackedItem?.id}" >${incentiveInstance?.trackedItem?.batch?.primaryInstrument}</g:link>
                                    </g:if>
                                    <g:else>None</g:else>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="incentive.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'type', 'errors')}">
                                    <g:select name="type.id" from="${edu.umn.ncs.IncentiveType.list()}" optionKey="id" optionValue="name" value="${incentiveInstance?.type?.id}"  />
                                </td>
                            </tr>

                            <g:if test="${incentiveInstance?.barcode}">
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                  <label for="barcode"><g:message code="incentive.barcode.label" default="Barcode" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'barcode', 'errors')}">
	                                    ${incentiveInstance?.barcode}
	                                </td>
	                            </tr>
							</g:if>
                        
                            <g:if test="${incentiveInstance?.receiptNumber}">
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                  <label for="receiptNumber"><g:message code="incentive.barcode.label" default="Receipt Number" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'receiptNumber', 'errors')}">
	                                    ${incentiveInstance?.receiptNumber}
	                                </td>
	                            </tr>
							</g:if>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="amount"><g:message code="incentive.amount.label" default="Amount" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'amount', 'errors')}">
                                    <g:textField name="amount" value="${g.formatNumber(number:incentiveInstance.amount, minFractionDigits:2)}" />
                                </td>
                            </tr>

                            <g:if test="${incentiveInstance?.trackedItem}">
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                  <label for="accepted"><g:message code="incentive.accepted.label" default="Accepted" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'accepted', 'errors')}">
	                                    <g:checkBox name="accepted" value="${incentiveInstance?.accepted}" />
	                                </td>
	                            </tr>
	
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                  <label for="incentiveDate"><g:message code="incentive.incentiveDate.label" default="Date Distributed" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'incentiveDate', 'errors')}">
	                                    <g:datePicker name="incentiveDate" value="${incentiveInstance?.incentiveDate}" precision="minute" />
	                                </td>
	                            </tr>
							</g:if>
							
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="checkedOut"><g:message code="incentive.checkedOut.label" default="Checked Out" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'checkedOut', 'errors')}">
                                    <g:checkBox name="checkedOut" value="${incentiveInstance?.checkedOut}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="checkedOutToWhom"><g:message code="incentive.checkedOutToWhom.label" default="Checked Out To" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'checkedOutToWhom', 'errors')}">
                                    <g:textField name="checkedOutToWhom" value="${fieldValue(bean: incentiveInstance, field: 'checkedOutToWhom')}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dateCheckedOut"><g:message code="incentive.dateCheckedOut.label" default="Date Checked Out" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'dateCheckedOut', 'errors')}">
                                    <g:datePicker name="dateCheckedOut" value="${incentiveInstance?.dateCheckedOut}" precision="minute" />
                                </td>
                            </tr>
							
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="activated"><g:message code="incentive.activated.label" default="Activated" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'activated', 'errors')}">
                                    <g:checkBox name="activated" value="${incentiveInstance?.activated}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dateActivated"><g:message code="incentive.dateActivated.label" default="Date Activated" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'dateActivated', 'errors')}">
                                    <g:datePicker name="dateActivated" value="${incentiveInstance?.dateActivated}" precision="minute" />
                                </td>
                            </tr>
                                                
                            <tr class="prop">
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
                        
                            <%--<tr class="prop">
                                <td valign="top" class="name">
                                  <label for="pva"><g:message code="incentive.pva.label" default="Pva" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: incentiveInstance, field: 'pva', 'errors')}">
                                    <g:textField name="pva" value="${incentiveInstance?.pva}" />
                                </td>
                            </tr>  --%>
                                                    
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
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <%--<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span> --%>
                </div>
            </g:form>
        </div>
    </body>
</html>
