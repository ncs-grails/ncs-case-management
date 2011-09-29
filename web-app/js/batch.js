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
	
	$('input:checkbox[name^="itemId-"]').attr('checked', false);
	
	/*
	$("#itemsList tr").click(function(){
		
		if ( $(this).attr("type") != "checkbox" ) {
			
			// console.log("This.type: " + this.nodeName.toLowerCase() );
			
			var cb = $(this).find("input[type='checkbox']");
			
			if (!$(cb).is(':checked')) {
				$(cb).attr('checked', true);
			} else {
				$(cb).attr('checked', false);
			}
		}
	});
	*/
	
	$('form[name="editBatch"]').keypress(function(e){
		
		if (e.keyCode == 13) {
		    e.preventDefault();
		    return false;
		}
	 });

});



function checkItems() {
	
	var valid_du = $('input[name="valid.dwelling.id"]');
	var valid_p = $('input[name="valid.person.id"]');
	var valid_h = $('input[name="valid.household.id"]');	
	
	var du = $('input[name="dwellingUnit.id"]');
	var p = $('input[name="person.id"]');
	var h = $('input[name="household.id"]');

	var validItems = (valid_du.val() != "" ? ' Dwelling Units ' : '');
	
	validItems += (valid_p.val() != "" ? (validItems.indexOf('Dwelling') > -1 ? ' and Person(s)' : 'Person(s)') : '');
	validItems += (valid_h.val() != "" ? (validItems.indexOf('Dwelling') > -1 || validItems.indexOf('Person(s)') > -1 ? ' and Household(s)' : 'Household(s)') : '');
	
	var m = "";
	var w = "";
	
	var r = false;
	
	// Check if at least one item entered
	// Check if only one item entered
	// Check if the entered item is one of the valid Items
	
	if (du.val() || p.val() || h.val()) {
		
		if (du.val().length > 0 && valid_du.val() != ""
				|| p.val().length > 0 && valid_p.val() != ""
				|| h.val().length > 0 && valid_h.val() != "") {
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

