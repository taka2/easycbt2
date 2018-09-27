package easycbt2.form;

import java.time.Instant;
import java.util.List;

import easycbt2.model.Examination;
import easycbt2.model.Question;

public class TakeExaminationForm {
	Examination examination;
	List<Question> questions;
	Instant startDateTime;
	Instant endDateTime;
	List<String> answers;

	public Examination getExamination() {
		return examination;
	}
	public void setExamination(Examination examination) {
		this.examination = examination;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public Instant getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Instant startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Instant getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Instant endDateTime) {
		this.endDateTime = endDateTime;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
}
