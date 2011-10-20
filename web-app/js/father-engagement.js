
var debug = false;

$(document).ready(function(){
	if (debug) {
		alert('jquery loaded');		
	}
	$('#notFound').hide();

	$('#searching').hide();
	
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

function showSearching() {
	$('#notFound').hide();
	$('#searching').show();	
}

function hideSearching() {
	$('#searching').hide();	
}

function showNotFound() {
	$('#notFound').show();	
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
