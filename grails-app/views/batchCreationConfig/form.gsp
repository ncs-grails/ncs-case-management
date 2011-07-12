<%@ page import="edu.umn.ncs.BatchCreationConfig"%>
<div id="tabs">
	<ul>
		<li><a href="#tabs-general">General</a>
		</li>
		<li><a href="#tabs-selection">Selection</a>
		</li>
		<li><a href="#tabs-parent">Parent</a>
		</li>
		<li><a href="#tabs-restriction">Restrictions</a>
		</li>
		<li><a href="#tabs-comments">Comments</a>
		</li>
		<li><a href="#tabs-report">Reporting</a>
		</li>
		<li><a href="#tabs-studyyear">Study Year</a>
		</li>
	</ul>
	<div id="tabs-general">
		<div class="prop">
			<span class="name"> <label for="name"><g:message
						code="batchCreationConfig.name.label" default="Name" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'name', 'errors')}">
				<g:textField name="name" size="65" maxlength="128"
					value="${batchCreationConfigInstance?.name}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="active"><g:message
						code="batchCreationConfig.active.label" default="Active" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'active', 'errors')}">
				<g:checkBox name="active"
					value="${batchCreationConfigInstance?.active}" /> </span>
		</div>

		<fieldset class="maroonBorder">
			<legend>Instrument Basics</legend>
			<div class="prop">
				<span class="name"> <label for="direction"><g:message
							code="batchCreationConfig.direction.label" default="Direction" />
				</label> </span> <span
					class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'direction', 'errors')}">
					<g:select name="direction.id"
						from="${edu.umn.ncs.BatchDirection.list()}" optionKey="id"
						value="${batchCreationConfigInstance?.direction?.id}" /> </span>
			</div>

			<div class="prop">
				<span class="name"> <label for="format"><g:message
							code="batchCreationConfig.format.label" default="Format" />
				</label> </span> <span
					class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'format', 'errors')}">
					<g:select name="format.id"
						from="${edu.umn.ncs.InstrumentFormat.list()}" optionKey="id"
						value="${batchCreationConfigInstance?.format?.id}" /> </span>
			</div>

			<div class="prop">
				<span class="name"> <label for="instrument"><g:message
							code="batchCreationConfig.instrument.label" default="Instrument" />
				</label> </span> <span
					class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'instrument', 'errors')}">
					<g:select name="instrument.id"
						from="${edu.umn.ncs.Instrument.list()}" optionKey="id"
						value="${batchCreationConfigInstance?.instrument?.id}" /> </span>
			</div>

			<div class="prop">
				<span class="name"> <label for="isInitial"><g:message
							code="batchCreationConfig.isInitial.label" default="Is Initial" />
				</label> </span> <span
					class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'isInitial', 'errors')}">
					<g:select name="isInitial.id"
						from="${edu.umn.ncs.IsInitial.list()}" optionKey="id"
						value="${batchCreationConfigInstance?.isInitial?.id}" /> </span>
			</div>

			<div class="prop">
				<span class="name"> <label for="isResend"><g:message
							code="batchCreationConfig.isResend.label" default="Is Resend" />
				</label> </span> <span
					class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'isResend', 'errors')}">
					<g:checkBox name="isResend"
						value="${batchCreationConfigInstance?.isResend}" /> </span>
			</div>
		</fieldset>
	</div>

	<div id="tabs-selection">

		<div class="prop">
			<span class="name"> <label for="automaticSelection"><g:message
						code="batchCreationConfig.automaticSelection.label"
						default="Automatic Selection" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'automaticSelection', 'errors')}">
				<g:checkBox name="automaticSelection"
					value="${batchCreationConfigInstance?.automaticSelection}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="selectionQuery"><g:message
						code="batchCreationConfig.selectionQuery.label"
						default="Selection Query" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'selectionQuery', 'errors')}">
				<g:textArea name="selectionQuery" cols="80" rows="5"
					value="${batchCreationConfigInstance?.selectionQuery}" /> </span>
			<p>Columns that are processed: dwelling_unit, person, household,
				parent_item, expire_date, study_year</p>
		</div>

		<div class="prop">
			<span class="name"> <label for="manualSelection"><g:message
						code="batchCreationConfig.manualSelection.label"
						default="Manual Selection" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'manualSelection', 'errors')}">
				<g:checkBox name="manualSelection"
					value="${batchCreationConfigInstance?.manualSelection}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="optionalSelection"><g:message
						code="batchCreationConfig.optionalSelection.label"
						default="Optional Selection" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'optionalSelection', 'errors')}">
				<g:checkBox name="optionalSelection"
					value="${batchCreationConfigInstance?.optionalSelection}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="postGenerationQuery"><g:message
						code="batchCreationConfig.postGenerationQuery.label"
						default="Post Generation Query" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'postGenerationQuery', 'errors')}">
				<g:textArea name="postGenerationQuery" cols="80" rows="5"
					value="${batchCreationConfigInstance?.postGenerationQuery}" /> </span>
			<p>This query will run after the batch has been generated.  There is a helper keyword available for you
			named ':batchId', so if you place the string ':batchId' (without the quotes) in the query it will be replaced
			with the batch ID of the primary batch generated as part of the configuration.</p>
		</div>
	</div>
	<div id="tabs-parent">
		<div class="prop">
			<span class="name"> <label for="useParentItem"> <g:message
						code="batchCreationConfig.useParentItem.label"
						default="Use Parent Item" /> </label> - If you check this, you have to
				pass the column 'parent_item' in the selection criteria... OR, you
				have to fill in as many of the options below. Passing the
				'parent_item' is preferred because it is faster and more accurate. </span>
			<span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'useParentItem', 'errors')}">
				<g:checkBox name="useParentItem"
					value="${batchCreationConfigInstance?.useParentItem}" /> </span>
		</div>

		<p>You only have to specify the following options if you checked
			the 'use parent item' checkbox, but are NOT passing the parent_item
			column in the selection criteria. Typically this is only done for
			manual batch generation. If it's an automatic selection, you should
			always pass the tracked item ID of the parent item in the selection
			query as the column name 'parent_item'.</p>

		<div class="prop">
			<span class="name"> <label for="parentInstrument"><g:message
						code="batchCreationConfig.parentInstrument.label"
						default="Parent Instrument" />
			</label> </span>
			<span class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentInstrument', 'errors')}">
				<g:select name="parentInstrument.id"
					from="${edu.umn.ncs.Instrument.list()}" optionKey="id"
					value="${batchCreationConfigInstance?.parentInstrument?.id}"
					noSelection="['null': '']" />
			</span>
		</div>

		<div class="prop">
			<span class="name"> <label for="parentDirection"> <g:message
						code="batchCreationConfig.parentDirection.label"
						default="Parent Direction" /> </label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentDirection', 'errors')}">
				<g:select name="parentDirection.id"
					from="${edu.umn.ncs.BatchDirection.list()}" optionKey="id"
					value="${batchCreationConfigInstance?.parentDirection?.id}"
					noSelection="['null': '']" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="parentResult"> <g:message
						code="batchCreationConfig.parentResult.label"
						default="Parent Result" /> </label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentResult', 'errors')}">
				<g:select name="parentResult.id" from="${edu.umn.ncs.Result.list()}"
					optionKey="id" optionValue="name"
					value="${batchCreationConfigInstance?.parentResult?.id}"
					noSelection="['null': '']" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="parentFormat"><g:message
						code="batchCreationConfig.parentFormat.label"
						default="Parent Format" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentFormat', 'errors')}">
				<g:select name="parentFormat.id"
					from="${edu.umn.ncs.InstrumentFormat.list()}" optionKey="id"
					value="${batchCreationConfigInstance?.parentFormat?.id}"
					noSelection="['null': '']" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="parentInitial"><g:message
						code="batchCreationConfig.parentInitial.label"
						default="Parent Initial" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'parentInitial', 'errors')}">
				<g:select name="parentInitial.id"
					from="${edu.umn.ncs.IsInitial.list()}" optionKey="id"
					value="${batchCreationConfigInstance?.parentInitial?.id}"
					noSelection="['null': '']" /> </span>
		</div>
	</div>
	
	<div id="tabs-restriction">
		<p>These options are good for adding restrictions on what goes into a batch for manual generation.
		It's a way to keep someone from manually generating an item that they shouldn't.</p>
		<div class="prop">
			<span class="name"> <label for="allowMultiplePersonPerBatch"><g:message
						code="batchCreationConfig.allowMultiplePersonPerBatch.label"
						default="Allow Multiple Person Per Batch" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'allowMultiplePersonPerBatch', 'errors')}">
				<g:checkBox name="allowMultiplePersonPerBatch"
					value="${batchCreationConfigInstance?.allowMultiplePersonPerBatch}" />
			</span>
		</div>
		<div class="prop">
			<span class="name"> <label for="oneBatchEventParentItem"><g:message
						code="batchCreationConfig.oneBatchEventParentItem.label"
						default="One Batch Event Parent Item" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'oneBatchEventParentItem', 'errors')}">
				<g:checkBox name="oneBatchEventParentItem"
					value="${batchCreationConfigInstance?.oneBatchEventParentItem}" />
			</span>
		</div>

		<div class="prop">
			<span class="name"> <label for="oneBatchEventPerYear"><g:message
						code="batchCreationConfig.oneBatchEventPerYear.label"
						default="One Batch Event Per Year" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'oneBatchEventPerYear', 'errors')}">
				<g:checkBox name="oneBatchEventPerYear"
					value="${batchCreationConfigInstance?.oneBatchEventPerYear}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="oneBatchEventPerson"><g:message
						code="batchCreationConfig.oneBatchEventPerson.label"
						default="One Batch Event Person" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'oneBatchEventPerson', 'errors')}">
				<g:checkBox name="oneBatchEventPerson"
					value="${batchCreationConfigInstance?.oneBatchEventPerson}" /> </span>
		</div>

		<p>This option is a way to limit the number of items generated.  It grabs 
		the first N items.  It also allows the person generating the batch to 
		adjust the amount on the fly rather than tweak a selection criteria 
		query just to change the number of items to generate.  </p>
		<div class="prop">
			<span class="name"> <label for="maxPieces"><g:message
						code="batchCreationConfig.maxPieces.label" default="Max Pieces" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'maxPieces', 'errors')}">
				<g:textField name="maxPieces"
					value="${fieldValue(bean: batchCreationConfigInstance, field: 'maxPieces')}" />
			</span>
		</div>

		<p>This option tells batch generation that the column 'expire_date' will be passed as part of the selection criteria.
			This makes it so that you can have items automatically "expire" so that the expectation of their fulfillment goes
			away after a certain day.  Examples include annual surveys, calling forms that shouldn't be worked on after 90 
			days, etc...</p>
		<div class="prop">
			<span class="name"> <label for="useExpiration"><g:message
						code="batchCreationConfig.useExpiration.label"
						default="Item Expires?" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'useExpiration', 'errors')}">
				<g:checkBox name="useExpiration"
					value="${batchCreationConfigInstance?.useExpiration}" /> </span>
		</div>
	</div>
	<div id="tabs-comments">
		<p>This will create pre-defined comments for all items in the batch.  This is usefull if you have a common shared
		form that only slightly differs in verbiage from one batch type to another.  Then you can keep on single merge master
		and only the following comments will vary.  For example, a generic call sheet.</p>
		<div class="prop">
			<span class="name"> <label for="defaultReason"><g:message
						code="batchCreationConfig.defaultReason.label"
						default="Default Reason" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'defaultReason', 'errors')}">
				<g:textField name="defaultReason" size="40"
					value="${batchCreationConfigInstance?.defaultReason}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="defaultInstructions"><g:message
						code="batchCreationConfig.defaultInstructions.label"
						default="Default Instructions" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'defaultInstructions', 'errors')}">
				<g:textField name="defaultInstructions" size="40"
					value="${batchCreationConfigInstance?.defaultInstructions}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="defaultComments"><g:message
						code="batchCreationConfig.defaultComments.label"
						default="Default Comments" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'defaultComments', 'errors')}">
				<g:textField name="defaultComments" size="40"
					value="${batchCreationConfigInstance?.defaultComments}" /> </span>
		</div>
	</div>
	<div id="tabs-report">
		<p>Check this if you want to automatically mark the item as being mailed as soon as it is generated.
		   If it is not, then someone will have to manually enter the mail date when it actually goes out.</p>
		<div class="prop">
			<span class="name"> <label for="autoSetMailDate"><g:message
						code="batchCreationConfig.autoSetMailDate.label"
						default="Auto Set Mail Date" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'autoSetMailDate', 'errors')}">
				<g:checkBox name="autoSetMailDate"
					value="${batchCreationConfigInstance?.autoSetMailDate}" /> </span>
		</div>

		<p>If you checked "Auto Set Mail Date", you can have the actual mailed date be the next day or 
		two by adjusting this number.  0 is today, 1 is tomorrow, etc...</p>
		<div class="prop">
			<span class="name"> <label for="mailDateDaysShift"><g:message
						code="batchCreationConfig.mailDateDaysShift.label"
						default="Mail Date Days Shift" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'mailDateDaysShift', 'errors')}">
				<g:textField name="mailDateDaysShift"
					value="${fieldValue(bean: batchCreationConfigInstance, field: 'mailDateDaysShift')}" />
			</span>
		</div>

		<p>This doesn't do anything currently.</p>
		<div class="prop">
			<span class="name"> <label for="batchReportsToPrint"><g:message
						code="batchCreationConfig.batchReportsToPrint.label"
						default="Batch Reports To Print" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'batchReportsToPrint', 'errors')}">
				<g:textField name="batchReportsToPrint"
					value="${fieldValue(bean: batchCreationConfigInstance, field: 'batchReportsToPrint')}" />
			</span>
		</div>

		<p>This determines whether or not a tracking document record will be placed in the merge source, and if so
		   who's contact information will go on it.</p>
		<div class="prop">
			<span class="name"> <label for="generateTrackingDocument"><g:message
						code="batchCreationConfig.generateTrackingDocument.label"
						default="Generate Tracking Document" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'generateTrackingDocument', 'errors')}">
				<g:checkBox name="generateTrackingDocument"
					value="${batchCreationConfigInstance?.generateTrackingDocument}" />
			</span>
		</div>

		<div class="prop">
			<span class="name"> <!-- TODO: Add no selection option --> <label
				for="trackingDocumentRecipient"><g:message
						code="batchCreationConfig.generateTrackingDocument.label"
						default="Select Tracking Document Recipient" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'trackingDocumentRecipient', 'errors')}">
				<g:select name="trackingDocumentRecipient"
					from="${edu.umn.ncs.TrackingDocumentRecipient.list()}"
					onSelection="${['null':'Select one or more...']}" optionKey="id"
					value="${batchCreationConfigInstance.recipients}" /> </span>
		</div>

	</div>

	<div id="tabs-studyyear">
		<p>This determines whether a "study year" will be assigned to the tracked item when it is generated.
		  if this box is checked, then the column 'study_year' needs to be passed with the selection criteria.
		  If nothing is passed, the current study year is used.</p>
		<div class="prop">
			<span class="name"> <label for="useStudyYear"><g:message
						code="batchCreationConfig.useStudyYear.label"
						default="Use Study Year" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'useStudyYear', 'errors')}">
				<g:checkBox name="useStudyYear"
					value="${batchCreationConfigInstance?.useStudyYear}" /> </span>
		</div>

		<div class="prop">
			<span class="name"> <label for="requireCurrentStudyYear"><g:message
						code="batchCreationConfig.requireCurrentStudyYear.label"
						default="Require Current Study Year" />
			</label> </span> <span
				class="value ${hasErrors(bean: batchCreationConfigInstance, field: 'requireCurrentStudyYear', 'errors')}">
				<g:checkBox name="requireCurrentStudyYear"
					value="${batchCreationConfigInstance?.requireCurrentStudyYear}" />
			</span>
		</div>

	</div>
</div>
