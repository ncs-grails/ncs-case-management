<%@ page import="edu.umn.ncs.Appointment" %>
<div class="list">
    <table>
    	<g:if test="${appointmentInstanceList}">
        <thead>
            <tr>
              <g:sortableColumn property="startTime" title="${message(code: 'appointment.appCreated.label', default: 'Appt Start Time')}" />
          
              <th>Type</th>
              <th>Result (click to change)</th>
              <th>Modify</th>
          </tr>
      	</thead>
      	</g:if>
      <tbody>
      <g:each in="${appointmentInstanceList}" status="i" var="appointmentInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          
              <td><g:link action="show" id="${appointmentInstance.id}">
              	<g:formatDate date="${appointmentInstance.startTime}" format="MM/dd/yyyy hh:mm a" />
              </g:link></td>
          
              <td>${appointmentInstance.type}</td>

              <td>
              	<g:link action="result" id="${appointmentInstance.id}">
              		${appointmentInstance.result ?: "(none)"}
				</g:link>
              </td>
          
			  <td>
				<g:link action="edit" id="${appointmentInstance.id}">Edit</g:link>
				|
			  	<g:link action="reschedule" id="${appointmentInstance.id}">Reschedule</g:link>
			  </td>
          
          </tr>
      </g:each>
        </tbody>
        <tfoot>
        	<tr>
        		<th colspan="4">
        			<g:link action="create" params="${ ['person.id': personInstance?.id ] }">New Appointment</g:link>
        		</th>
        	</tr>
        </tfoot>

    </table>
</div>

