<%@ page import="edu.umn.ncs.Appointment" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="ncs" />
		<g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div class="nav">
			<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span> 
			<span class="menuButton"><g:link class="list" action="calendar">Calendar</g:link></span>
		</div>

		<div class="body">

			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			<div class="list">
				<table>
					<thead>
						<tr>
							<g:sortableColumn property="personId" title="${message(code: 'appointment.personId.label', default: 'Person Id')}" />
							<g:sortableColumn property="norcId" title="${message(code: 'personLink.norcSuId.label', default: 'NORC Id')}" />
							<g:sortableColumn property="name" title="Name" />
							<g:sortableColumn property="visit" title="${message(code: 'appointmentType.name.label', default: 'Visit')}" />
							<g:sortableColumn property="startTime" title="${message(code: 'appointment.startTime.label', default: 'Start Time')}" />
						</tr>
					</thead>
					<tbody>
						<g:each in="${appointmentDetailInstanceList}" status="i" var="ea">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td>${ea.personId}</td>
								<td>${ea.norcId}</td>
								<td>${ea.lastName}, ${ea.firstName}</td>
								<td>${ea.apptType}</td>
								<td><g:formatDate date="${ea.startTime}" /></td>
							</tr>
						</g:each>
					</tbody>
				</table>
			</div> <!-- end class=list  -->

			Total number of appointments: ${appointmentInstanceTotal}




		</div> <!-- class=body  -->
	</body>
</html>
