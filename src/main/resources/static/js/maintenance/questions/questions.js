function deleteAnswerText(index) {
	$('#answer' + index).remove();
}

var answerTextIndex = 10;

function addAnswerText() {
	var appendElement = '<li class="form-group" id="answer' + answerTextIndex + '">' + 
		'<input class="col-sm-10" type="text" name="answerText"/>' + 
		'<button class="col-sm-1 delete-button btn btn-danger" type="button" onClick="deleteAnswerText(' + answerTextIndex + ')">Delete</button>' + 
		'</li>';

	$('#answer-text').append(appendElement);
	
	answerTextIndex++;
}

function addAnswerChoice() {
	var appendElement = '<li class="form-group" id="answer' + answerTextIndex + '">' + 
		'<input class="col-sm-1" type="checkbox" name="answerIsCorrect' + answerTextIndex + '">' +
		'<input class="col-sm-9" type="text" name="answerText' + answerTextIndex + '">' + 
		'<button class="col-sm-1 delete-button btn btn-danger" type="button" onClick="deleteAnswerText(' + answerTextIndex + ')">Delete</button>' + 
		'</li>';

	$('#answer-text').append(appendElement);
	
	answerTextIndex++;
}
