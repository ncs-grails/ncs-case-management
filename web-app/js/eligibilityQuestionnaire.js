/**
 * eligibilityQuestionnaire.js
 * Date Created: 2011-12-21
 * Author: Aaron J. Zirbes
 */
var debug = false;

$(document).ready(function() {
	if (debug) { console.log("jQuery ready."); }

	/** The following code toggles the display of the address input form
	 * depending on if the "Use existing street address" checkbox is checked.
	 */
	var useAddressCheckbox = $("input[name='useExistingStreetAddress.id']");
	var hideArea = $("#addressFields");

	toggleAddress(useAddressCheckbox, hideArea);
	$(useAddressCheckbox).change(function() {
		toggleAddress(this, hideArea);
	});

	/**
	 * The following code toggles the display of zipcode vs. international
	 * postal code depending on if the country selected is US or not.
	 */
	var countrySelect = $("select[name='country.id']");
	var usValue = $("#countryUs").val();
	var usDiv = $(".usPostalCode");
	var nonUsDiv = $(".nonUsPostalCode");
	toggleCountry(countrySelect, usValue, usDiv, nonUsDiv);
	$(countrySelect).change(function() {
		toggleCountry(this, usValue, usDiv, nonUsDiv);
	});


	/**
	 * The following sets input fields with the class 'capitalize' to automatically
	 * capitalize their contents as the user types.
	 */
	$("input.capitalize").keyup(function() {
		var strValue = $(this).val();
		if (strValue.length > 0) {
			var firstChar = strValue.charAt(0);
			if (firstChar != firstChar.toUpperCase()) {
				strValue = strValue.replace(firstChar, firstChar.toUpperCase());
				$(this).val(strValue);
			}
		}
	});

	/**
	 * The following code ensures the user doesn't put a dot, or period, in the
	 * input fields with the class 'nodots'.
	 */
	$("input.nodots").keyup(function() {
		var strValue = $(this).val();
		if (strValue.indexOf('.') > 0) {
			strValue = strValue.replace('.', '');
			$(this).val(strValue);
		}
	});


	/** Make it difficult to enter a Zip4 code by hand */
	$("input[name='zip4']").focus(function() {
		setTimeout("zp4NextField();", 20);
	});

	/** Set the focus to the title field */
	$("input[name='title']").focus();

});

function zp4NextField() {
	var countrySelect = $("select[name='country.id']");
	$(countrySelect).focus();

}

function toggleCountry(select, compareTo, div1, div2) {
	var countryCode = $(select).val();
	if (debug) { console.log("toggling postal code visibility for country:" + countryCode); }
	if (countryCode != compareTo) {
		$(div1).hide();
		$(div2).show();
	} else {
		$(div1).show();
		$(div2).hide();
	}
}

function toggleAddress(checkbox, hideDivId) {

	if (debug) { console.log("toggling address visibility"); }

	var useAddressVal = $(checkbox).val();
	if (debug) { console.log("useAddressVal: " + useAddressVal); }

	if ( $(checkbox).attr("checked") ) {
		$(hideDivId).hide();
	} else {
		$(hideDivId).show();
	}
}
