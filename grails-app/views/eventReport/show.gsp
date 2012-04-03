
<%@ page import="edu.umn.ncs.EventReport" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'eventReport.label', default: 'EventReport')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
		<g:javascript src="ncs-event.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="index" params="['person.id': eventReportInstance?.person?.id]">Event Report List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create" params="['person.id': eventReportInstance?.person?.id]">New Event Report</g:link></span>
            <span class="menuButton"><g:link class="edit" action="edit" id="${eventReportInstance?.id }">Edit Event Report</g:link></span>
        </div>
        <div class="body">
            <h1>Event Report for ${eventReportInstance?.person}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>

			<!-- Report Info -->
			<div class="dialog">
            	<div class="ui-tabs ui-widget ui-widget-content ui-corner-all pad-content"> 
				
	                <div class="prop">
	                    <span valign="top" class="show">
	                      <label for="studies"><g:message code="eventReport.studies.label" default="Studies:" /></label>
	                    	<g:if test="${eventReportInstance?.studies}">
	                    		<ul>
	                    			<g:each var="studyInstance" in="${eventReportInstance?.studies}">
	                    				<li>${studyInstance?.name}</li>
	                    			</g:each>
	                    		</ul>
                    		</g:if>
                    		<g:else>    None</g:else>
	                    </span>
	                </div>
	            
	                <div class="prop">
	                    <span valign="top" class="show">
	                      <label for="eventSource"><g:message code="eventReport.eventSource.label" default="Event Source:" /></label>    ${eventReportInstance?.eventSource}
	                    </span>
	                </div>
	            
	                <g:if test="${eventReportInstance?.eventSourceOther}">
		                <div id="eventSourceOther" class="prop">
		                    <span valign="top" class="show">
		                      <label for="eventSourceOther"><g:message code="eventReport.eventSourceOther.label" default="Event Source Other:" /></label>    ${eventReportInstance?.eventSourceOther}
		                    </span>
		                </div>
	            	</g:if>
	            	
	                <div class="prop">
	                    <span valign="top" class="show">
	                      <label for="contactDate"><g:message code="eventReport.contactDate.label" default="Contact Date:" /></label>    <g:formatDate date="${eventReportInstance?.contactDate}" format="MM/dd/yyyy" /> 
	                    </span>
	                </div>
	            
	                <div class="prop">
	                    <span valign="top" class="show">
	                      <label for="filledOutBy"><g:message code="eventReport.filledOutBy.label" default="Filled Out By:" /></label>    ${eventReportInstance?.filledOutBy}
	                    </span>
	                </div>
	            
	                <div class="prop">
	                    <span valign="top" class="show">
	                      <label for="filledOutDate"><g:message code="eventReport.filledOutDate.label" default="Filled Out Date:" /></label>    <g:formatDate date="${eventReportInstance?.filledOutDate}" format="MM/dd/yyyy"/>
	                    </span>
	                </div>
               </div>

				<!-- Events -->
				<div class="eoi-header">
					<h2>Events of Interest</h2>
				</div>
				<g:if test="${eventReportInstance?.events}">
	                <div id="tabs">
						<ul>
					        <g:each var="i" in="${eventReportInstance.events.sort{ it.eventType.name }}">
				                <li><a href="#itabs-${i.id}">${i.eventType?.name}</a></li>
					        </g:each>
						</ul>
				        <g:each var="eventOfInterestInstance" in="${eventReportInstance.events.sort{ it.eventType.name }}">
					        <div id="itabs-${eventOfInterestInstance.id}">
				
				                <div class="prop">
				                	<span class="show">
                                    	<label for="eventType"><g:message code="eventOfInterest.eventType.label" default="Event Type:" /></label>    ${eventOfInterestInstance?.eventType}
                                    </span>
				                </div>

								<g:if test="${eventOfInterestInstance?.eventType?.useEventDescription}" >
				                <div class="prop">
				                	<span class="show">
                                    	<label for="eventDescription"><g:if test="${eventOfInterestInstance?.eventType?.nameEventDescription}" >${eventOfInterestInstance?.eventType?.nameEventDescription}:</g:if><g:else>Event Code:</g:else></label>    ${eventOfInterestInstance?.eventDescription}
				                    </span>
				                </div>
				                </g:if>

								<g:if test="${eventOfInterestInstance?.eventType?.useEventCode}" >
				                <div class="prop">
				                	<span class="show">

										<label for="eventCode">
											<g:if test="${eventOfInterestInstance?.eventType?.nameEventCode}">
												${eventOfInterestInstance?.eventType?.nameEventCode}
											</g:if>
											<g:else>
												Event Code
											</g:else>
										</label>    ${eventOfInterestInstance?.eventCode}
				                    </span>
				                </div>
				                </g:if>

								<g:if test="${eventOfInterestInstance?.eventType?.useEventPickOne}" >
				                <div class="prop">
				                	<span class="show">
										<label for="eventPickOne">
											<g:if test="${eventOfInterestInstance?.eventType?.nameEventPickOne}">
												${eventOfInterestInstance?.eventType?.nameEventPickOne}
											</g:if>
											<g:else>
												Event Pick One:
											</g:else>
										</label>    ${eventOfInterestInstance?.eventPickOne}
				                    </span>
				                </div>
				                </g:if>

								<g:if test="${eventOfInterestInstance?.eventType?.useEventDate}" >
				                <div class="prop">
				                	<span class="show">
										<label for="eventDate">
											<g:if test="${eventOfInterestInstance?.eventType?.nameEventDate}">
												${eventOfInterestInstance?.eventType?.nameEventDate}
											</g:if>
											<g:else>
												Event Date
											</g:else>
										</label>    <g:formatDate format="MM/dd/yyyy" date="${eventOfInterestInstance?.eventDate}" />
				                    </span>
				                </div>
				                </g:if>

								<g:if test="${eventOfInterestInstance?.eventType?.useEventDate}" >
				                <div class="prop">
				                	<span class="show">
                                    	<label for="datePrecision"><g:message code="eventOfInterest.datePrecision.label" default="Date Precision:" /></label>    ${eventOfInterestInstance?.datePrecision}
				                    </span>
				                </div>
				                </g:if>

				                <%--<div class="prop">
				                	<span class="show">
                                    	<label for="eventResult"><g:message code="eventOfInterest.eventResult.label" default="Event Result:" /></label>    ${eventOfInterestInstance?.eventResult}
				                    </span>
				                </div>

				                <div class="prop">
				                	<span class="show">
                                    	<label for="eventResultDate"><g:message code="eventOfInterest.eventResultDate.label" default="Event Result Date:" /></label>    <g:formatDate format="MM/dd/yyyy" date="${eventOfInterestInstance?.eventResultDate}" />
				                    </span>
				                </div>

				                <div class="prop">
				                	<span class="show">
                                    	<label for="userResultEntered"><g:message code="eventOfInterest.userResultEntered.label" default="Result Entered By:" /></label>    ${eventOfInterestInstance?.userResultEntered}
				                    </span>
				                </div>

				                <div class="prop">
				                	<span class="show">
                                    	<label for="dateResultEntered"><g:message code="eventOfInterest.dateResultEntered.label" default="Date Result Entered:" /></label>    <g:formatDate format="MM/dd/yyyy" date="${eventOfInterestInstance?.dateResultEntered}" />
				                    </span>
				                </div> --%>
					
					        </div>
						</g:each>
					</div>		
				</g:if>
			</div>			
        </div>
    </body>
</html>
