<%@ page import="edu.umn.ncs.Appointment" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="ncs" />
		<g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'appointment.css')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div class="nav">
			<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span> 
			<span class="menuButton"><g:link class="list" action="calendar">Calendar</g:link></span>
		</div>

		<div class="body">

			<h1>
				<g:message code="default.list.label" args="[entityName]" />
				<g:if test="${appointmentTypeInstance}">: ${appointmentTypeInstance.name.toUpperCase()}</g:if>
			</h1>
		<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			
				<g:form method="post">

					<div class="formControlSection">
						Appointment Type <g:select 
							name="appointmentType.id" 
							from="${edu.umn.ncs.AppointmentType.listOrderByName()}" 
							optionKey="id" 
							optionValue="name"
							value="${appointmentTypeInstance?.id}" 
							noSelection="['': ' -- Choose Appointment Type  -- ']" 
						/>
						<g:actionSubmit action="list" value="GO" />
					</div>

					<g:if test="${appointmentDetailInstanceList}">
						<div class="list">
							<table>
								<thead>
									<!-- 
									<tr>
										<th class="basic" colspan="5">
											<g:if test="${appointmentTypeInstance}">Current ${appointmentTypeInstance.name.toUpperCase()} Appointments</g:if>
											<g:else>All Current Appointments</g:else>
										</th>
									</tr>
									-->
									<tr>
										<th class="basic"></th>
										<th class="basic">Person Id</th>
										<th class="basic">NORC Id</th>
										<th class="basic">Name</th>
										<th class="basic">Type</th>
										<th class="basic">Start Time</th>
										<th class="basic">Result</th>
									</tr>
								</thead>
								<tbody>
									<g:each in="${appointmentDetailInstanceList}" status="i" var="ea">
										<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
											<td class="basic" style="text-align:right">${ea.rowNum}</td>
											<td class="basic" style="text-align:right">${ea.personId}</td>
											<td class="basic">${ea.norcId}</td>
											<td class="basic">${ea.lastName}, ${ea.firstName}</td>
											<td class="basic">${ea.apptType}</td>
											<td class="basic"><g:formatDate date="${ea.startTime}" /></td>
											<td class="basic">${ea.apptResult}</td>
										</tr>
									</g:each>
								</tbody>
							</table>
						</div> <!-- end class=list  -->
					</g:if>
					<g:else>
						<div class="specialMessage">
							No current appointments.
						</div>
					</g:else>
					<p class="tableFooterNote">
						Total Past Appointments: ${pastAppointmentTotal}
					</p>

				</g:form>

		</div> <!-- class=body  -->

	</body>

</html>
