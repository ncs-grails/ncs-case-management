
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'report_print.css')}" media="print" />
        <title>Show Report</title>
    </head>
    <body>
        <div class="body">
            <h1>${reportInstance?.study?.name} ${reportInstance?.title} Report</h1>
            <h3>${reportInstance?.subtitle}</h3>
			
            <div class="dialog">
                <table>
                    <thead>
                        <tr>
                        	<g:each var="headerInstance" in="${headerList}" >
								<th>${headerInstance}</th>
							</g:each>
                        </tr>
                    </thead>
                    <tbody>
	                    <g:each in="${recordList}" status="i" var="recordInstance">
	                        <tr>
	                        	<g:each var="headerInstance" in="${columnList}" >	                        		
									<td>
										<g:if test="${headerInstance.toLowerCase().contains('date')}">
											<g:if test="${headerInstance.toLowerCase().contains('updated_by')}">
												${recordInstance[headerInstance]}
											</g:if>
											<g:else><g:formatDate date="${recordInstance[headerInstance]}" format="MM/dd/yyyy" /></g:else>
										</g:if>
										<g:else>${recordInstance[headerInstance]}</g:else>
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