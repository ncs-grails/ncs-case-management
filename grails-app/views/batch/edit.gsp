

<%@ page import="edu.umn.ncs.Batch" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batch.label', default: 'Batch')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript src="batch.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Editing Batch # ${batchInstance?.id}</h1>
            
            <g:if test="${message}">
            	<div class="message">${message}</div>
            </g:if>            
            
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${batchInstance}">
            <div class="errors">
                <g:renderErrors bean="${batchInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" name="editBatch">
                <g:hiddenField name="id" value="${batchInstance?.id}" />
                <g:hiddenField name="version" value="${batchInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="format"><g:message code="batch.dateCreated.label" default="Date Created" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <label><g:formatDate date="${batchInstance?.dateCreated}" format="M/d/yyyy"/></label>
                                </td>
                            </tr>                        
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="format"><g:message code="batch.format.label" default="Format" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'format', 'errors')}">
                                    <g:select name="format.id" from="${edu.umn.ncs.InstrumentFormat.list()}" optionKey="id" value="${batchInstance?.format?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="direction"><g:message code="batch.direction.label" default="Direction" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'direction', 'errors')}">
                                    <g:select name="direction.id" from="${edu.umn.ncs.BatchDirection.list()}" optionKey="id" value="${batchInstance?.direction?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="master"><g:message code="batch.master.label" default="Master" /></label>
                                </td>
                                <td valign="top" class="value">
                                	<label>Batch Id: ${batchInstance?.id} ${batchInstance?.master?.primaryInstrument?.name?.encodeAsHTML()}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="trackingReturnDate">
                                  	<g:message code="batch.trackingReturnDate.label" default="Tracking Return Date" />
                                  </label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'trackingReturnDate', 'errors')}">
                                    <g:datePicker name="trackingReturnDate" precision="day" value="${batchInstance?.trackingReturnDate}" default="none" noSelection="['': '']" years="${yearRange}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="minimumReturnDate"><g:message code="batch.minimumReturnDate.label" default="Minimum Return Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'minimumReturnDate', 'errors')}">
                                    <g:datePicker name="minimumReturnDate" precision="day" value="${batchInstance?.minimumReturnDate}" default="none" noSelection="['': '']" years="${yearRange}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="instrumentDate"><g:message code="batch.instrumentDate.label" default="Instrument Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'instrumentDate', 'errors')}">
                                    <g:datePicker name="instrumentDate" precision="day" value="${batchInstance?.instrumentDate}" default="none" noSelection="['': '']" years="${yearRange}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="mailDate"><g:message code="batch.mailDate.label" default="Mail Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'mailDate', 'errors')}">
                                    <g:datePicker name="mailDate" precision="day" value="${batchInstance?.mailDate}" default="none" noSelection="['': '']" years="${yearRange}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="addressAndMailingDate"><g:message code="batch.addressAndMailingDate.label" default="Address And Mailing Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'addressAndMailingDate', 'errors')}">
                                    <g:datePicker name="addressAndMailingDate" precision="day" value="${batchInstance?.addressAndMailingDate}" default="none" noSelection="['': '']" years="${yearRange}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="printingServicesDate"><g:message code="batch.printingServicesDate.label" default="Printing Services Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'printingServicesDate', 'errors')}">
                                    <g:datePicker name="printingServicesDate" precision="day" value="${batchInstance?.printingServicesDate}" default="none" noSelection="['': '']" years="${yearRange}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="calledCampusCourierDate"><g:message code="batch.calledCampusCourierDate.label" default="Called Campus Courier Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'printingServicesDate', 'errors')}">
                                    <g:datePicker name="calledCampusCourierDate" precision="day" value="${batchInstance?.calledCampusCourierDate}" default="none" noSelection="['': '']" years="${yearRange}" />
                                </td>
                            </tr>                            
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="batchRunBy"><g:message code="batch.batchRunBy.label" default="Batch Run By" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <label>${batchInstance?.batchRunBy}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="batchRunByWhat"><g:message code="batch.batchRunByWhat.label" default="Batch Run By What" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'batchRunByWhat', 'errors')}">
                                    <label></label>${batchInstance?.batchRunByWhat}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="trackingDocumentSent"><g:message code="batch.trackingDocumentSent.label" default="Tracking Document Sent" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'trackingDocumentSent', 'errors')}">
                                    <g:checkBox name="trackingDocumentSent" value="${batchInstance?.trackingDocumentSent}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="updatedBy"><g:message code="batch.updatedBy.label" default="Updated By" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'updatedBy', 'errors')}">
                                    <label>${batchInstance?.updatedBy}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="creationConfig"><g:message code="batch.creationConfig.label" default="Creation Config" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <%-- <g:select name="creationConfig.id" from="${edu.umn.ncs.BatchCreationConfig.list()}" optionKey="id" value="${batchInstance?.creationConfig?.id}" optionValue="name" noSelection="['null': '']" /> --%>
									<label>${batchInstance?.creationConfig?.name}</label>                                    
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="instruments"><g:message code="batch.instruments.label" default="Instruments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'instruments', 'errors')}">
									<ul class="list">
										<g:each in="${batchInstance?.instruments?}" var="i">
										    <li><label>${i?.instrument?.name?.encodeAsHTML()}</label></li>
										</g:each>
									</ul>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="items"><g:message code="batch.items.label" default="Items" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: batchInstance, field: 'items', 'errors')}">
                                
                                <div class="manageBatch">
 								<table class="scroll">
									<thead>
										<th>Item ID</th>
										<th>Dwelling Unit Id</th>
										<th>Person Id</th>
										<th>Household Id</th>
										<th>Remove</th>
									</thead>
									<tbody>
										<g:each in="${batchInstance.items.sort{it?.id}}" status="s" var="i">
											<tr class="${(s % 2) == 0 ? 'even' : 'odd'}">
												<td><g:link controller="trackedItem" action="edit" id="${i.id}">${i?.id?.encodeAsHTML()}</g:link></td>
												<td><g:link controller="trackedItem" action="show" id="${i.id}">${i.dwellingUnit?.id?.encodeAsHTML()}</g:link></td>
												<td><g:link controller="trackedItem" action="show" id="${i.id}">${i.person?.id?.encodeAsHTML()}</g:link></td>
												<td><g:link controller="trackedItem" action="show" id="${i.id}">${i.household?.id?.encodeAsHTML()}</g:link></td>
									            <td>
									                <div class="buttons">
									                    <span class="button">
									                    	<%-- <g:actionSubmit class="delete" action="deleteItem" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /> --%>
									                    	<g:link class="delete" action="deleteItem" id="${i.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">Delete</g:link>
									                    </span>
									                </div>
									            </td>												
											</tr>
										</g:each>
									</tbody>
							</table>   
							</div>
	
							<div class="prop">
							
								<span class="name">Add New:</span>

								<span class="prop">
									<label for="dwellingUnit.id">Dwelling Unit:</label>
									<g:textField size="10" name="dwellingUnit.id"/>
								</span>
								<span class="prop">
									 Or 
									<label for="person.id">Person:</label>
									<g:textField name="person.id" size="10" />
								</span>
								<span class="value">
									 Or 
									<label for="household.id">Household:</label>
									<g:textField name="household.id" size="10" />
								</span>		
									<span style="margin-left:5px;" class="buttons">
										<g:actionSubmit name="addItemTest" class="create" action="addItem" value="Add Item" onclick="return checkItems(${validItems?.dwellingUnit?.id}, ${validItems?.person?.id}, ${validItems?.household?.id});" /> 
										<%--<g:actionSubmit class="create" action="addItem" value="Add Item" onclick="return confirm('Are You  Sure?');" /> --%>
									</span>
							</div>

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
