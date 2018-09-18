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
