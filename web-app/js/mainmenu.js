/* 
 * Author: Aaron S. Timbo
 * Date Created: 2011-03-31
 * 
 * jQuery implemented on NCS Case Management main menu page.
 */

var defaultTab = "data-mngmt-tab";

$(document).ready(function(){	
	//alert("jQuery loaded");
	
	// Highlight default selected list item
	$('#data-mngmt').addClass('selected');
	
	// Hide all tabs except the default
	$('#tabs div').each(function() {
		if ($(this).attr('id') != defaultTab) {
			$(this).hide();			
		}
	});
	
	$('.container li').click(function(){
		// alert("do something here for id " + $(this).attr('id'));
		var menuItem = $(this).attr('id');
		$('#' + menuItem).addClass('selected');
		// Show tab for selected li
		var tab = menuItem + "-tab";
		$('#' + tab).show();
		// Hide all other tabs
		$('.menu-box li').each(function() {
			if (menuItem != $(this).attr('id')) {
				$(this).removeClass('selected');
				tab = $(this).attr('id') + "-tab";
				$('#' + tab).hide();				
			}
		});
	});

});