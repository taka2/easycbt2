package easycbt2.form;

import easycbt2.model.QuestionAnswer;

public class QuestionChoiceForm extends QuestionForm {

	public Long getCorrectAnswersCount() {
		Long result = 0L;
		
		for(QuestionAnswer questionAnswer : getQuestionsAnswers()) {
			if(questionAnswer.getIsCorrect() != null && questionAnswer.getIsCorrect()) {
				result++;
			}
		}
		
		return result;
	}
}
