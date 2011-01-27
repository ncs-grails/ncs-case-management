

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
        var strHtml = '<div id="scan-' + receivedElementId + '" class="item-' + barcodeValue + '">';
        strHtml += barcodeValue;
        strHtml += '<span style="padding-left:3em;;" id="scan-' + receivedElementId + '-status">...Processing</span></div>';

        $('#resultLog').prepend(strHtml);

        ///////////////////////////
        // send the ajax request //
        ///////////////////////////

        // get the URL to send it to
        var url = $('form[name="receiptForm"]').attr("action");
        // build the data
        var data = { "id": barcodeValue, "divId": receivedElementId, "receiptDateInstance": receiptDateInstance };

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
                    var resultText = "success, received " + data.studyName + " " + data.instrumentName;
                    resultText += ", ID: " + data.trackedItemId;

                    $("#scan-" + resultDivId + "-status").html(resultText);
                } else {
                    $("#scan-" + resultDivId + "-status").html("failed to receipt item:" + data.errorText);
                }

            } else if (textStatus == "timeout") {
                alert(2);
                $("#scan-" + divId + "-status").html("request timed out.");
            } else if (textStatus == "parsererror") {
                alert(3);
                $("#scan-" + divId + "-status").html("error parsing response from server, please retry.");
            } else if (textStatus == "notmodified") {
                alert(4);
                $("#scan-" + divId + "-status").html("what? not modified? ok.");
            } else {
                alert(5);
                $("#scan-" + divId + "-status").html("an error occured on the server.");
            }
        });

        //alert(barcodeValue);
        
    });
});

