$(document).ready(function() {
	// Event handlers.
	$('#btn_finish').click(function() {
		document.form1.submit();
	});
	
	// Load event.
	var firstLi = $('div.questions ul li:first');
	var firstInput = firstLi.children()[0];
	firstInput.focus();
});