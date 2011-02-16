/* 
 * jQuery implemented on report views
 */

var paramCount = 0;

$(document).ready(function(){
	//alert("jQuery loaded");

	// Enable table sorting
    $("#list-table").tablesorter({
    	widgets: ['zebra']
    });

    //default each row to visible
    $('tbody tr').addClass('visible');

    //zebraRows('tbody.zebra tr:odd', 'odd');

    $("#list-table tr").hover(function () {
            if ($(this).hasClass('odd')) {
                $(this).removeClass('odd');
                $(this).addClass('was-odd');
            }
            $(this).addClass("highlight-row");
        },
        function () {
            if ($(this).hasClass('was-odd')) {
               $(this).removeClass('was-odd');
               $(this).addClass('odd');
            }
            $(this).removeClass("highlight-row");
    });
    
	// Hide the query entry box by default
	if ($("#useQuery").attr("checked") == false) {
		$("#trQuery").hide();
	}
	
	// Show/hide the query entry box
	$("#useQuery").click(function() {
		if ($("#useQuery").attr("checked") == true) {
			//alert("Checked");
			$("#trQuery").show();
			$("#designedReport").hide();
		}
		else {
			//alert("Unchecked");		
			$("#trQuery").hide();
			$("#designedReport").show();
		}		
	});
	
    // Filter table lists
    $('#filter').keyup(function(event) {
        //if esc is pressed or nothing is entered
        if (event.keyCode == 27 || $(this).val() == '') {
          //if esc is pressed we want to clear the value of search box
          $(this).val('');

          //we want each row to be visible because if nothing
          //is entered then all rows are matched.
          $('tbody tr').removeClass('visible').show().addClass('visible');
          $('.total-count').show();
        }

        //if there is text, lets filter
        else {
          filter('tbody tr', $(this).val());
          $('.total-count').hide();
        }

        //reapply zebra rows
        $('tbody.zebra tr').removeClass('odd');
        zebraRows('tbody.zebra tr:visible:odd', 'odd');
    });
	

});

//Disable form submit on ENTER key press
function stopRKey(evt) {
  var evt = (evt) ? evt : ((event) ? event : null);
  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
  if ((evt.keyCode == 13) && (node.type=="text"))  {
      return false;
  }
}

document.onkeypress = stopRKey;

//filter results based on query
function filter(selector, query) {
  query	=	$.trim(query); //trim white space
  query = query.replace(/ /gi, '|'); //add OR for regex query

  $(selector).each(function() {
    ($(this).text().search(new RegExp(query, "i")) < 0) ? $(this).hide().removeClass('visible') : $(this).show().addClass('visible');
  });
}

//used to apply alternating row styles
function zebraRows(selector, className)
{
  $(selector).removeClass(className).addClass(className);
}

function validateReportEntry() {
	/*
	 * Validates report entry form
	 */
	if (!$('.info-box').hasClass('hidden')){
	    $('.info-box').addClass('hidden');		
	}
    var elementId = "designedName";
    var element = document.getElementById(elementId);
	var reportList = new Array();
	var filename = $('input[type=file]').val();
	//alert("Filename is " + filename);
	if (filename) {
		// Get the BIRT report name from the full upload path (remove extension .rptdesign)
		var reportName = filename.split('.').shift();
		//alert("Report name is " + reportName);
		// Get list of existing BIRT report names
        elementId = "reportList";
        element = document.getElementById(elementId);
        // If reports exist, check to see if the current report to be uploaded already exists
        if ($(element).val()){
        	reportList = groovyListToArray($(element).val());
        	//alert("Report name check returns " + existsInArray(reportName, reportList));
        	if (existsInArray(reportName, reportList) > -1) {
        		var answer = confirm("The report [" + reportName + "] already exists. Do you wish to overwrite? --Click OK to continue (THIS MAY AFFECT EXISTING REPORTS)!");
        		if (!answer) {
            		return false;        			
        		}
        	}
        }		
	}
	
    if (!$('#title').val()){
        $('.info-box').removeClass('hidden');
        $('.info-box').html("Please enter a title for this report.");
    	$(element).text("Please enter a title for this report.");
    	return false;
    }
	return true;
}



/* UTILITY FUNCTIONS */

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

function existsInArray(item, theArray) {
	/*
	 *  Determine if an item exists in an array
	 */
	return $.inArray(item,theArray);
}