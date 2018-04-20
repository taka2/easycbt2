package easycbt2.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
@Table(name="take_examinations")
public class TakeExamination {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private User user;
	@ManyToOne
	private Examination examination;
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "takeExamination")
    private Set<TakeExaminationsQuestion> takeExaminationsQuestions;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Examination getExamination() {
		return examination;
	}
	public void setExamination(Examination examination) {
		this.examination = examination;
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
	public Set<TakeExaminationsQuestion> getTakeExaminationsQuestions() {
		return takeExaminationsQuestions;
	}
	public void setTakeExaminationsQuestions(Set<TakeExaminationsQuestion> takeExaminationsQuestions) {
		this.takeExaminationsQuestions = takeExaminationsQuestions;
	}

	public int getQuestionCount() {
		return getTakeExaminationsQuestions().size();
	}
	public int getCorrectCount() {
		int correctCount = 0;
		for(TakeExaminationsQuestion question : getTakeExaminationsQuestions()) {
			if(question.isCorrect()) {
				correctCount++;
			}
		}
		
		return correctCount;
	}
	public double getCorrectPercentage() {
		int questionCount = getQuestionCount();
		int correctCount = getCorrectCount();
	
		if(questionCount == 0) {
			return 0d;
		}

		return correctCount / (double)questionCount * 100;
	}
	
	public List<Question> getIncorrectAnsweredQuestions() {
		List<Question> resultList = new ArrayList<>();
		for(TakeExaminationsQuestion question : this.getTakeExaminationsQuestions()) {
			if(!question.isCorrect()) {
				resultList.add(question.getQuestion());
			}
		}

		return resultList;
	}
	
	public String getFormattedElapsedTime() {
		Instant instant = Instant.ofEpochMilli(getElapsedTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withLocale(Locale.getDefault()).withZone(ZoneId.of("GMT"));
		return formatter.format(instant);
	}
}
