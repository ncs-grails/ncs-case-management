/**
 * Author: Aaron S. Timbo
 * Date Created: 2012-09-19
 * 
 * javascript support for contact info editing.
 */

jQuery.noConflict();

//var debug = true;
var debug = true;

jQuery(document).ready(function() {
	if (debug) {
		jQuery(".message").html("jQuery loaded!");
	}
	
	// Enable date picker for dynamic controls
	jQuery("input[id*=datepicker]").datepicker();
	
	// Handle change of active status event
	jQuery("input[id*=active]").change(function() {
		if (debug) {
			jQuery(".message").html("Toggle active for " + jQuery(this).attr('name'));
		}
		var id = getId(jQuery(this).attr('name'));
		if (debug) {
			jQuery(".message").html("ID set to " + id);
		}
		var datePickerElementId = '#datepicker_' + id;
		if (jQuery(this).is(':checked')) {
			jQuery(datePickerElementId).hide();
		} else {
			jQuery(datePickerElementId).show();
			jQuery(datePickerElementId).focus();			
		}
		
	});

	jQuery(".errors").hide();
	
	// Hide date entry fields for active records
	jQuery("input:checked").each(function() {
		var id = getId(jQuery(this).attr('name'));
		var datePickerElementId = '#datepicker_' + id;
		jQuery(datePickerElementId).hide();
	});
	
});

function getId(name) {
	var id = 0;
	var l = name.split('_');
	try {
		id = parseInt(l[1]);
	} catch (e) {
		jQuery(".errors").html("Error getting id from " + l[1] + " : " + e);
		jQuery(".errors").show();
	}
	return id;
}

function updateMessage() {
	jQuery(".message").html("Contact info updated successfully!");	
}