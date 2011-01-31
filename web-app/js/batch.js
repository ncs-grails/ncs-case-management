/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
	$("#id").focus();

	/* resubmit form on date change */
	$('select[name^="referenceDate_"]').change(function(){
            $('form[name="mailDateForm"]').submit();
	});

        $('select[name^="listByDateSelect_"]').change(function(){
            $('form[name="listByDateForm"]').submit();
        });

});
