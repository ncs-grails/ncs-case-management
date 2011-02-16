<%@ page contentType="text/html;charset=UTF-8"%>
<g:setProvider library="jquery" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="ncs" />
<g:set var="entityName"
	value="${message(code: 'Report.label', default: 'Report')}" />
<title>Report</title>
</head>
<body>
<div class="nav"><span class="menuButton"><a class="home"
	href="${createLink(uri: '/')}"><g:message code="default.home.label" /></a></span>
<g:javascript src="jquery/jquery.tablesorter.min.js" /> <g:javascript
	src="registry-results.js" /></div>
<div class="body">
<h1>Report</h1>
<g:if test="${flash.message}">
	<div class="message">
	${flash.message}
	</div>
</g:if>

<div class="filter-list"><label for="filter">Filter List </label>
<input type="text" name="filter" value="" id="filter" /></div>

<fieldset class="maroonBorder"><legend>Report</legend>
<div class="list">
<table id="list-table" class="tablesorter">
	<thead>
		<tr>
			<g:each in="${headerList}" status="i" var="headerInstance">

				<th>
				${headerInstance}
				</th>

			</g:each>

		</tr>
	</thead>
	<tbody>
		<g:each in="${recordList}" status="i" var="recordInstance">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

				<g:each in="${headerList}" var="headerInstance">
					<td>
					${recordInstance[headerInstance]}
					</td>
				</g:each>

			</tr>
		</g:each>
	</tbody>
</table>
</div>
</fieldset>



<%--<div class="paginateButtons">
      <g:paginate total="${reportInstanceTotal}" />
    </div>--%></div>
</body>
</html>