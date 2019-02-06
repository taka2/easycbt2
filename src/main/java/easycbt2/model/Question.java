package easycbt2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

import easycbt2.constants.QuestionType;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name="questions")
public class Question {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private QuestionType questionType;
	@Column(length=1024)
	private String text;
	@ManyToOne
	private QuestionCategory questionCategory;
	@Column(length=1024)
	private String defaultText;
	@Column(length=1024)
	private String explanation;
	@Column
	private Boolean enabled;
	@Column(updatable=false)
    @CreatedBy
    private String createdBy;
	@Column(updatable=false)
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
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
		return questionAnswerList.stream().filter(s -> s.getEnabled()).collect(Collectors.toSet());
	}
	public void setQuestionAnswerList(Set<QuestionAnswer> questionAnswerList) {
		this.questionAnswerList = questionAnswerList;
	}
	public List<QuestionAnswer> getQuestionAnswerListOrderByIdAsc() {
		List<QuestionAnswer> questionsAnswerList = new ArrayList<>(getQuestionAnswerList());
		Collections.sort(questionsAnswerList, QuestionAnswer.getIdComparator());
		return questionsAnswerList;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static Comparator<Question> getIdComparator() {
		return new Comparator<Question>() {
			public int compare(Question e1, Question e2) {
				return Long.compare(e1.getId(), e2.getId());
			}
		};
	}
}
