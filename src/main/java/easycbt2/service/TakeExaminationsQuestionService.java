package easycbt2.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;
import easycbt2.repository.TakeExaminationsQuestionRepository;

@Service
public class TakeExaminationsQuestionService {
	@Autowired
	TakeExaminationsQuestionRepository takeExaminationsQuestionRepository;

	public List<TakeExaminationsQuestion> findLatestsByTakeExaminationDate(User user, Date takeExaminationDate) {
		return takeExaminationsQuestionRepository.findLatestsByTakeExaminationDate(user, takeExaminationDate);
	}

	public List<TakeExaminationsQuestion> findLatestsByQuestionCategoryAndTakeExaminationDate(User user, QuestionCategory questionCategory, Date takeExaminationDate) {
		return takeExaminationsQuestionRepository.findLatestsByQuestionCategoryAndTakeExaminationDate(user, questionCategory, takeExaminationDate);
	}
	
	public Map<Question, SummaryByQuestion> summaryByUserAndQuestionCateogry(User user, QuestionCategory questionCategory) {
		Map<Question, SummaryByQuestion> resultMap = new HashMap<>();

		List<TakeExaminationsQuestion> takeExaminationsQuestions = takeExaminationsQuestionRepository.findByUserAndQuestionCategory(user, questionCategory);
		for(TakeExaminationsQuestion takeExaminationsQuestion : takeExaminationsQuestions) {
			Question question = takeExaminationsQuestion.getQuestion();
			if(resultMap.containsKey(question)) {
				SummaryByQuestion result = resultMap.get(question);
				if(takeExaminationsQuestion.isCorrect()) {
					result.setNumCorrect(result.getNumCorrect() + 1);
				} else {
					result.setNumWrong(result.getNumWrong() + 1);
				}
				resultMap.put(question, result);
			} else {
				SummaryByQuestion result = new SummaryByQuestion();
				result.setQuestion(question);
				if(takeExaminationsQuestion.isCorrect()) {
					result.setNumCorrect(1);
					result.setNumWrong(0);
				} else {
					result.setNumCorrect(0);
					result.setNumWrong(1);
				}
				resultMap.put(question, result);
			}
		}
		
		return resultMap;
	}
	
	public static class SummaryByQuestion {
		private Question question;
		private Integer numCorrect;
		private Integer numWrong;

		public Question getQuestion() {
			return question;
		}
		public void setQuestion(Question question) {
			this.question = question;
		}
		public Integer getNumCorrect() {
			return numCorrect;
		}
		public void setNumCorrect(Integer numCorrect) {
			this.numCorrect = numCorrect;
		}
		public Integer getNumWrong() {
			return numWrong;
		}
		public void setNumWrong(Integer numWrong) {
			this.numWrong = numWrong;
		}
		public double getCorrectPercentage() {
			if(getNumCorrect() == 0 && getNumWrong() == 0) {
				return 0;
			}
			
			return getNumCorrect() / (double)(getNumCorrect() + getNumWrong()) * 100;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((question == null) ? 0 : question.hashCode());
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
			SummaryByQuestion other = (SummaryByQuestion) obj;
			if (question == null) {
				if (other.question != null)
					return false;
			} else if (!question.equals(other.question))
				return false;
			return true;
		}
	}
}
