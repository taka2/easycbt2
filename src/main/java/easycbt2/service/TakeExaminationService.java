package easycbt2.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExamination;
import easycbt2.model.TakeExaminationsAnswer;
import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;
import easycbt2.repository.TakeExaminationRepository;
import easycbt2.repository.TakeExaminationsAnswerRepository;
import easycbt2.repository.TakeExaminationsQuestionRepository;

@Service
public class TakeExaminationService {
	@Autowired
	TakeExaminationRepository takeExaminationRepository;
	@Autowired
	TakeExaminationsQuestionRepository takeExaminationsQuestionRepository;
	@Autowired
	TakeExaminationsAnswerRepository takeExaminationsAnswerRepository;
	@Autowired
	ExaminationService examinationService;
	@Autowired
	QuestionService questionService;
	@Autowired
	FillExtractionConditionService fillExtractionConditionService;

	@Transactional
	public TakeExamination save(User user, Examination examination, List<Question> questions, List<String> answers, Instant startDateTime, Instant endDateTime) {
		TakeExamination takeExamination = new TakeExamination();
		takeExamination.setUser(user);
		takeExamination.setExamination(examination);
		takeExamination.setElapsedTime(Duration.between(startDateTime, endDateTime).toMillis());
		takeExaminationRepository.save(takeExamination);

		Set<TakeExaminationsQuestion> takeExaminationsQuestions = new HashSet<>();
		final int questionsSize = questions == null ? 0 : questions.size();
		for(int i=0; i<questionsSize; i++) {
			Question question = questions.get(i);
			String answer = null;
			if(answers != null && answers.size() >= (i+1)) {
				// radio/checkboxは回答なしの場合リストに含まれないので、questions.size > answers.sizeとなる可能性がある
				answer = answers.get(i);
			}

			// Questions
    		TakeExaminationsQuestion takeExaminationsQuestion = new TakeExaminationsQuestion();
    		takeExaminationsQuestion.setTakeExamination(takeExamination);
    		takeExaminationsQuestion.setQuestion(question);
    		takeExaminationsQuestion.setElapsedTime(-1L);
    		takeExaminationsQuestionRepository.save(takeExaminationsQuestion);

    		// Answers
			Set<TakeExaminationsAnswer> takeExaminationsAnswers = new HashSet<>();
			if(answer == null) {
				// no answer for the question
    			TakeExaminationsAnswer takeExaminationAnswer = new TakeExaminationsAnswer();
    			takeExaminationAnswer.setTakeExaminationsQuestion(takeExaminationsQuestion);
    			takeExaminationsAnswerRepository.save(takeExaminationAnswer);
    			takeExaminationsAnswers.add(takeExaminationAnswer);
			} else {
				String[] values = null;
				switch(question.getQuestionType()) {
					case MULTIPLE_CHOICE:
						values = answer.split(",");
						break;
					default:
						values = new String[1];
						values[0] = answer;						
				}

				for(String value : values) {
	    			TakeExaminationsAnswer takeExaminationAnswer = new TakeExaminationsAnswer();
	    			takeExaminationAnswer.setTakeExaminationsQuestion(takeExaminationsQuestion);
	        		switch(question.getQuestionType()) {
	        		case SINGLE_CHOICE:
	        		case MULTIPLE_CHOICE:
	        			takeExaminationAnswer.setAnswerId(Long.parseLong(value));
	        			break;
	        		case TEXT:
	        			takeExaminationAnswer.setAnswerText(value);
	        			break;
	        		default:
	        			System.err.println("Unsupported QuestionType");
	        		}
	    			takeExaminationsAnswerRepository.save(takeExaminationAnswer);
	    			takeExaminationsAnswers.add(takeExaminationAnswer);
				}
			}
			takeExaminationsQuestion.setTakeExaminationsAnswers(takeExaminationsAnswers);
			takeExaminationsQuestions.add(takeExaminationsQuestion);
		}
		takeExamination.setTakeExaminationsQuestions(takeExaminationsQuestions);

		return takeExamination;
	}

	public List<TakeExamination> findTakeExaminationsByUserOrderByIdDesc(User user) {
		return takeExaminationRepository.findByUserOrderByIdDesc(user);
	}
	
	public Page<TakeExamination> findByUserOrderByIdDescWithPageable(User user, Pageable pageable) {
		return takeExaminationRepository.findByUserOrderByIdDesc(user, pageable);
	}

	public TakeExamination findByIdAndUser(Long id, User user) {
		Optional<TakeExamination> takeExaminationOptional = takeExaminationRepository.findById(id);
		if(!takeExaminationOptional.isPresent()) {
			return null;
		}

		TakeExamination takeExamination = takeExaminationOptional.get();
		if(takeExamination.getUser().equals(user)) {
			return takeExamination;
		} else {
			return null;
		}
	}

	public List<TakeExamination> findByUserOrderByCreatedDateDesc(User user) {
		return takeExaminationRepository.findByUserOrderByCreatedDateDesc(user);
	}

	/**
	 * @param user
	 * @return userが受けた試験の問題の一覧をカテゴリごとに返す
	 */
	public Map<QuestionCategory, List<Question>> summaryByQuestionCategoryByUser(User user) {
		Map<QuestionCategory, List<Question>> summary = new HashMap<>();

		List<QuestionCategory> questionCategoryList = takeExaminationRepository.findQuestionCategoryByUserOrderByNameAsc(user);
		for(QuestionCategory questionCategory : questionCategoryList) {
			summary.put(questionCategory, takeExaminationRepository.findByUserAndQuestionCateogry(user, questionCategory));
		}
		
		return summary;
	}
	
	/**
	 * userが受けたFillモードの進捗をカテゴリごとに返す
	 * @param user
	 * @param questionCategory
	 * @return userが受けたFillモードのカテゴリごとの進捗
	 */
	public Map<QuestionCategory, FillProgressByQuestionCategory> getFillProgressByUser(User user) {
		Map<QuestionCategory, FillProgressByQuestionCategory> resultMap = new HashMap<>();

		// 受講履歴の抽出日付
		Date extractionDate = fillExtractionConditionService.findExtractionDateByUser(user);
		
		// 受講履歴のあるユーザごとのカテゴリー一覧を取得
		List<QuestionCategory> questionCategoryList = takeExaminationRepository.findQuestionCategoryByUserOrderByNameAsc(user);
		
		for(QuestionCategory questionCategory : questionCategoryList) {
			// 問題総数
			Integer questionCount = questionService.findByUserAndQuestionCategory(user, questionCategory).size();
			
			// 直近の正解／不正解を取得する
			List<TakeExaminationsQuestion> takeExaminationsQuestionList = takeExaminationsQuestionRepository.findLatestsByQuestionCategoryAndTakeExaminationDate(user, questionCategory, extractionDate);
			int latestCorrectCount = 0;
			int latestWrongCount = 0;
			for(TakeExaminationsQuestion teq : takeExaminationsQuestionList) {
				if(teq.isCorrect()) {
					latestCorrectCount++;
				} else {
					latestWrongCount++;
				}
			}
			
			FillProgressByQuestionCategory result = new FillProgressByQuestionCategory();
			result.setQuestionCategory(questionCategory);
			result.setQuestionCount(questionCount);
			result.setLatestCorrectCount(latestCorrectCount);
			result.setLatestWrongCount(latestWrongCount);
			resultMap.put(questionCategory, result);
		}
		
		return resultMap;
	}

	public static class FillProgressByQuestionCategory {
		private QuestionCategory questionCategory;
		private int questionCount;
		private int latestCorrectCount;
		private int latestWrongCount;

		public QuestionCategory getQuestionCategory() {
			return questionCategory;
		}
		public void setQuestionCategory(QuestionCategory questionCategory) {
			this.questionCategory = questionCategory;
		}
		public int getQuestionCount() {
			return questionCount;
		}
		public void setQuestionCount(int questionCount) {
			this.questionCount = questionCount;
		}
		public int getLatestCorrectCount() {
			return latestCorrectCount;
		}
		public void setLatestCorrectCount(int latestCorrectCount) {
			this.latestCorrectCount = latestCorrectCount;
		}
		public int getLatestWrongCount() {
			return latestWrongCount;
		}
		public void setLatestWrongCount(int latestWrongCount) {
			this.latestWrongCount = latestWrongCount;
		}

		public int getDoneCount() {
			return getLatestCorrectCount() + getLatestWrongCount();
		}
		
		public int getUndoneCount() {
			return getQuestionCount() - getDoneCount();
		}
		
		public double getProgress() {
			return (double)getDoneCount() / getQuestionCount();
		}
		
		public double getCorrectPercentage() {
			return (double)getLatestCorrectCount() / getDoneCount();
		}
	}
}
