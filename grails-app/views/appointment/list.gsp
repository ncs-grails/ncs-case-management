<%@ page import="edu.umn.ncs.Appointment" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="ncs" />
		<g:set var="entityName"
		value="${message(code: 'appointment.label', default: 'Appointment')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav"><span class="menuButton"><a class="home"
					href="${createLink(uri: '/')}"><g:message
					code="default.home.label" /></a></span> <span class="menuButton"><g:link
				class="create" action="create">
				<g:message code="default.new.label" args="[entityName]" />
				</g:link></span></div>
				<div class="body">
					<h1><g:message code="default.list.label" args="[entityName]" /></h1>
					<g:if test="${flash.message}">
					<div class="message">${flash.message}</div>
					</g:if>
					<div class="list">
						<table>
							<thead>
								<tr>

									<g:sortableColumn property="id" title="${message(code: 'appointment.id.label', default: 'Id')}" />

									<g:sortableColumn property="startTime" title="${message(code: 'appointment.startTime.label', default: 'Start Time')}" />

									<g:sortableColumn property="endTime" title="${message(code: 'appointment.endTime.label', default: 'End Time')}" />

									<th><g:message code="appointment.person.label" default="Person" /></th>

									<th><g:message code="appointment.dwellingUnit.label" default="Dwelling Unit" /></th>

									<th><g:message code="appointment.study.label" default="Study" /></th>

								</tr>
							</thead>
							<tbody>
								<g:each in="${appointmentInstanceList}" status="i" var="appointmentInstance">
								<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

									<td><g:link action="show" id="${appointmentInstance.id}">${fieldValue(bean: appointmentInstance, field: "id")}</g:link></td>

									<td><g:formatDate date="${appointmentInstance.startTime}" /></td>

									<td><g:formatDate date="${appointmentInstance.endTime}" /></td>

									<td>${fieldValue(bean: appointmentInstance, field: "person")}</td>

									<td>${fieldValue(bean: appointmentInstance, field: "dwellingUnit")}</td>

									<td>${fieldValue(bean: appointmentInstance, field: "study")}</td>

								</tr>
								</g:each>
							</tbody>
						</table>
					</div>
				</div>
			</body>
		</html>
