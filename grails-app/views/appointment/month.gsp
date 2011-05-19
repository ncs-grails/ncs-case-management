
<%@ page import="edu.umn.ncs.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="index" params="${ ['person.id':appointmentInstance?.person?.id] }">
            	Back to <g:message code="default.list.label" args="[entityName]" />
            </g:link></span>
        </div>
        <div class="body">
            <h1>Appointments for <g:formatDate date="${refDate}" format="MMMM yyyy" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
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
							<td>
								<div class="dayOfMonth">${d.dayOfMonth}</div>
								<ul class="appointmentList">
									<g:each var="a" in="${d?.appointments}">
									<li>
										<g:formatDate date="${a?.startTime}" format="h:mm" /> - ${a?.type}<br/>
										${a.person?.lastName}
										${a.dwellingUnit?.address}
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
