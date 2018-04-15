package easycbt2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name="questions")
public class Question {
	@Id
	@GeneratedValue
	private Long id;
	@Enumerated(EnumType.STRING)
	private QuestionType questionType;
	@Column
	private String text;
	@ManyToOne
	private QuestionCategory questionCategory;
	@Column
	private String defaultText;
	@Column
	private String explanation;
    @CreatedBy
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;
    @LastModifiedBy
    private String modifiedBy;
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date modifiedDate;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    private Set<QuestionAnswer> questionAnswerList;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public QuestionCategory getQuestionCategory() {
		return questionCategory;
	}
	public void setQuestionCategory(QuestionCategory questionCategory) {
		this.questionCategory = questionCategory;
	}
	public String getDefaultText() {
		return defaultText;
	}
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Set<QuestionAnswer> getQuestionAnswerList() {
		return questionAnswerList;
	}
	public void setQuestionAnswerList(Set<QuestionAnswer> questionAnswerList) {
		this.questionAnswerList = questionAnswerList;
	}

	public List<QuestionAnswer> getCorrectQuestionAnswerList() {
		List<QuestionAnswer> resultList = new ArrayList<>();
		for(QuestionAnswer questionAnswer : getQuestionAnswerList()) {
			if(questionAnswer.getIsCorrect()) {
				resultList.add(questionAnswer);
			}
		}
		
		return resultList;
	}
}
