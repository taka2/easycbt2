var answerTextIndex = 1;

function deleteAnswerText(index) {
	$('#answer' + index).remove();
	answerTextIndex--;
}

function addAnswerText() {
	var appendElement = '<li class="form-group" id="answer' + answerTextIndex + '">' + 
		'<input class="col-sm-10" type="text" name="questionsAnswers[' + answerTextIndex + '].text"/>' + 
		'<button class="col-sm-1 delete-button btn btn-danger" type="button" onClick="deleteAnswerText(' + answerTextIndex + ')">Delete</button>' + 
		'</li>';

	$('#answer-text').append(appendElement);
	
	answerTextIndex++;
}

function addAnswerChoice() {
	var appendElement = '<li class="form-group" id="answer' + answerTextIndex + '">' + 
		'<input class="col-sm-1" type="checkbox" name="questionsAnswers[' + answerTextIndex + '].isCorrect">' +
		'<input class="col-sm-9" type="text" name="questionsAnswers[' + answerTextIndex + '].text">' + 
		'<button class="col-sm-1 delete-button btn btn-danger" type="button" onClick="deleteAnswerText(' + answerTextIndex + ')">Delete</button>' + 
		'</li>';

	$('#answer-text').append(appendElement);
	
	answerTextIndex++;
}

jQuery(document).ready(function() {
	if(document.getElementById("example-table") == null) {
		return;
	}

	//custom formatter definition
	var editIcon = function(cell, formatterParams, onRendered){
		return '<i class="far fa-edit"></i>';
	};
	var removeIcon = function(cell, formatterParams, onRendered){
		return '<i class="far fa-trash-alt"></i>';
	};

	//create Tabulator on DOM element with id "example-table"
	var table = new Tabulator("#example-table", {
	 	layout:"fitColumns", //fit columns to width of table (optional)
	 	columns:[
	 		{title:"id", field:"id", width:30},
	 		{title:"questionType", field:"questionType"},
	 		{title:"text", field:"text"},
	 		{title:"questionCategory", field:"questionCategory.name"},
	 		{title:"defaultText", field:"defaultText"},
	 		{title:"explanation", field:"explanation"},
	 		{formatter:editIcon, width:30, align:"center", cellClick:function(e, cell){location.href='/maintenance/questions/' + cell.getRow().getData().id + '/edit';}},
	 		{formatter:removeIcon, width:30, align:"center", cellClick:function(e, cell){deleteQuestion(cell.getRow().getData().id);}},
	 	],
	 	ajaxURL: '/api/maintenance/questions',
	 	pagination: 'remote', //enable remote pagination
	 	paginationSize: 10,
	 	ajaxContentType: 'json'
	});

	function deleteQuestion(id) {
		const xsrf = $.cookie('XSRF-TOKEN');
	    $.ajax({
	        url: '/api/maintenance/questions/' + id,
	        type: 'DELETE',
	        dataType: 'json',
	        headers: { 'X-XSRF-TOKEN': xsrf },
	        timeout:3000,
	    }).done(function(data) {
	    	table.setData();
	    }).fail(function(XMLHttpRequest, textStatus, errorThrown) {
	    	alert("error");
	    })
	}

	//Trigger setFilter function with correct parameters
	function updateFilter(){
		var filter = [
			[
				{field:"text", type:"like", value:$("#filter-value").val()}
				, {field:"questionCategory.name", type:"like", value:$("#filter-value").val()}
				, {field:"explanation", type:"like", value:$("#filter-value").val()}
			]
		];

        $("#filter-value").prop("disabled", false);

        table.setFilter(filter);
	}

	//Update filters on value change
	$("#filter-value").keyup(updateFilter);
});
