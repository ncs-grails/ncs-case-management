
var debug = false;

$(document).ready(function(){
	if (debug) {
		alert('jquery loaded');		
	}
	$('#errorDiv').hide();

	$('#searching').hide();
	
});

function validateForm() {
	if (debug) {
		alert('Checking tracked item');
	}
	var trackedItem = $('#trackedItem').val();
	var interviewerInitials = $('#interviewerInitials').val();
	if (trackedItem != '' && trackedItem != null) {
		if (debug) {
			alert('trackedItem::' + trackedItem + '::interviewerInitials::' + interviewerInitials);
			alert('interviewerInitials.length::' + interviewerInitials.length);
		}
		if ((interviewerInitials != '' && interviewerInitials.length < 4) || (interviewerInitials == ''))  {
			return true;			
		}
		else {
			$('#interviewerInitials').focus();
			$('#errorLabel').html('Interviewer initials must be limited to three characters. Please review.');			
			$('#errorDiv').show();		
			return false;
		}
	}
	else {
		$('#errorLabel').html('No TRACKED ITEM ID specified. Please review.');
		$('#errorDiv').show();		
		return false;
	}
}

function showSearching() {
	$('#formContainter').removeClass('errors');
	$('#errorDiv').hide();
	$('#searching').show();	
}

function hideSearching() {
	$('#searching').hide();
}

function showFailure() {
	$('#errorDiv').show();	
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
