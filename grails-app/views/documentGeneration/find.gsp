 
<g:if test="${flash.message}">
    <!-- <div class="message">${flash.message}</div> -->
    <p class="highlightRed">${flash.message}</p>
  </g:if>

  <div class="list">
    <table>
      <thead>
        <tr>
      <g:sortableColumn property="batchCreationQueue.person.id" params="${[q: params?.q]}" title="${message(code: 'batchCreationQueue.person.label', default: 'Person')}" />

      <g:sortableColumn property="batchCreationQueue.household.id" params="${[q: params?.q]}" title="${message(code: 'batchCreationQueue.household.label', default: 'Household')}" />

      <g:sortableColumn property="batchCreationQueue.dwellingUnit.id" params="${[q: params?.q]}" title="${message(code: 'batchCreationQueue.dwellingUnit.label', default: 'Dwelling Unit')}" />

      </tr>
      </thead>
      <tbody>
      <g:each in="${batchCreationQueueList}" status="i" var="batchCreationQueue">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td>${batchCreationQueue?.person?.name}</td>

          <td>${batchCreationQueue?.household?.name}</td>

          <td>${batchCreationQueue?.dwellingUnit?.address?.address}</td>

        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
