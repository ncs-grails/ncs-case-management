<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Incentives Checked Out To ${interviewer?.displayName}</title>

		<style>
			body
			{
				/*width: 8.5in;*/
				width: 9.0in;
				/*margin: 0.3in .1875in;*/
				margin: 0.3in 0;
			}
			.label
			{
		    	/* Avery 5160 labels -- CSS and HTML by MM at Boulder Information Services */
		        /*width: 2.625in; /* plus .25 inches from padding */
		        width: 2.875in; /* plus .25 inches from padding */
		        height: .875in; /* plus .125 inches from padding */
		        /*padding: .125in .125in; */
		        padding: .125in 0;
		        margin-right: .125in; /* the gutter */
		
		        float: left;
		
		        text-align: center;
		        overflow: hidden;
		
		        /*outline: 1px dotted; /* outline doesn't occupy space like border does */
			}
			.label-row 
			{
				padding-bottom: .1in;
			}
			.barcode {
		        font-size:0.7em;
			}
		    .page-break  {
		        clear: left;
		        display:block;
		        page-break-after:always;
			}
			img
			{
				height:1.9em;
				width:18.0em;
				margin-left:-1.0em;
			}
		</style>
    </head>
    <body>
    	<g:each in="${incentiveInstanceList}" var="incentiveInstance" status="i">
    		<div class="label">
    			<div class="label-row">${incentiveInstance?.type?.name}</div>
    			<div class="label-row barcode"><img src="${createLink(controller:'barcode', action:'png', id:'INC' + incentiveInstance?.id)}" /></div>
    			<div class="label-row barcode">${incentiveInstance?.barcode}</div>    			
    		</div>
    	</g:each>
    </body>
</html>