
function addDoc(form) {
    var docId = $("input[name=item]").val();

    $('#updatedArea').append('<div id="i' + docId + '">' + docId + '<span style="padding-left:3em;;" id="i' + docId + '_status">...Processing</span></div>')
    //return false;
  }

