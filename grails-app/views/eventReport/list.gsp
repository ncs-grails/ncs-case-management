
<%@ page import="edu.umn.ncs.EventReport" %>
<div class="list">
    <table>
    	<g:if test="${eventReportInstanceList}">
        <thead>
            <tr>
              <th>Report</th>
              <g:sortableColumn property="filledOutDate" title="${message(code: 'eventReport.filledOutDate.label', default: 'Filled Out Date')}" />
              <g:sortableColumn property="filledOutBy" title="${message(code: 'eventReport.filledOutBy.label', default: 'Filled Out By')}" />

              <th>Events of Interest</th>          
              <th>Modify</th>
          </tr>
      	</thead>
      	</g:if>
      <tbody>
      <g:each in="${eventReportInstanceList}" status="i" var="eventReportInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          
              <td><g:link action="show" id="${eventReportInstance.id}">
              	${eventReportInstance.id}              	
              </g:link></td>
          
              <td>
				<g:formatDate date="${eventReportInstance.filledOutDate}" format="MM/dd/yyyy" />
			  </td>

              <td>
                ${eventReportInstance.filledOutBy}
              </td>

              <td>
              	<g:each in="${eventReportInstance.events}" status="count" var="eventOfInterestInstance">
					<g:if test="${count > 0}" >,</g:if>
					${eventOfInterestInstance.eventType}
		        </g:each>
              </td>
          
			  <td>
				<g:link action="edit" id="${eventReportInstance.id}">Edit</g:link>
			  </td>
          
          </tr>
      </g:each>
        </tbody>
        <tfoot>
        	<tr>
        		<th colspan="5">
        			<g:link action="create" params="${ ['person.id': personInstance?.id ] }">New Event of Interest Report</g:link>
        		</th>
        	</tr>
        </tfoot>

    </table>
</div>