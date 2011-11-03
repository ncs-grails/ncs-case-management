<g:setProvider library="jquery" />
<g:form action="assignIncentiveToItem" method="post" onsubmit="return validateForm();" >
	<g:hiddenField name="incentive" value="${result?.incentiveId}" />
	<g:hiddenField name="version" value="${result?.incentiveVersion}" />
	<g:hiddenField name="trackedItem" value="${result?.trackedItemId}" />
	<h3>${result?.person}</h3>
   	<div class="dialog">
		<div class="scan-box">
			<div class="scan-row">
	            <label for="type"><g:message code="incentive.type.label" default="Scan Incentive Barcode" /></label>
			</div>
            <div>
	            <g:textField name="code" value="${result?.incentiveBarcode}" />
            </div>
		</div>
		<div class="arrow-box">
			<img id="arrow-img" src="${resource(dir:'images',file:'go-next_100x100.png')}" alt="Arrow" />
		</div>
		<div class="scan-box">
			<div class="scan-row">
                   <label for="person"><g:message code="person.id.label" default="Scan Item Barcode" /></label>
			</div>
            <div>
				<g:textField name="trackedItemId" value="${result?.trackedItemId}" />					
            </div>
		</div>
		
		<div id="appointment-info">
			<div class="scan-box">
				<div class="scan-row">
                    <label for="appointment"><g:message code="appointment.id.label" default="Appointment" /></label>
				</div>
                <div>
					${result?.appointment}					
                </div>
			</div>
				
			<div class="arrow-box">
			</div>
			<div class="scan-box">
				<div class="scan-row">
                    <label for="incentiveDate"><g:message code="incentive.incentiveDate.label" default="Incentive Date" /></label>
				</div>
                <div>
					<g:datePicker name="incentiveDate"  value="${result?.appointmentDate}" precision="day" />					
                </div>
			</div>
		</div>
	</div>
	
	<div class="buttons">
		<span class="button"><g:submitButton name="create" class="save" value="Save" /></span>
	</div>
</g:form>