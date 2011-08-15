

var receivedElementId = 0

$(document).ready(function(){
    $('#barcode').change(function() {

        // get the value entered in the field
        var barcodeValue = $(this).val();
		// get the receipt date
		var receiptDateInstance = $("#receiptDateInstance").val();
        // clear the input
        $(this).val("");
        // set the focus back to the input field
        $(this).focus();
        // increment the counter
        receivedElementId++;

        var divId = receivedElementId;
        // create an element stating that we are trying to receipt it
        var strHtml = '<div style="padding-top:0.5em;" id="scan-' + receivedElementId + '" class="item-' + barcodeValue + '">';
        strHtml += barcodeValue;
        strHtml += '<span style="padding-left: 1em;" id="scan-' + receivedElementId + '-status">...Processing</span></div>';

        $('#resultLog').prepend(strHtml);

        ///////////////////////////
        // send the ajax request //
        ///////////////////////////

        // get the URL to send it to
        var url = $('form[name="receiptForm"]').attr("action");
        // build the data
        var data = { "id": barcodeValue, "divId": receivedElementId, "receiptDateInstance": receiptDateInstance };

        $("#scan-" + receivedElementId + "-status").load(url, data, function(response, textStatus, xhr){

            // Possible outcomes of textStatus are:
            // "success"
            // "timeout"
            // "error"
            // "notmodified"
            // "parsererror"

            if (textStatus == "success") {
                var resultDivId = data.divId

                //$("#scan-" + resultDivId + "-status").html(response);

            } else if (textStatus == "timeout") {
                $("#scan-" + divId + "-status").html("request timed out.");
            } else if (textStatus == "parsererror") {
                $("#scan-" + divId + "-status").html("error parsing response from server, please retry.");
            } else if (textStatus == "notmodified") {
                $("#scan-" + divId + "-status").html("what? not modified? ok.");
            } else {
                var msg = "Sorry but there was an error: ";
                $("#scan-" + divId + "-status").html(msg + xhr.status + " " + xhr.statusText);
                
            }
        });

        //alert(barcodeValue);
        
    });
});

