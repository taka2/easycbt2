package easycbt2.form;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import easycbt2.model.QuestionCategory;

public class ExaminationForm {
	private Long id;
	@NotBlank
	private String text;
	@NotNull
	@Min(1)
	@Max(200)
	private Integer questionCount;
	@NotNull
	private List<QuestionCategory> selectedQuestionCategories;
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
	public Integer getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}
	public List<QuestionCategory> getSelectedQuestionCategories() {
		return selectedQuestionCategories;
	}
	public void setSelectedQuestionCategories(List<QuestionCategory> selectedQuestionCategories) {
		this.selectedQuestionCategories = selectedQuestionCategories;
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
}
