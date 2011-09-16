
var debug = false;

$(document).ready(function(){
	if (debug) {
		alert('jquery loaded');		
	}
	$('#errorDiv').hide();

	$('#person-info').hide();
	
    $('#trackedItem').change(function() {
    	$("#errorDiv").hide();

        // get the value entered in the field
        var trackedItemValue = $(this).val();
        // clear the input
        //$(this).val("");
        if (debug) {
        	alert('trackedItemValue::' + trackedItemValue);
        }
        
        if (trackedItemValue != '' && trackedItemValue != null) {
        	$('#person-info').show();
            $('#resultLog').html('');
        	$('#trackedItemInstance').val(trackedItemValue);
            if (debug) {
            	alert('personInfoFrom::trackedItemValue::' + $('#trackedItemInstance').val());
            }
            // create an element stating that we are trying to receipt it
            var strHtml = '<div id="person-info-div" class="item-' + trackedItemValue + '">';
            //strHtml += barcodeValue;
            strHtml += '<span id="person-info-status" style="font-weight:bold;">...Processing</span></div><br />';

            $('#resultLog').prepend(strHtml);

            ///////////////////////////
            // send the ajax request //
            ///////////////////////////

            // get the URL to send it to
            var url = $('form[name="personInfoForm"]').attr("action");
            // build the data
            var data = { "id": trackedItemValue, "divId": "person-info" };

            $.getJSON(url, data, function(data, textStatus){

                // Possible outcomes of textStatus are:
                // "success"
                // "timeout"
                // "error"
                // "notmodified"
                // "parsererror"

            	// TO DO: Continue here
                if (textStatus == "success") {
                    var resultDivId = data.divId

                    if (data.success) {
                    	if (debug) {
                        	alert("person::" + data.person);                    		
                    	}
                        var resultText = "Person ID: " + data.person.id + ", " + data.fullName;
     
                        $("#person-info-status").html(resultText);
                    } else {
                    	$("#errorDiv").show();
                        $("#errorLabel").html("Failed to find tracked item: " + data.errorText);
                    }

                } else if (textStatus == "timeout") {
                    alert(2);
                	$("#errorDiv").show();
                    $("#errorLabel").html("Request timed out.");
                } else if (textStatus == "parsererror") {
                    alert(3);
                	$("#errorDiv").show();
                    $("#errorLabel").html("Error parsing response from server, please retry.");
                } else if (textStatus == "notmodified") {
                    alert(4);
                	$("#errorDiv").show();
                    $("#errorLabel").html("What? Not modified? OK.");
                } else {
                    alert(5);
                	$("#errorDiv").show();
                    $("#errorLabel").html("An error occured on the server.");
                }
            });

            //alert(trackedItemValue);        	
        }
        else {
        	$('#person-info').hide();        	
        }
        
    });
	
});

function validateForm() {
	if (debug) {
		alert('Checking tracked item');
	}
	var trackedItem = $('#trackedItem').val();
	if (trackedItem != '' && trackedItem != null) {
		return true;
	}
	else {
		$('#errorLabel').html('No TRACKED ITEM ID specified. Please review.');
		$('#errorDiv').show();		
		return false;
	}
}

//Disable form submit on ENTER key press
function stopRKey(evt) {
  var evt = (evt) ? evt : ((event) ? event : null);
  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
  if ((evt.keyCode == 13) && (node.type=="text"))  {
      return false;
  }
}

document.onkeypress = stopRKey;