/**
 * Author: Aaron S. Timbo
 * Date Created: 2011-05-26
 * 
 * javascript support for ncs-event application
 */

jQuery.noConflict();

//var debug = true;
var debug = false;

jQuery(document).ready(function() {
	
	jQuery("#tabs").tabs({ selected: 0 });
	
	// Enable date picker for dynamic controls
	jQuery("#datepicker").datepicker();
	jQuery("#filledOutDatepicker").datepicker();
	jQuery("input[id*=eventDatepicker]").datepicker();
	jQuery("input[id*=eventResultDatepicker]").datepicker();
	jQuery("input[id*=dateResultEnteredDatepicker]").datepicker();
	
	// Handle event source changes -- display additional items as needed based on selected event source
	jQuery("#eventSource\\.id").change(function(){
		eventSourceChanged();
	});

	eventSourceChanged();
	
	if (debug) {
		alert("jQuery loaded");			
	}

});

function eventSourceChanged() {
	jQuery("#eventSourceOther").hide();
	var eventSource = jQuery("#eventSource\\.id").find(':selected').text();
	if (debug) {
		alert("eventSource::" + eventSource);
	}
	if (eventSource == 'Other') {
		jQuery("#eventSourceOther").show();			
	}	
}

function displayControls(e) {
    if (debug) {
    	alert("Setting controls to display");
    }

	// The response comes back as a bunch-o-JSON - list of objects
    var controlsToDisplay = eval("(" + e.responseText + ")"); // evaluate JSON
    if (debug) {
    	alert("controlsToDisplay::" + controlsToDisplay);
    }

	if (controlsToDisplay) {
        for (var i=0; i < controlsToDisplay.length; i++) {        	
        	var control = controlsToDisplay[i];
        	// Hide controls by default
        	var controlId = control.controlId;
        	hideEoiControls(controlId);
        	
            if (debug) {
            	alert("controlId::" + controlId);
            }

        	var element = '#eventCode' + controlId;
	    	if (control.useEventCode) {
	    		jQuery(element).removeClass('hidden');
	    	}
	    	element = '#eventDateDiv' + controlId;
            //if (debug) {
            //	alert("eventDateControl::" + element + '::useEventDate::' + control.useEventDate);
            //}
	    	if (control.useEventDate) {
	    		jQuery(element).removeClass('hidden');
		    	element = '#datePrecision' + controlId;
	    		jQuery(element).removeClass('hidden');
	    	}
	    	element = '#eventDescription' + controlId;
	    	if (control.useEventDescription) {
	    		jQuery(element).removeClass('hidden');
	    	}
	    	element = '#eventPickOne' + controlId;
	    	if (control.useEventPickOne) {	    		
	    		jQuery(element).removeClass('hidden');
	    		// Add select options for this event type
	    		element = '#eventPickOneOptions' + controlId;
	    		// Create a list for new options and add default option
                var newOptions = { '': '-Please Choose-' };            
                // Get list of pick one values for this event type
                var eventPickOneList = control.eventPickOneList;
                if (eventPickOneList.length > 0) {
	                if (debug) {
	                	alert("eventPickOneList::" + eventPickOneList);
	                }
	                // Add pick one values to options list
		    		for (j=0; j < eventPickOneList.length; j++) {
		    			var eventPickOne = eventPickOneList[j];
		    			newOptions[eventPickOne.id] = eventPickOne.name
		    		}
		    		var select = jQuery(element);
		    		if(select.prop) {
		    			var options = select.prop('options');	    			
		    		}
		    		else {
		    			var options = select.attr('options');
		    		}
		    		// Clear existing options
		    		jQuery('option', select).remove();
		    		// Append new options to select element
		    		jQuery.each(newOptions, function(val, text) {
		    			options[options.length] = new Option(text, val);
		    		});
	    		}
	    	}
        }    	
    }
}

function hideEoiControls(controlId) {
	if (debug) {
		alert("Hiding EOI controls");
	}
	var element = '#eventCode' + controlId;
	if (! jQuery(element).hasClass('hidden')) {
		jQuery(element).addClass('hidden');		
	}
	element = '#eventDateDiv' + controlId;
	if (! jQuery(element).hasClass('hidden')) {
		jQuery(element).addClass('hidden');		
	}
	element = '#datePrecision' + controlId;
	if (! jQuery(element).hasClass('hidden')) {
		jQuery(element).addClass('hidden');		
	}
	element = '#eventDescription' + controlId;
	if (! jQuery(element).hasClass('hidden')) {
		jQuery(element).addClass('hidden');		
	}
	element = '#eventPickOne' + controlId;
	if (! jQuery(element).hasClass('hidden')) {
		jQuery(element).addClass('hidden');		
	}
}
