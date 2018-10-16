package easycbt2.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;
import easycbt2.repository.QuestionRepository;
import easycbt2.repository.QuestionsAuthPublicRepository;
import easycbt2.repository.QuestionsAuthUsersRepository;

@Service
public class QuestionService {
	@Autowired
	private UserService userService;
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	QuestionsAuthPublicRepository questionsAuthPublicRepository;
	@Autowired
	QuestionsAuthUsersRepository questionsAuthUsersRepository;
	@Autowired
	TakeExaminationService takeExaminationService;
	@Autowired
	TakeExaminationsQuestionService takeExaminationsQuestionService;
	@Autowired
	DateTimeService dateTimeService;

	private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
	
	public List<Question> findAll() {
        return questionRepository.findByEnabledOrderByIdAsc(true);
    }

    public Question findOne(Long id) {
        return questionRepository.findByIdAndEnabled(id, true);
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public void delete(Long id) {
    	Question obj = findOne(id);
    	obj.setEnabled(false);
    	save(obj);
    }

	public List<Question> findByUserAndQuestionCategory(User user, QuestionCategory questionCategory) {
		List<Question> resultList = new ArrayList<>();
		
		List<Question> questions = findByUser(user);
		for(Question question : questions) {
			if(question.getQuestionCategory().equals(questionCategory)) {
				resultList.add(question);
			}
		}
		
		return resultList;
	}

	public List<Question> findByUser(User user) {
		//List<Question> resultList = new ArrayList<>();
		Set<Question> resultSet = new HashSet<>();
		
		// List Public Questions
		resultSet.addAll(questionsAuthPublicRepository.findQuestions());
		
		// List Questions restricted by user
		resultSet.addAll(questionsAuthUsersRepository.findQuestionsByUser(user));
		List<Question> resultList = new ArrayList<>(resultSet);

		Collections.sort(resultList, Question.getIdComparator());
		return resultList;
	}

	public Question findByIdAndUser(Long id, User user) {
		logger.info("id = " + id);
		logger.info("username = " + user.getUsername());
		logger.info("hasAdminRole = " + userService.hasAdminRole(user));
		List<Question> questions = findByUser(user);

		Question result = null;
		for(Question question : questions) { 
			logger.info("question.id/text = " + question.getId() + "/" + question.getText());
			if(question.getId().equals(id)) {
				logger.info("hit! " + question.getId());
				result = question;
				break;
			}
		}
		
		logger.info("result = " + result);
		return result;
	}

	public List<Question> findByUserAndExamination(User user, Examination examination) {
		Set<Question> resultSet = new HashSet<>();
		
		// List Public Questions
		resultSet.addAll(questionsAuthPublicRepository.findQuestionsByExamination(examination));
		
		// List Questions restricted by user
		resultSet.addAll(questionsAuthUsersRepository.findQuestionsByUserAndExamination(user, examination));

		List<Question> resultList = new ArrayList<>(resultSet);
		Collections.sort(resultList, Question.getIdComparator());
		
		return resultList;
	}

	public List<Question> findByUserAndExaminationWithRandomize(User user, Examination examination) {
		return findByUserAndExaminationWithRandomize(user, examination, true, null);
	}

	public List<Question> findByUserAndExaminationWithRandomize(User user, Examination examination, Boolean isIncludeCorrect, Date takeExaminationDate) {
		List<Question> questions = findByUserAndExamination(user, examination);

		// Calc weight
		Map<Question, Long> weightMap = calcWeight(user, questions, isIncludeCorrect, takeExaminationDate);
		List<Entry<Question, Long>> list = new ArrayList<>(weightMap.entrySet());
		Collections.sort(list, new Comparator<Entry<Question, Long>>() {
			@Override
			public int compare(Entry<Question, Long> arg0, Entry<Question, Long> arg1) {
				return Long.compare(arg0.getValue(), arg1.getValue()) * (-1);
			}
		});
		List<Question> weightedList = new ArrayList<>();
		for(Entry<Question, Long> anElement : list) {
			weightedList.add(anElement.getKey());
		}
		
		// Limit size
		int indexTo = Math.min(weightedList.size(), examination.getQuestionCount());
		weightedList = weightedList.subList(0, indexTo);

		// Randomize
		Collections.shuffle(weightedList);

		return weightedList;
	}

	public Map<Question, Long> calcWeightAll(User user, List<Question> questions) {
		return calcWeight(user, questions, true, null);
	}
	
	public Map<Question, Long> calcWeightExceptCorrect(User user, List<Question> questions, Date takeExaminationDate) {
		return calcWeight(user, questions, false, takeExaminationDate);
	}

	/**
	 * 指定ユーザの受験履歴をもとに、指定問題リストの出題優先度を計算する。
	 * @param user ユーザ
	 * @param questions 問題リスト
	 * @param isIncludeCorrect 正解を含めるかどうか、true: 正解を含める、false: 正解を含めない（未実施、または、不正解のみ）
	 * @param takeExaminationDate この日時以降の受験履歴のみ参照する
	 * @return 問題と優先度のMap
	 */
	public Map<Question, Long> calcWeight(User user, List<Question> questions, Boolean isIncludeCorrect, Date takeExaminationDate) {
		Map<Question, Long> resultMap = new HashMap<>();
		
		// Initialize map
		for(Question question : questions) {
			resultMap.put(question, 0L);
		}
		
		// Current Date
		Instant instant = dateTimeService.getCurrentDateTime();
		List<TakeExaminationsQuestion> takeExaminationsQuestions = null;
		if(takeExaminationDate != null) {
			takeExaminationsQuestions = takeExaminationsQuestionService.findLatestsByTakeExaminationDate(user, takeExaminationDate);
		} else {
			takeExaminationsQuestions = takeExaminationsQuestionService.findLatests(user);
		}
		for(TakeExaminationsQuestion takeExaminationsQuestion : takeExaminationsQuestions) {
			if(!resultMap.containsKey(takeExaminationsQuestion.getQuestion())) {
				continue;
			}
			Boolean isCorrect = takeExaminationsQuestion.isCorrect();
			Date timestamp = takeExaminationsQuestion.getModifiedDate();
			// TODO オーバーフローする可能性
			Long score = Duration.between(timestamp.toInstant(), instant).getSeconds() * (isCorrect ? 1 : 100);
			if(!isIncludeCorrect && isCorrect) {
				resultMap.remove(takeExaminationsQuestion.getQuestion());
			} else {
				resultMap.put(takeExaminationsQuestion.getQuestion(), score);
			}
		}
		
		// Reset Unanswered Questios score to Long.MAX_VALUE
		for(Entry<Question, Long> entry : resultMap.entrySet()) {
			if(entry.getValue().equals(0L)) {
				resultMap.put(entry.getKey(), Long.MAX_VALUE);
			}
		}
		
		return resultMap;
	}

	public boolean canWrite(Long id, User user) {
		Question question = findByIdAndUser(id, user);
		
		if(user.getUsername().equals(question.getCreatedBy()) 
				|| userService.hasAdminRole(user)) {
			// CreatedBy user or has ADMIN_ROLE
			return true;
		}
		return false;
	}
}
