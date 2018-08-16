package easycbt2.model;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name="take_examinations_questions")
public class TakeExaminationsQuestion {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private TakeExamination takeExamination;
	@OneToOne
	private Question question;
	@Column
	private Long elapsedTime;
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "takeExaminationsQuestion")
    private Set<TakeExaminationsAnswer> takeExaminationsAnswers;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TakeExamination getTakeExamination() {
		return takeExamination;
	}
	public void setTakeExamination(TakeExamination takeExamination) {
		this.takeExamination = takeExamination;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public Long getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
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
	public Set<TakeExaminationsAnswer> getTakeExaminationsAnswers() {
		return takeExaminationsAnswers;
	}
	public void setTakeExaminationsAnswers(Set<TakeExaminationsAnswer> takeExaminationsAnswers) {
		this.takeExaminationsAnswers = takeExaminationsAnswers;
	}

	public Boolean isCorrect() {
		if(getTakeExaminationsAnswers() == null) {
			return false;
		}
		
		Question question = getQuestion();
		List<QuestionAnswer> expectedAnswers = question.getCorrectQuestionAnswerList();
		Set<TakeExaminationsAnswer> actualAnswers = getTakeExaminationsAnswers();

		switch(question.getQuestionType()) {
		case SINGLE_CHOICE:
			if(expectedAnswers.size() != 1 || actualAnswers.size() != 1) {
				return false;
			}
			QuestionAnswer expectedAnswer = expectedAnswers.iterator().next();
			TakeExaminationsAnswer actualAnswer_ = actualAnswers.iterator().next();
			return expectedAnswer.getId().equals(actualAnswer_.getAnswerId());
		case MULTIPLE_CHOICE:
			if(expectedAnswers.size() != actualAnswers.size()) {
				return false;
			}
			for(TakeExaminationsAnswer actualAnswer : actualAnswers) {
				if(!isContainsAnswerId(expectedAnswers, actualAnswer.getAnswerId())) {
					return false;
				}
			}
			return true;
		case TEXT:
			for(TakeExaminationsAnswer actualAnswer : actualAnswers) {
				if(isContainsAnswerText(expectedAnswers, actualAnswer.getAnswerText())) {
					return true;
				}
			}
			return false;
		default:
			return false;
		}
	}
	
	private Boolean isContainsAnswerId(List<QuestionAnswer> expectedAnswers, Long actualAnswer) {
		for(QuestionAnswer expectedAnswer : expectedAnswers) {
			if(expectedAnswer.getId().equals(actualAnswer)) {
				return true;
			}
		}
		return false;
	}

	private Boolean isContainsAnswerText(List<QuestionAnswer> expectedAnswers, String actualAnswer) {
		for(QuestionAnswer expectedAnswer : expectedAnswers) {
			if(expectedAnswer.getText().equals(actualAnswer)) {
				return true;
			}
		}
		return false;
	}

	public static Comparator<TakeExaminationsQuestion> getIdComparator() {
		return new Comparator<TakeExaminationsQuestion>() {
			public int compare(TakeExaminationsQuestion e1, TakeExaminationsQuestion e2) {
				return Long.compare(e1.getId(), e2.getId());
			}
		};
	}
}
