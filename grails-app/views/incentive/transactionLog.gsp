
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'incentiveInstance.label', default: 'Incentive')}" />
        <title>Incentive Transaction Log</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list">Incentive List</g:link></span>
        </div>
        <div class="body">
            <h1>Transaction Log for <g:formatNumber number="${incentiveInstance?.amount}" type="currency" currencyCode="USD" /> ${incentiveInstance?.type?.name}: <g:link action="edit" id="${incentiveInstance.id}">${incentiveInstance.id}</g:link></h1>
			<h3>Current Status: <g:if test="${incentiveInstance?.checkedOut}">Checked out to ${incentiveInstance?.checkedOutToWhom} on <g:formatDate date="${incentiveInstance?.dateCheckedOut}" format="MM/dd/yyyy h:mm a" /></g:if><g:else>Not checked out</g:else></h3><br />
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
            	<div>Showing ${incentiveTransactionLogTotal} transaction(s)</div>
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="checkedOutInToWhom" title="${message(code: 'incentiveTransactionLog.checkedOutInToWhom.label', default: 'Checked Out To / Checked In From')}" />
                        
                            <g:sortableColumn property="checkedOutInByWhom" title="${message(code: 'incentiveTransactionLog.checkedOutInByWhom.label', default: 'Checked Out/In By')}" />
                        
                            <g:sortableColumn property="transactionDate" title="${message(code: 'incentiveTransactionLog.transactionDate.label', default: 'Transaction Date')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${incentiveTransactionLogList}" status="i" var="incentiveTransactionLogInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td>${fieldValue(bean: incentiveTransactionLogInstance, field: "checkedOutInToWhom")}</td>
                        
                            <td>${fieldValue(bean: incentiveTransactionLogInstance, field: "checkedOutInByWhom")}</td>
                        
                            <td><g:formatDate date="${incentiveTransactionLogInstance?.transactionDate}" format="MM/dd/yyyy h:mm a" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${incentiveTransactionLogTotal}" />
            </div>
        </div>
    </body>
</html>
