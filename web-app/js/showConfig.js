
$(document).ready(function(){

   $('#printDetails').click(function(){
        return warn('batch.id', 'Reprint Document(s)',  'Select a batch to reprint.');

    });

    $('#batchReport').click(function(){
        return warn('batch.id', 'View Batch Report', 'Select a batch to view Batch Report.');
    });

    function warn(name, t, m) {
        if ($('input[name="'+ name +'"]:checked').length) {
            return true;
        } else {
            //Show dialgue here
            $('<div>' + m + '</div>').dialog({
                title: t,
                modal: true,
                height:200,
                width:400,
                overlay: {
                    background: '#000',
                    opacity: 0.5
                },
                buttons: {
                    'OK': function() {
                        $(this).dialog('close').remove();
                    }
                }
            });
            return false;
        }
    };

});
