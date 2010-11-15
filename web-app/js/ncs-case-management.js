
/*
function logItem() {
    receivedElementId++;
    var itemId = $("input[name=item]").val();

    itemId = itemId.replace("I");

    alert('I am in logItem! Item ID --> ' + itemId);

    var html = '<div id="scan-' + receivedElementId + '" class="item-' + itemId + '">';
    html += docId;
    html += '<span style="padding-left:3em;;" id="scan-' + receivedElementId + '_status">...Processing</span></div>';

    $('#updateMe').append(html);
  }

function successLogItem() {
    var docId = $("input[name=item]").val();
    alert('I am in successLogItem! Item Id --> ' + docId)

    $('#i' + docId).html(docId + '<span style="padding-left:3em;" id="i' + docId + '_status">Received and Logged</span>');
}

function errorLogItem() {
    var docId = $("input[name=item]").val();
    alert('I am in errorLogItem! Item Id --> ' + docId)

    $('#i' + docId).html(docId + '<span style="padding-left:3em;" id="i' + docId + '_status">ERROR: Failed to log item</span>');
}
*/


var receivedElementId = 0

$(document).ready(function(){
    $('#barcode').change(function() {

        // get the value entered in the field
        var barcodeValue = $(this).val();
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

        $('#resultLog').append(strHtml);

        ///////////////////////////
        // send the ajax request //
        ///////////////////////////

        // get the URL to send it to
        var url = $('form[name="receiptForm"]').attr("action");
        // build the data
        var data = { "id": barcodeValue, "divId": receivedElementId };

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

