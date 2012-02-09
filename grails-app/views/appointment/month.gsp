<%@ page import="edu.umn.ncs.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'calendar.css')}" />        
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
		<span class="menuButton"><g:link class="list" action="list">List</g:link></span>
            <span class="menuButton"><g:link class="create" action="index">Enter New Appointment</g:link></span>
        </div>
        <div class="body" style="margin: 0 0 1em 0;">
            <h1>Appointments for <g:formatDate date="${refDate}" format="MMMM yyyy" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
            	<span class="prevMonth">
            		<g:link action="calendar" id="${formatDate(date:prevMonth, format:'yyyy-MM')}">
            		&lt; <g:formatDate date="${prevMonth}" format="MMMM yyyy" />
            		</g:link>
            	</span>
            	<span class="nextMonth">
            		<g:link action="calendar" id="${formatDate(date:nextMonth, format:'yyyy-MM')}">
            		<g:formatDate date="${nextMonth}" format="MMMM yyyy" /> &gt; 
            		</g:link>
            	</span>
                <table class="calendar">
                	<thead>
                		<tr>
                			<th>Sunday</th>
                			<th>Monday</th>
                			<th>Tuesday</th>
                			<th>Wednesday</th>
                			<th>Thursday</th>
                			<th>Friday</th>
                			<th>Saturday</th>
                		</tr>
                	</thead>
                    <tbody>
						<g:each var="w" in="${weeks}">
						<tr>
							<g:each var="d" in="${w?.days}">
							<td class="${d?.cssShadow}">
								<div class="dayOfMonth">${d.dayOfMonth}</div>
								<ul class="appointmentList">
									<g:each var="a" in="${d?.appointments}">
									<li>
										${formatDate(date:a?.startTime, format:'H') + formatDate(date:a?.startTime, format:'a').toLowerCase()[0]}
										<g:link controller="appointment" action="show" id="${a.id}">${a.person?.lastName}
										${a.dwellingUnit?.address}
										</g:link>
									</li>
									</g:each>
								</ul>
							</td>
							</g:each>
						</tr>
						</g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
