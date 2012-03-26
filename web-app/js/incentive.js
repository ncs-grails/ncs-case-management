/**
* Incentive jQuery
* Date Created: 2011-08-04
* By: Aaron S. Timbo
*/

var debug = false;
var incentive = false;
var instrument = false;
var incentiveElementId = 0;
var barcodeCount = 0;
var spinnerVisible = false;
var selectAll = false;

$(document).ready(function() {
	if (debug) {
		alert('jQuery loaded');
	}

    // Enable table sorting
    $("#list-table").tablesorter({widgets: ['zebra']});

    // Refresh zebra striping
    $("#list-table th").click(function() {
        $('tbody.zebra tr').removeClass('odd');
        zebraRows('tbody.zebra tr:odd', 'odd');
    });

    //default each row to visible
    $('tbody tr').addClass('visible');

    zebraRows('tbody.zebra tr:odd', 'odd');

    // Set default value of search box
    $('#filter').val('Search...');
    
    // Clear value of search box when activated
    $('#filter').focus(function(event) { 
    	$(this).val('');
    	$(this).css({'color':'#666'});
    });

    // Filter table lists
    $('#filter').keyup(function(event) {
    	if (debug) {
    		alert('filtering list...');
    	}
        //if esc is pressed or nothing is entered
        if (event.keyCode == 27 || $(this).val() == '') {
          //if esc is pressed we want to clear the value of search box
          $(this).val('');

          //we want each row to be visible because if nothing
          //is entered then all rows are matched.
          $('tbody tr').removeClass('visible').show().addClass('visible');
        }

        //if there is text, lets filter
        else {
          filter('tbody tr', $(this).val());
          // Count the visible items
          var numItems = $('.visible').length
          $('.total-count').html(numItems);
        }

        //reapply zebra rows
        $('.visible td').removeClass('odd');
        zebraRows('.visible:odd td', 'odd');
    });

    // Handle checkbox events for printable incentive list
    $("input[id*='checkBox_']").click(function(event) {
        $('.noSelectedIncentives').hide();
    	updateIncentiveList(this);
    });
	
    // Un/select all incentives to print
    $(".selectIncentivesToPrint").click(function(event){
    	if (selectAll) {
            $('.noSelectedIncentives').hide();
    		$(this).html('Unselect All');
    		selectAll = false; 
    		$("input[id*='checkBox_']").each(function(event){
    			$(this).attr('checked', true);
    		});
    		// Add all original incentives to the list
    		var incentiveList = groovyListToArray($('#incentivesToPrintOrig').val());
			$('#incentivesToPrint').val(incentiveList);
    		
    	} else {
    		$(this).html('Select All');
    		selectAll = true;    		
    		$("input[id*='checkBox_']").each(function(event){
    			$(this).attr('checked', false);
    		});
    		// Clear out incentives list
			$('#incentivesToPrint').val('');
    	}    	
    });
    
    // Hide no selected incentive message by default
    $('.noSelectedIncentives').hide();
    
//    $('#barcode').change(function() {
	// Only do a save on enter
    $('#barcode').keyup(function(event) {
        // get the value entered in the field
        var barcodeValue = $(this).val();
    	if (event.keyCode == '13' && barcodeValue != "") {
        	$('.scanned-header').show();

    		// get the receipt number
    		var receiptNumberInstance = $("#receiptNumber").val();
    		// get the receipt number
    		var incentiveTypeInstance = $("#incentiveTypeInstance").val();
    		// get the receipt number
    		var amountInstance = $("#amount").val();
            // clear the input
            $(this).val("");
            // set the focus back to the input field
            $(this).focus();
            // increment the counter
            incentiveElementId++;

            var divId = incentiveElementId;
            // create an element stating that we are trying to receipt it
            var strHtml = '<div id="scan-' + incentiveElementId + '" class="item-' + barcodeValue + '">';
            //strHtml += barcodeValue;
            strHtml += '<span style="padding-left:3em;;" id="scan-' + incentiveElementId + '-status">...Processing</span></div>';

            $('#resultLog').prepend(strHtml);

            ///////////////////////////
            // send the ajax request //
            ///////////////////////////

            // get the URL to send it to
            var url = $('form[name="incentiveForm"]').attr("action");
            // build the data
            var data = { "id": barcodeValue, "divId": incentiveElementId, "receiptNumberInstance": receiptNumberInstance, "incentiveTypeInstance": incentiveTypeInstance, "amountInstance": amountInstance };

            $.getJSON(url, data, function(data, textStatus){

                // Possible outcomes of textStatus are:
                // "success"
                // "timeout"
                // "error"
                // "notmodified"
                // "parsererror"

                if (textStatus == "success") {
                    var resultDivId = data.divId
                    if (debug) {
                    	alert("data::" + data);
                    }
                    
                    if (data.success) {
                    	// update scanned count
                    	barcodeCount += 1;
                    	if (barcodeCount == 1) {
                        	$('.scanned-header').html(barcodeCount + " Scanned Incentive");                		
                    	}
                    	else {
                        	$('.scanned-header').html(barcodeCount + " Scanned Incentives");                		
                    	}
                    	
                        var resultText = "Saved";
                        if (data.amount){
                            resultText += ", Barcode: " + data.barcode + ", $" + data.amount + " " + data.incentiveType;                    	
                        }
                        else {
                            resultText += ", Barcode: " + data.barcode + ", (no value specified) " + data.incentiveType;                    	
                        }

                        $("#scan-" + resultDivId + "-status").html(resultText);
                    } else {
                        $("#scan-" + divId + "-status").addClass('scan-error');
                        $("#scan-" + resultDivId + "-status").html("Failed to save incentive: " + data.errorText);
                    }

                } else if (textStatus == "timeout") {
                    alert(2);
                    $("#scan-" + divId + "-status").addClass('scan-error');
                    $("#scan-" + divId + "-status").html("Request timed out.");
                } else if (textStatus == "parsererror") {
                    alert(3);
                    $("#scan-" + divId + "-status").addClass('scan-error');
                    $("#scan-" + divId + "-status").html("Error parsing response from server, please retry.");
                } else if (textStatus == "notmodified") {
                    alert(4);
                    $("#scan-" + divId + "-status").html("What? Not modified? OK.");
                } else {
                    alert(5);
                    $("#scan-" + divId + "-status").addClass('scan-error');
                    $("#scan-" + divId + "-status").html("An error occured on the server.");
                }    		
            });
    	}

        //alert(barcodeValue);
        return false;
        
    });

    $('#checkoutBarcode').keyup(function(event) {
        // get the value entered in the field
        var barcodeValue = $(this).val();
    	if (event.keyCode == '13' && barcodeValue != "") {
	    	$('.scanned-header').show();
	
			// get the receipt number
			var receiptNumberInstance = $("#receiptNumber").val();
			// get the receipt number
			var incentiveTypeInstance = $("#incentiveTypeInstance").val();
			// get the receipt number
			var amountInstance = $("#amount").val();
			// get the interviewer
			var checkedOutToInstance = $("#checkedOutTo").val();
	        // clear the input
	        $(this).val("");
	        // set the focus back to the input field
	        $(this).focus();
	        // increment the counter
	        incentiveElementId++;
	
	        var divId = incentiveElementId;
	        // create an element stating that we are trying to receipt it
	        var strHtml = '<div id="scan-' + incentiveElementId + '" class="item-' + barcodeValue + '">';
	        //strHtml += barcodeValue;
	        strHtml += '<span style="padding-left:3em;;" id="scan-' + incentiveElementId + '-status">...Processing</span></div>';
	
	        $('#resultLog').prepend(strHtml);
	
	        ///////////////////////////
	        // send the ajax request //
	        ///////////////////////////
	
	        // get the URL to send it to
	        var url = $('form[name="giftCardForm"]').attr("action");
	        // build the data
	        var data = { "id": barcodeValue, "divId": incentiveElementId, "receiptNumberInstance": receiptNumberInstance, "incentiveTypeInstance": incentiveTypeInstance, "amountInstance": amountInstance, "checkedOutToInstance": checkedOutToInstance };
	
	        $.getJSON(url, data, function(data, textStatus){
	
	            // Possible outcomes of textStatus are:
	            // "success"
	            // "timeout"
	            // "error"
	            // "notmodified"
	            // "parsererror"
	
	            if (textStatus == "success") {
	                var resultDivId = data.divId
	
	                if (data.success) {
	                	// update scanned count
	                	barcodeCount += 1;
	                	if (barcodeCount == 1) {
	                    	$('.scanned-header').html(barcodeCount + " Checked Out Incentive");                		
	                	}
	                	else {
	                    	$('.scanned-header').html(barcodeCount + " Checked Out Incentives");                		
	                	}
	                	
	                    var resultText = "Checked Out";
	                    if (data.amount){
	                        resultText += ", Barcode: " + data.barcode + ", $" + data.amount + " " + data.incentiveType + " checked out to " + data.checkedOutTo;                    	
	                    }
	                    else {
	                        resultText += ", Barcode: " + data.barcode + ", (no value specified) " + data.incentiveType + " out to " + data.checkedOutTo;                    	
	                    }
	
	                    $("#scan-" + resultDivId + "-status").html(resultText);
	                } else {
	                    $("#scan-" + divId + "-status").addClass('scan-error');
	                    $("#scan-" + resultDivId + "-status").html("Failed to check out incentive: " + data.errorText);
	                }
	
	            } else if (textStatus == "timeout") {
	                alert(2);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("Request timed out.");
	            } else if (textStatus == "parsererror") {
	                alert(3);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("Error parsing response from server, please retry.");
	            } else if (textStatus == "notmodified") {
	                alert(4);
	                $("#scan-" + divId + "-status").html("What? Not modified? OK.");
	            } else {
	                alert(5);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("An error occured on the server.");
	            }
	        });        
		}
	
	    //alert(barcodeValue);
	    return false;
    });

    $('#checkinBarcode').keyup(function(event) {
        // get the value entered in the field
        var barcodeValue = $(this).val();
    	if (event.keyCode == '13' && barcodeValue != "") {
	    	$('.scanned-header').show();
	
	        // clear the input
	        $(this).val("");
	        // set the focus back to the input field
	        $(this).focus();
	        // increment the counter
	        incentiveElementId++;
	
	        var divId = incentiveElementId;
	        // create an element stating that we are trying to receipt it
	        var strHtml = '<div id="scan-' + incentiveElementId + '" class="item-' + barcodeValue + '">';
	        //strHtml += barcodeValue;
	        strHtml += '<span style="padding-left:3em;;" id="scan-' + incentiveElementId + '-status">...Processing</span></div>';
	
	        $('#resultLog').prepend(strHtml);
	
	        ///////////////////////////
	        // send the ajax request //
	        ///////////////////////////
	
	        // get the URL to send it to
	        var url = $('form[name="incentiveForm"]').attr("action");
	        // build the data
	        var data = { "id": barcodeValue, "divId": incentiveElementId };
	
	        $.getJSON(url, data, function(data, textStatus){
	
	            // Possible outcomes of textStatus are:
	            // "success"
	            // "timeout"
	            // "error"
	            // "notmodified"
	            // "parsererror"
	
	            if (textStatus == "success") {
	                var resultDivId = data.divId
	
	                if (data.success) {
	                	// update scanned count
	                	barcodeCount += 1;
	                	if (barcodeCount == 1) {
	                    	$('.scanned-header').html(barcodeCount + " Checked In Incentive");                		
	                	}
	                	else {
	                    	$('.scanned-header').html(barcodeCount + " Checked In Incentives");                		
	                	}
	                	
	                    var resultText = "Checked In";
	                    if (data.amount){
	                        resultText += ", Barcode: " + data.barcode + ", $" + data.amount + " " + data.incentiveType;                    	
	                    }
	                    else {
	                        resultText += ", Barcode: " + data.barcode + ", (no value specified) " + data.incentiveType;                    	
	                    }
	
	                    $("#scan-" + resultDivId + "-status").html(resultText);
	                } else {
	                    $("#scan-" + divId + "-status").addClass('scan-error');
	                    $("#scan-" + resultDivId + "-status").html("Failed to check in incentive: " + data.errorText);
	                }
	
	            } else if (textStatus == "timeout") {
	                alert(2);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("Request timed out.");
	            } else if (textStatus == "parsererror") {
	                alert(3);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("Error parsing response from server, please retry.");
	            } else if (textStatus == "notmodified") {
	                alert(4);
	                $("#scan-" + divId + "-status").html("What? Not modified? OK.");
	            } else {
	                alert(5);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("An error occured on the server.");
	            }
	        });        
		}
	
	    //alert(barcodeValue);
	    return false;
    });

    $('#receiptNumber').keyup(function(event) {
        // get the value entered in the field
        var receiptNumber = $(this).val();
    	if (event.keyCode == '13' && receiptNumber != "") {
	    	$('.scanned-header').show();
	
	        // clear the input
	        $(this).val("");
	        // set the focus back to the input field
	        $(this).focus();
	
	        var divId = incentiveElementId;
	        // create an element stating that we are trying to receipt it
	        var strHtml = '<div id="scan-' + incentiveElementId + '" class="item-' + receiptNumber + '">';
	        //strHtml += barcodeValue;
	        strHtml += '<span style="padding-left:3em;;" id="scan-' + incentiveElementId + '-status">...Processing</span></div>';
	
	        $('#resultLog').prepend(strHtml);
	
	        ///////////////////////////
	        // send the ajax request //
	        ///////////////////////////
	
	        // get the URL to send it to
	        var url = $('form[name="incentiveForm"]').attr("action");
	        // build the data
	        var data = { "id": receiptNumber, "divId": incentiveElementId };
	
	        $.getJSON(url, data, function(data, textStatus){
	
	            // Possible outcomes of textStatus are:
	            // "success"
	            // "timeout"
	            // "error"
	            // "notmodified"
	            // "parsererror"
	
	            if (textStatus == "success") {
	                var resultDivId = data.divId
	
	                if (data.success) {
                    	$('.scanned-header').html("Incentives Activated by Receipt Number");                		
	                	
	                    var resultText = "Activated " + data.incentiveCount;
	                    alert("incentiveCount::" + parseInt(data.incentiveCount));
	                    if (parseInt(data.incentiveCount) == 1) {
	                    	resultText += " incentive ";
	                    }
	                    else {
	                    	resultText += " incentives ";	                    	
	                    }
	                    resultText += "with Receipt Number: " + data.receiptNumber;                    	
	
	                    $("#scan-" + resultDivId + "-status").html(resultText);
	                } else {
	                    $("#scan-" + divId + "-status").addClass('scan-error');
	                    $("#scan-" + resultDivId + "-status").html("Failed to activate incentives: " + data.errorText);
	                }
	
	            } else if (textStatus == "timeout") {
	                alert(2);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("Request timed out.");
	            } else if (textStatus == "parsererror") {
	                alert(3);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("Error parsing response from server, please retry.");
	            } else if (textStatus == "notmodified") {
	                alert(4);
	                $("#scan-" + divId + "-status").html("What? Not modified? OK.");
	            } else {
	                alert(5);
	                $("#scan-" + divId + "-status").addClass('scan-error');
	                $("#scan-" + divId + "-status").html("An error occured on the server.");
	            }
	        });        
		}
	
	    //alert(barcodeValue);
	    return false;
    });
    
	$('#code').keyup(function(event){
		if ($('#code').val()) {
			// Change the arrow image
			$('#arrow-img').attr('src','../images/go-next_100x100.png');
			incentive = true;
	    	if (event.keyCode == '13') {
	    		if ($('#trackedItemId').val()) {
        			$('#incentiveForm').submit();
	    		}
	    		else {
			        $('#trackedItemId').focus();	    			    			
	    		}
	    	}
		}
		else {
			$('#arrow-img').attr('src','../images/go-next-grey_100x100.png');			
			incentive = false;
		}
		return false;
	});
	
	$('#trackedItemId').keyup(function(event){
    	if (event.keyCode == '13') {
    		if ($('#code').val()) {
        		if ($('#trackedItemId').val()) {
    				incentive = true;
        			$('#incentiveForm').submit();
        		}
        		else {
    		        $('#trackedItemId').focus();	    		
        		}
    		}
    		else {
    			// Change the arrow image
    			$('#arrow-img').attr('src','../images/go-next-grey_100x100.png');			
		        $('#code').focus();
				incentive = false;
    		} 
		}
		return false;
	});	
	
	//$('#data-errors').hide();
	$('#assign-status').hide();
	$('#appointment-info').hide();
	$('#searching').hide();
	$('#notFound').hide();
	//$('.buttons').hide();
	
	$('#trackedItemBarcode').keyup(function(event){
		$('#data-errors').hide();

		// Get the entered person id
		var trackedItemIdValue = $(this).val();
    	if (event.keyCode == '13' && trackedItemIdValue != "") {
			
			// Get all appointments for the selected person
	
			///////////////////////////
	        // send the ajax request //
	        ///////////////////////////
	
	        // get the URL to send it to
	        var url = $('form[name="appointmentForm"]').attr("action");
	        // build the data
	        var data = { "id": personIdValue };
	        
	        $("#appointment-status").load(url, data, function(response, textStatus, xhr){
	        	$(this).hide();
	            // Possible outcomes of textStatus are:
	            // "success"
	            // "timeout"
	            // "error"
	            // "notmodified"
	            // "parsererror"
	
	            if (textStatus == "success") {
	                // Clear the appointment select list
	            	$('#appointments').empty();
	            	// Parse the returned JSON
	            	var json = jQuery.parseJSON(response);            	
	            	if (debug) {
	            		alert('json::' + json);
	            	}
	            	
	            	// Get the appointment objects
	            	var appointments = json.appointments;
	            	if (debug) {
	            		alert('appointments::' + appointments);
	            	}
	            	for (var i=0; i < appointments.length; i++) {
	            		var appointment = appointments[i];
	            		if (debug) {
	            			alert('appointment::' + appointment);
	            			alert('appointment.id::' + appointment.id);
	            		}
	            		// Populate the select box with appointment type names and appointment ids
	            		$('#appointments').
	            			append($("<option></option>").
	            			attr("value",appointment.id).
	            			text(appointment.name));
	            		
	            	}
	            	if (appointments != null && appointments != ''){
	            		// Show the appointment select box
	            		$('.buttons').show();
	            		$('#appointment-info').slideDown();            		
	            	}
	            	else {
	            		// Notify user that no appointments were found
	            		$('#data-errors').html('No appointments were found for this participant');
	            		$('#data-errors').show();
	            		// Hide the appointment select box
	            		$('#appointment-info').hide();
	            		$('.buttons').hide();
	            	}
	            	
	            } else if (textStatus == "timeout") {
	                $("#appointment-status").html("request timed out.");
	            } else if (textStatus == "parsererror") {
	                $("#appointment-status").html("error parsing response from server, please retry.");
	            } else if (textStatus == "notmodified") {
	                $("#appointment-status").html("what? not modified? ok.");
	            } else {
	                var msg = "Sorry but there was an error: ";
	                $("#appointment-status").html(msg + xhr.status + " " + xhr.statusText);                
	            }
	        });
    	}
	    return false;
	});

	$('#incentiveBarcode').keyup(function(event){
    	if (event.keyCode == '13') {
    		if ($('#incentiveBarcode').val()) {
				incentive = true;
    			$('#incentiveForm').submit();
    		}
    		else {
		        $('#incentiveBarcode').focus();
    		} 
		}
		return false;
	});		
	
});


function showSearching() {
	$('#formContainter').removeClass('errors');
	$('#notFound').hide();
	$('.message').hide();	
	// $('#searching').show();
	if (!spinnerVisible) {
		$('#searching').fadeIn('fast');
		spinnerVisible = true;
	}
}

function hideSearching() {
	// $('#searching').hide();
	if (spinnerVisible) {
		var spinner = $('div#searching');
		spinner.stop();
		$('#searching').fadeOut('fast');
		spinnerVisible = false;
	}
}

function showFailure() {
	$('#notFound').show();	
}

//used to apply alternating row styles
function zebraRows(selector, className)
{
  $(selector).removeClass(className).addClass(className);
}

//filter results based on query
function filter(selector, query) {
  query	=	$.trim(query); //trim white space
  query = query.replace(/ /gi, '|'); //add OR for regex query

  $(selector).each(function() {
    ($(this).text().search(new RegExp(query, "i")) < 0) ? $(this).hide().removeClass('visible') : $(this).show().addClass('visible');
  });
  
}

// Update list of incentive ids to print
function updateIncentiveList(checkBox) {
	// Get incentive id
	try {
		var incentiveId = $(checkBox).attr('name').replace('checkBox_','');		
	} catch (err) {
		alert ("Could not get id from element::" + $(checkBox).attr('name') + " -- ERROR:: " + err);
	}
	var index = null;
	var incentiveList = null;
	
	if (incentiveId) {
		if (debug) {
			alert ("Updating print status for incentive::" + incentiveId);
		}		
		// Get list of incentive ids	
		var incentiveList = groovyListToArray($('#incentivesToPrint').val());
		if (incentiveList != '') {
			if (debug) {
				alert ("Searching for incentive in list::" + incentiveList);
			}
			// Get index of incentive id in incentive list
			index = $.inArray(incentiveId, incentiveList);
			if (debug) {
				alert ("Setting incentive id index::" + index + " | incentiveList::[" + incentiveList + "]::length::" + incentiveList.length);
			}		
			// Update incentive list based on checkbox status
			if ($(checkBox).attr('checked')) {
				// Search incentive list for current id and add if missing
				if (index < 0) {
					incentiveList.push(incentiveId);						
					// Update the page with new array list
					$('#incentivesToPrint').val(incentiveList);
					if (debug) {
						alert ("Added incentive::" + incentiveId + " to list");
					}		
				}
			} else {
				// Search incentive list for current id and remove if found
				if (index >= 0) {
					incentiveList.splice(index, 1);
					// Update the page with new array list
					$('#incentivesToPrint').val(incentiveList);
					if (debug) {
						alert ("Removed incentive::" + incentiveId + " from list");
					}		
				}							
			}			
		} else {
			// Add one incentive id to list
			incentiveList = incentiveId;			
			// Update the page with new array list
			$('#incentivesToPrint').val(incentiveList);
		}
		if (debug) {
			alert ("Updated incentive list::" + $('#incentivesToPrint').val());
		}		

	}	
}

function groovyListToArray(l) {
    // convert a groovy list to an array
    //alert("Converting string to array: " + l);
    var list = l.replace(/\[/g, "");
    list = list.replace(/\]/g, "");
    list = list.replace(/ /g, "");
    //alert("Removed characters, new list: " + list);
    var theArray = new Array
    theArray = list.split(",");
    //alert("New array: " + theArray + " with length: " + theArray.length);
    return theArray;
}

function checkSelectedIncentives() {
	var incentiveInstanceList = $('#incentivesToPrint').val();
	if (incentiveInstanceList) {
		// check for leading comma
		if (debug) {
			alert("incentiveInstanceList::" + incentiveInstanceList);
		}
		if (incentiveInstanceList.substring(1) == ',') {
			$('#incentivesToPrint').val(incentiveInstanceList.replace(' ,',''));
		}
		if (debug) {
			alert("Updated incentiveInstanceList::" + $('#incentivesToPrint').val());
		}
		return true;
	}
    $('.noSelectedIncentives').show();
	return false;
}
/*
//Disable form submit on ENTER key press
function stopRKey(evt) {
  var evt = (evt) ? evt : ((event) ? event : null);
  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
  if ((evt.keyCode == 13) && (node.type=="text"))  {
	  //alert('The ENTER key has been disabled. Please use the TAB key to navigate between fields.')
      return false;
  }
}

document.onkeypress = stopRKey;
*/
