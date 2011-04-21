
<%@ page import="edu.umn.ncs.Batch" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'batch.label', default: 'Batch')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.dateCreated.label" default="Date Created" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.dateCreated}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.format.label" default="Format" /></td>
                            
                            <td valign="top" class="value"><g:link controller="instrumentFormat" action="show" id="${batchInstance?.format?.id}">${batchInstance?.format?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.direction.label" default="Direction" /></td>
                            
                            <td valign="top" class="value"><g:link controller="batchDirection" action="show" id="${batchInstance?.direction?.id}">${batchInstance?.direction?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.master.label" default="Master" /></td>
                            
                            <td valign="top" class="value"><g:link controller="batch" action="show" id="${batchInstance?.master?.id}">${batchInstance?.master?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.trackingReturnDate.label" default="Tracking Return Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.trackingReturnDate}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.minimumReturnDate.label" default="Minimum Return Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.minimumReturnDate}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.instrumentDate.label" default="Instrument Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.instrumentDate}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.mailDate.label" default="Mail Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.mailDate}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.addressAndMailingDate.label" default="Address And Mailing Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.addressAndMailingDate}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.printingServicesDate.label" default="Printing Services Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.printingServicesDate}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.batchRunBy.label" default="Batch Run By" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchInstance, field: "batchRunBy")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.batchRunByWhat.label" default="Batch Run By What" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchInstance, field: "batchRunByWhat")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.trackingDocumentSent.label" default="Tracking Document Sent" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${batchInstance?.trackingDocumentSent}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.updatedBy.label" default="Updated By" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: batchInstance, field: "updatedBy")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.creationConfig.label" default="Creation Config" /></td>
                            
                            <td valign="top" class="value"><g:link controller="batchCreationConfig" action="show" id="${batchInstance?.creationConfig?.id}">${batchInstance?.creationConfig?.name?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.instruments.label" default="Instruments" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${batchInstance.instruments}" var="i">
                                    <li><g:link controller="batchInstrument" action="show" id="${i.id}">${i?.instrument?.name?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.items.label" default="Items" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                            
                                <!--  <ul>
                                <g:each in="${batchInstance.items}" var="i">
                                    <li><g:link controller="trackedItem" action="show" id="${i.id}">Tracked Item ID: ${i?.id?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                                 -->
                                
                                <div class="manageBatch">
 								<table class="scroll">
									<thead>
										<th>Item ID</th>
										<th>Dwelling Unit Id</th>
										<th>Person Id</th>
										<th>Household Id</th>
									</thead>
									<tbody>
										<g:each in="${batchInstance.items}" status="s" var="i">
											<tr class="${(s % 2) == 0 ? 'even' : 'odd'}">
												<td><g:link controller="trackedItem" action="show" id="${i.id}">${i?.id?.encodeAsHTML()}</g:link></td>
												<td><g:link controller="trackedItem" action="show" id="${i.id}">${i.dwellingUnit?.id?.encodeAsHTML()}</g:link></td>
												<td><g:link controller="trackedItem" action="show" id="${i.id}">${i.person?.id?.encodeAsHTML()}</g:link></td>
												<td><g:link controller="trackedItem" action="show" id="${i.id}">${i.household?.id?.encodeAsHTML()}</g:link></td>
											</tr>
										</g:each>
									</tbody>
							</table>   
							</div>
                                
                                
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="batch.lastUpdated.label" default="Last Updated" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${batchInstance?.lastUpdated}" format="M/d/yyyy"/></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${batchInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
