/**
* Incentive jQuery
* Date Created: 2011-08-04
* By: Aaron S. Timbo
*/

var incentive = false;
var instrument = false;
var debug = false;
var incentiveElementId = 0;
var barcodeCount = 0;

$(document).ready(function() {
	if (debug) {
		alert('jQuery loaded');
	}

	
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
	$('#searching').show();	
}

function hideSearching() {
	$('#searching').hide();
}

function showFailure() {
	$('#notFound').show();	
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
