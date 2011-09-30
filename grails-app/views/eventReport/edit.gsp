

<%@ page import="edu.umn.ncs.EventReport" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'eventReport.label', default: 'Event Report')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript library="prototype" />
		<g:javascript src="ncs-event.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="index" params="['person.id': eventReportInstance?.person?.id]"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="list" action="show" id="${eventReportInstance?.id}">Show Event Report</g:link></span>
            <span class="menuButton"><g:link class="create" action="create" params="['person.id': eventReportInstance?.person?.id]"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Event Report for ${eventReportInstance?.person}
            	<g:link controller="person" action="show" id="${eventReportInstance?.person?.id}" title="Open in Lookup">
            		<img style="vertical-align: middle;" src="${resource(dir:'images', file:'magnifying-glass-48x48.png')}" width="24" height="24" alt="View in Lookup" />
            	</g:link>
            </h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${eventReportInstance}">
            <div class="errors">
                <g:renderErrors bean="${eventReportInstance}" as="list" />
            </div>
            </g:hasErrors>
            
			<!-- Report Info -->
			<div class="dialog">
		        <!-- Form goes here -->

                <div id="tabs">
					<ul>
						<li><a href="#itabs-main">General Info</a></li>
				        <g:each var="i" in="${eventReportInstance.events.sort{ it.eventType.name }}">
			                <li><a href="#itabs-${i.id}">${i.eventType?.name}</a></li>
				        </g:each>
				        <g:set var="unusedEvents" value="${true}" />
				        <g:if test="${unusedEvents}">
							<li><a href="#itabs-new">New Event</a></li>
				        </g:if>
					</ul>
					<div id="itabs-main">
		                <div id="report">
				            <g:form method="post" >
					            	<g:hiddenField id="personId" name="person.id" value="${eventReportInstance?.person?.id}" />
					                <g:hiddenField name="id" value="${eventReportInstance?.id}" />
					                <g:hiddenField name="version" value="${eventReportInstance?.version}" />
					                <g:hiddenField name="studies" value="${eventReportInstance?.studies?.id}" />

					                <div class="prop">
					                    <span valign="top" class="name">
					                      <label for="eventSource"><g:message code="eventReport.eventSource.label" default="Event Source" /></label>
					                    </span>
					                    <span valign="top" class="${hasErrors(bean: eventReportInstance, field: 'eventSource', 'errors')}">
					                        <g:select name="eventSource.id" from="${edu.umn.ncs.EventSource.list()}" optionKey="id" value="${eventReportInstance?.eventSource?.id}"  />
							                <span id="eventSourceOther">
							                	<label for="eventSourceOther"><g:message code="eventReport.eventSourceOther.label" default="Explain Other:" /></label>
							                	<g:textField name="eventSourceOther" value="${eventReportInstance?.eventSourceOther}" />
						               		</span>
					                    </span>
					                </div>
					            	
					            	
					                <div class="prop">
					                    <span valign="top" class="name">
					                      <label for="contactDate"><g:message code="eventReport.contactDate.label" default="Contact Date" /></label>
					                    </span>
					                    <span valign="top" class="value ${hasErrors(bean: eventReportInstance, field: 'contactDate', 'errors')}">
					                        <%--<gui:datePicker id="contactDate"
					                        	value="${eventReportInstance?.contactDate}" 
					                        	formatString="MM/dd/yyyy" />--%>
		                                    <input size="12" id="datepicker" name="contactDate" type="text" value="<g:formatDate date='${eventReportInstance?.contactDate}' format='MM/dd/yyyy' />" />
					                    </span>
					                </div>
					            
					                <div class="prop">
					                    <span valign="top" class="name">
					                      <label for="filledOutBy"><g:message code="eventReport.filledOutBy.label" default="Filled Out By" /></label>
					                    </span>
					                    <span valign="top" class="value ${hasErrors(bean: eventReportInstance, field: 'filledOutBy', 'errors')}">
					                        <g:textField name="filledOutBy" value="${eventReportInstance?.filledOutBy}" />
					                    </span>
					                </div>
					            
					                <div class="prop">
					                    <span valign="top" class="name">
					                      <label for="filledOutDate"><g:message code="eventReport.filledOutDate.label" default="Filled Out Date" /></label>
					                    </span>
					                    <span valign="top" class="value ${hasErrors(bean: eventReportInstance, field: 'filledOutDate', 'errors')}">
					                        <%--<gui:datePicker id="filledOutDate"
					                        	value="${eventReportInstance?.filledOutDate}"
					                        	formatString="MM/dd/yyyy" />--%>
					                        <input size="12" id="filledOutDatepicker" name="filledOutDate" type="text" value="<g:formatDate date='${eventReportInstance?.filledOutDate}' format='MM/dd/yyyy' />"/>
					                    </span>
					                </div>
				                <div class="buttons">
				                	<span class="button">
				                		<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
			                		</span>
			                		<g:if test="${! eventReportInstance.events}">
				                	<span class="button">
				                		<g:actionSubmit class="delete" action="delete" value="Remove" onclick="return confirm('Are you sure you want to permanently delete this EOI Report?');" />
				                	</span>
				                	</g:if>
				                	<span class="button">
				                		<g:actionSubmit class="list" action="index" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" />
		                			</span>
				                </div>
				            </g:form>
		               </div>
					</div>
					<g:set var="eoiCount" value="${0}" />
			        <g:each var="eventOfInterestInstance" in="${eventReportInstance.events}" status="i" >
				        <div id="itabs-${eventOfInterestInstance.id}">
				        	<g:form method="post" controller="eventOfInterest">
				                <g:hiddenField name="eventOfInterestInstance_id" value="${eventOfInterestInstance?.id}" />
								<g:hiddenField name="${'eoiId' + eventOfInterestInstance?.id}" value="${i}" />
				
				                <div class="prop">
				                	<span class="name">
	                                   	<label for="eventType"><g:message code="eventOfInterest.eventType.label" default="Event Type" /></label>
	                                   </span>
				                	<span class="value"> 
	                                    <g:select name="eventType.id"
	                                    	title="${eoiCount}" 
	                                    	from="${edu.umn.ncs.EventType.list()}" 
	                                    	optionKey="id" 
	                                    	value="${eventOfInterestInstance?.eventType?.id}"
	                                    	onChange="${remoteFunction(
	                                    		controller:'eventType',
	                                    		action:'getUseElements',
	                                    		params:'\'id=\' + escape(this.value) + \'&controlId=\' + escape(this.title)',
	                                    		onComplete:'displayControls(e)')}"	                                    		                                   
	                                    	noSelection="['':'- Please Choose -']"  />
				                    </span>
				                </div>
	
				                <div id="${'eventDescription' + i}" class="prop <g:if test='${! eventOfInterestInstance?.eventType?.useEventDescription}'>hidden</g:if>">
				                	<span class="name">
	                                   	<label for="eventDescription"><g:if test="${eventOfInterestInstance?.eventType?.nameEventDescription}" >${eventOfInterestInstance?.eventType?.nameEventDescription}</g:if><g:else>Event Description</g:else></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:textField name="eventDescription" value="${eventOfInterestInstance?.eventDescription}" />
				                    </span>
				                </div>
	
				                <div id="${'eventCode' + i}" class="prop <g:if test='${! eventOfInterestInstance?.eventType?.useEventCode }'>hidden</g:if>">
				                	<span class="name">
	                                   	<label for="eventCode"><g:if test="${eventOfInterestInstance?.eventType?.nameEventCode}" >${eventOfInterestInstance?.eventType?.nameEventCode}</g:if><g:else>Event Code</g:else></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:textField name="eventCode" value="${fieldValue(bean: eventOfInterestInstance, field: 'eventCode')}" />
				                    </span>
				                </div>
	
				                <div id="${'eventPickOne' + i }" class="prop <g:if test='${! eventOfInterestInstance?.eventType?.useEventPickOne }'>hidden</g:if>">
				                	<span class="name">
	                                   	<label for="eventPickOne"><g:message code="eventOfInterest.eventPickOne.label" default="Event Pick One" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:select id="${'eventPickOneOptions' + i}"  name="eventPickOne.id" from="${edu.umn.ncs.EventPickOne.findAllByEventType(eventOfInterestInstance?.eventType)}" optionKey="id" value="${eventOfInterestInstance?.eventPickOne?.id}" noSelection="['null': '']" />
				                    </span>
				                </div>
	
				                <div id="${'eventDateDiv' + i}" class="prop <g:if test='${! eventOfInterestInstance?.eventType?.useEventDate }'>hidden</g:if>">
				                	<span class="name">
	                                   	<label for="eventDate"><g:message code="eventOfInterest.eventDate.label" default="Event Date" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <%--<gui:datePicker id="${'eventDate' + i}"
	                                    	formatString="MM/dd/yyyy"
	                                    	value="${eventOfInterestInstance?.eventDate}" />--%>
                                    	<input id="${'eventDatepicker' + i}" name="${'eventDate' + i}" type="text" value="<g:formatDate date='${eventOfInterestInstance?.eventDate}' format='MM/dd/yyyy' />" />
								                                    	
				                    </span>
				                </div>
	
				                <div id="${'datePrecision' + i}" class="prop <g:if test='${! eventOfInterestInstance?.eventType?.useEventDate }'>hidden</g:if>">
				                	<span class="name">
	                                   	<label for="datePrecision"><g:message code="eventOfInterest.datePrecision.label" default="Date Precision" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:select name="datePrecision" 
	                                    	from="['day','month','year']" 
	                                    	value="${eventOfInterestInstance?.datePrecision}"
	                                    	noSelection="['':'- Please Choose -']"  />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="eventResult"><g:message code="eventOfInterest.eventResult.label" default="Event Result" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:select name="eventResult.id" from="${edu.umn.ncs.EventResult.list()}" optionKey="id" value="${eventOfInterestInstance?.eventResult?.id}" noSelection="['null': '']" />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="eventResultDate"><g:message code="eventOfInterest.eventResultDate.label" default="Event Result Date" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <%--<gui:datePicker id="${'eventResultDate' + i}"
	                                    	formatString="MM/dd/yyyy"
	                                    	value="${eventOfInterestInstance?.eventResultDate}" />--%>
                                    	<input id="${'eventResultDatepicker' + i}" name="${'eventResultDate' + i}" type="text" value="<g:formatDate date='${eventOfInterestInstance?.eventResultDate}' format='MM/dd/yyyy' />" />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="userResultEntered"><g:message code="eventOfInterest.userResultEntered.label" default="Result Entered By" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:textField name="userResultEntered" value="${eventOfInterestInstance?.userResultEntered}" />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="dateResultEntered"><g:message code="eventOfInterest.dateResultEntered.label" default="Date Result Entered" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <%--<gui:datePicker id="${'dateResultEntered' + i}"
	                                    	formatString="MM/dd/yyyy"
	                                    	value="${eventOfInterestInstance?.dateResultEntered}" />--%>
                                    	<input id="${'dateResultEnteredDatepicker' + i}" name="${'dateResultEntered' + i}" type="text" value="<g:formatDate date='${eventOfInterestInstance?.dateResultEntered}' format='MM/dd/yyyy' />" />
				                    </span>
				                </div>
								<g:set var="eoiCount" value="${i}" />
				                <div class="buttons">
				                	<span class="button">
					                	<g:actionSubmit class="save" action="update" value="Save" />
					                </span>
				                	<span class="button">
					                	<g:actionSubmit class="delete" action="delete" value="Remove" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure you want to permanently delete this EOI?')}');" />
					                </span>
				                </div>
				        	</g:form>
				        </div>
					</g:each>
					<g:if test="${unusedEvents}">
				        <div id="itabs-new">
			            <g:hasErrors bean="${eventOfInterestInstance}">
			            <div class="errors">
			                <g:renderErrors bean="${eventOfInterestInstance}" as="list" />
			            </div>
			            </g:hasErrors>
				        	<g:form method="post" controller="eventOfInterest">
				                <g:hiddenField name="eventReport.id" value="${eventReportInstance?.id}" />
								<g:set var="eoiCount" value="${eoiCount + 1}" />
								<g:hiddenField name="eoiCount" value="${eoiCount}" />
				
				                <div class="prop">
				                	<span class="name">
	                                   	<label for="eventType"><g:message code="eventOfInterest.eventType.label" default="Event Type" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:select name="eventType.id"
	                                    	title="${eoiCount}" 
	                                    	from="${edu.umn.ncs.EventType.list()}" 
	                                    	optionKey="id" 
	                                    	value=""
	                                    	onChange="${remoteFunction(
	                                    		controller:'eventType',
	                                    		action:'getUseElements',
	                                    		params:'\'id=\' + escape(this.value) + \'&controlId=\' + escape(this.title)',
	                                    		onComplete:'displayControls(e)')}"	                                    		                                   
	                                    	noSelection="['':'- Please Choose -']"  />
				                    </span>
				                </div>
	
				                <div id="${'eventDescription' + eoiCount}" class="prop hidden">
				                	<span class="name">
	                                   	<label id="${'eventDescriptionName' + eoiCount}" for="eventDescription"><g:message code="eventOfInterest.eventDescription.label" default="Event Description" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:textField name="eventDescription" value="${eventOfInterestInstance?.eventDescription}" />
				                    </span>
				                </div>
	
				                <div id="${'eventCode' + eoiCount}" class="prop hidden">
				                	<span class="name">
	                                   	<label id="${'eventCodeName' + eoiCount}" for="eventCode"><g:message code="eventOfInterest.eventCode.label" default="Event Code" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:textField name="eventCode" value="${fieldValue(bean: eventOfInterestInstance, field: 'eventCode')}" />
				                    </span>
				                </div>
	
				                <div id="${'eventPickOne' + eoiCount}" class="prop hidden">
				                	<span class="name">
	                                   	<label for="eventPickOne"><g:message code="eventOfInterest.eventPickOne.label" default="Event Pick One" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:select id="${'eventPickOneOptions' + eoiCount}" name="eventPickOne.id" from="" optionKey="id" value="" noSelection="['': '-Please choose-']" />
				                    </span>
				                </div>
	
				                <div id="${'eventDateDiv' + eoiCount}" class="prop hidden">
				                	<span class="name">
	                                   	<label for="eventDate"><g:message code="eventOfInterest.eventDate.label" default="Event Date" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <%--<gui:datePicker id="${'eventDate' + eoiCount}"
	                                    	formatString="MM/dd/yyyy"
	                                    	value="" />--%>
                                    	<input id="${'eventDatepicker' + eoiCount}" name="${'eventDate' + eoiCount}" type="text" value="" />
				                    </span>
				                </div>
	
				                <div id="${'datePrecision' + eoiCount}" class="prop hidden">
				                	<span class="name">
	                                   	<label for="datePrecision"><g:message code="eventOfInterest.datePrecision.label" default="Date Precision" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <%--<g:textField name="datePrecision" value="${eventOfInterestInstance?.datePrecision}" /> --%>
	                                    <g:select name="datePrecision" 
	                                    	from="['day','month','year']" 
	                                    	noSelection="['':'- Please Choose -']"  />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="eventResult"><g:message code="eventOfInterest.eventResult.label" default="Event Result" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:select name="eventResult.id" from="${edu.umn.ncs.EventResult.list()}" optionKey="id" value="${eventOfInterestInstance?.eventResult?.id}" noSelection="['null': '']" />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="eventResultDate"><g:message code="eventOfInterest.eventResultDate.label" default="Event Result Date" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <%--<gui:datePicker id="${'eventResultDate' + eoiCount}"
	                                    	formatString="MM/dd/yyyy"
	                                    	value="${eventOfInterestInstance?.eventResultDate}" />--%>
                                    	<input id="${'eventResultDatepicker' + eoiCount}" name="${'eventResultDate' + eoiCount}" type="text" value="" />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="userResultEntered"><g:message code="eventOfInterest.userResultEntered.label" default="Result Entered By" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <g:textField name="userResultEntered" value="${eventOfInterestInstance?.userResultEntered}" />
				                    </span>
				                </div>
	
				                <div class="prop hidden">
				                	<span class="name">
	                                   	<label for="dateResultEntered"><g:message code="eventOfInterest.dateResultEntered.label" default="Date Result Entered" /></label>
				                	</span>
				                	<span class="value"> 
	                                    <%--<gui:datePicker id="${'dateResultEntered' + eoiCount}"
	                                    	formatString="MM/dd/yyyy"
	                                    	value="${eventOfInterestInstance?.dateResultEntered}" />--%>
                                    	<input id="${'dateResultEnteredDatepicker' + eoiCount}" name="${'dateResultEntered' + eoiCount}" type="text" value="" />
				                    </span>
				                </div>
				                <div class="buttons">
				                	<span class="button">
						                <g:actionSubmit class="save" action="save" value="Add" />
					                </span>
				                </div>
				        	</g:form>
				        </div>
					</g:if> <!-- /Events -->
				</div>
	        </div>
        </div>
    </body>
</html>
