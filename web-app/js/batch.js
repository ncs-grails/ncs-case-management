/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
	$("#id").focus();

	/* resubmit form on date change */
	$('select[name^="referenceDate_"]').change(function(){
            $('form[name="mailDateForm"]').submit();
	});

});



function checkItems(dwellingUnit, person, household) {
	
	var du = $('input[name="dwellingUnit.id"]');
	var p = $('input[name="person.id"]');
	var h = $('input[name="household.id"]');

	var validItems = (dwellingUnit!= null ? ' Dwelling Units ' : '');
	
	validItems += (person != null ? (validItems.indexOf('Dwelling') > -1 ? ' and Person(s)' : 'Person(s)') : '');
	validItems += (household != null ? (validItems.indexOf('Dwelling') > -1 || validItems.indexOf('Person(s)') > -1 ? ' and Household(s)' : 'Household(s)') : '');
	
	var m = "";
	var w = "";
	
	var r = false;
	
	// Check if at least one item entered
	// Check if only one item entered
	// Check if the entered item is one of the valid Items
	
	if (du.val() || p.val() || h.val()) {
		if (du.val().length > 0 && dwellingUnit != null
				|| p.val().length > 0 && person != null
				|| h.val().length > 0 && household != null) {

			return true;
		} else {
			return warn(validItems, du, p, h);
		}
	}
}

function warn(validItems, du, p, h) {
	
	var m = '<div>This batch created for '; 
		m += validItems + '. You are adding';
		
		if (du.val().length) {
			m += ' Dwelling Unit ';
		} else if (p.val().length > 0) {
			m += ' Person ';
		} else if (h.val().length > 0) {
			m += ' Household ';
		}
		
		m += 'to this batch. Proceed?</div>'; 
	
	$(m).dialog({
        title: "Adding New Tracked Item",
        modal: true,
        height:250,
        width:450,
        overlay: {
            background: '#000',
            opacity: 0.5
        },
        buttons: {
        	
            'Yes': function() {
            	$(this).dialog('close').remove();
            	
            	var action = $('form[name="editBatch"]').attr('action').replace('index', 'addItem');
            	$('form[name="editBatch"]').attr('action', action)
                $('form[name="editBatch"]').submit();
            },
            'No': function() {
            	$(this).dialog('close').remove();
                return false;
            }
        }
    });	
	
	return false;
}

