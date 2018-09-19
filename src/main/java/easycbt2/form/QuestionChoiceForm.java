package easycbt2.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import easycbt2.constants.QuestionType;
import easycbt2.model.QuestionAnswer;
import easycbt2.model.QuestionCategory;

public class QuestionChoiceForm {
	private Long id;
	@NotBlank
	private String text;
	@NotNull
	private QuestionType questionType;
	@NotNull
	private QuestionCategory selectedQuestionCategory;
	private String explanation;
	@Valid
	@NotNull
	private List<QuestionAnswer> questionsAnswers;
	@NotBlank
	private String scope;
	private List<QuestionCategory> questionCategories;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	public QuestionCategory getSelectedQuestionCategory() {
		return selectedQuestionCategory;
	}
	public void setSelectedQuestionCategory(QuestionCategory selectedQuestionCategory) {
		this.selectedQuestionCategory = selectedQuestionCategory;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public List<QuestionAnswer> getQuestionsAnswers() {
		return questionsAnswers;
	}
	public void setQuestionsAnswers(List<QuestionAnswer> questionsAnswers) {
		this.questionsAnswers = questionsAnswers;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public List<QuestionCategory> getQuestionCategories() {
		return questionCategories;
	}
	public void setQuestionCategories(List<QuestionCategory> questionCategories) {
		this.questionCategories = questionCategories;
	}
	
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
