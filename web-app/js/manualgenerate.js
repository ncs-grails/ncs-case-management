/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var queueId = 0;

$(document).ready(function(){
    $('#sourceValue').change(function(){

        // sourceId {person, dwellingUnit or houseHold }
        var batchCreationQueueSourceId = $('#batchCreationQueueSource\\.id').val();
        var batchCreationQueueSource = $('#batchCreationQueueSource\\.id option:selected').text();
        // div the result message will go into.
        var messageDiv = "#message";
        var batchCreationConfigId = $('#batchCreationConfig\\.id').val();

        // get the value entered in the field
        var sourceValue = $(this).val();

        // alert("sourceId ->" + batchCreationQueueSource + "\nsourceValue -> " + sourceValue);

        queueId++;

        $("#itemsLegend").html("Entered Items (" + queueId + ")");

        // clear the input
        $(this).val("");
        // set the focus back to the input field
        $(this).focus();

        var newResultDivId = 'queueAddResult-' + queueId + '';
        var newDivContent = "";
        if ((queueId % 2) == 0) {
            newDivContent = '<div class="prop-q odd bb">';
        } else {
            newDivContent = '<div class="prop-q even bb">';
        }
        //newDivContent = '<div>ID: ' + sourceValue + ', <span id="' + newResultDivId + '">Searching...</span></div>';
        newDivContent += '<span class="value-q s">' + sourceValue + '</span>'
            + '<span class="value-q s">' + batchCreationQueueSource + '</span>'
            + '<span class="value-q l" id="' + newResultDivId + '">Searching...</span>';
        newDivContent += '</div>'

        // manualGenerationQueue
        // get url to send it to, and escape it (URL Encode it)
        if (queueId == 1) {
            $('#deleteMe').remove();
        }
        
        // are we searching for a parent item id?
        var useParentItem = $("#useParentItem").val()

        var url = $('#findUrl').val();

        var data = { 'batchCreationQueueSource.id': batchCreationQueueSourceId,  id: sourceValue, useParentItem: useParentItem, 'batchCreationConfig.id': batchCreationConfigId};

        $("#manualGenerationQueue").prepend(newDivContent);

        $("#" + newResultDivId).load(url, data, function(responseText, textStatus) {

            if (textStatus == "success") {
                $(messageDiv).html("success!.");
            } else if (textStatus == "timeout") {
                $(messageDiv).html("request timed out.");
                $(this).html('timeout, try again.');
            } else if (textStatus == "parsererror") {
                $(messageDiv).html("error parsing response from server, please retry.");
                $(this).html("???");
            } else if (textStatus == "notmodified") {
                $(messageDiv).html("what? not modified? ok.");
                $(this).html("???");
            } else {
                $(messageDiv).html("an error occured on the server.");
                $(this).html("ERROR!!!");
            }
        });
    });
});