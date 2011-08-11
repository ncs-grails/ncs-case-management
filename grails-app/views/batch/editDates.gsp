

<%@ page import="edu.umn.ncs.Batch" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batch.label', default: 'Batch')}" />
        <title><g:message code="default.editDates.label" args="[entityName]" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'global.css')}" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="listByDate" params="${ [ referenceDate:'date.struct', referenceDate_month: referenceDateMonth, referenceDate_year: referenceDateYear ] }"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Editing Batch # ${batchInstance?.id}</h1>

            <h3>${referenceDateYear}</h3>

            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${batchInstance}">
            <div class="errors">
                <g:renderErrors bean="${batchInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${batchInstance?.id}" />
                <g:hiddenField name="version" value="${batchInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="batchId"><g:message code="batch.id.label" default="Batch ID" /></label>
                                </td>
                                <td valign="top" class="value">
                                    ${batchInstance?.id}
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="instrument"><g:message code="batch.instrument.label" default="Instrument" /></label>
                                </td>
                                <td valign="top" class="value">
                                    ${batchInstance?.primaryInstrument}
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="batchDateCreated"><g:message code="batch.dateCreated.label" default="Date Created" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:formatDate date="${batchInstance?.dateCreated}" format="M/d/yyyy"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="pieces"><g:message code="batch.pieces.label" default="Pieces" /></label>
                                </td>
                                <td valign="top" class="value">
                                    ${batchInstance?.pieces}
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="runBy"><g:message code="batch.runBy.label" default="Run By" /></label>
                                </td>
                                <td valign="top" class="value">
                                    ${batchInstance?.batchRunBy}
                                </td>
                            </tr>


                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="instrumentDate"><g:message code="batch.instrumentDate.label" default="Instrument Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'instrumentDate', 'errors')}">
                                    <g:formatDate id="instrumentDate" date="${batchInstance?.instrumentDate}" format="M/d/yyyy" />
                                </td>
                            </tr>


                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="calledCampusCourierDate"><g:message code="batch.calledCampusCourierDate.label" default="Campus Courier Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'calledCampusCourierDate', 'errors')}">
                                    <g:datePicker name="calledCampusCourierDate" precision="day" value="${batchInstance?.calledCampusCourierDate}" default="none" noSelection="['null': '']" years="${yearRange}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="addressAndMailingDate"><g:message code="batch.addressAndMailingDate.label" default="Address And Mailing Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'addressAndMailingDate', 'errors')}">
                                    <g:datePicker name="addressAndMailingDate" precision="day" value="${batchInstance?.addressAndMailingDate}" default="none" noSelection="['null': '']" years="${yearRange}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="mailDate"><g:message code="batch.mailDate.label" default="Mail Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'mailDate', 'errors')}">
                                    <g:datePicker name="mailDate" precision="day" value="${batchInstance?.mailDate}" default="none" noSelection="['null': '']" years="${yearRange}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="trackingReturnDate"><g:message code="batch.trackingReturnDate.label" default="Tracking Return Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'trackingReturnDate', 'errors')}">
                                    <g:datePicker name="trackingReturnDate" precision="day" value="${batchInstance?.trackingReturnDate}" default="none" noSelection="['null': '']" years="${yearRange}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="updateDates" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
