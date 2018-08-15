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
		List<Question> questions = findByUser(user);
		Question result = null;
		for(Question question : questions) { 
			if(question.getId() == id) {
				result = question;
				break;
			}
		}
		
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
		List<Question> questions = findByUserAndExamination(user, examination);

		// Calc weight
		Map<Question, Long> weightMap = calcWeight(user, questions);
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

	public Map<Question, Long> calcWeight(User user, List<Question> questions) {
		Map<Question, Long> resultMap = new HashMap<>();
		
		// Initialize map
		for(Question question : questions) {
			resultMap.put(question, 0L);
		}
		
		// Current Date
		Instant instant = dateTimeService.getCurrentDateTime();
		List<TakeExaminationsQuestion> takeExaminationsQuestions = takeExaminationsQuestionService.findLatests(user);
		for(TakeExaminationsQuestion takeExaminationsQuestion : takeExaminationsQuestions) {
			if(!resultMap.containsKey(takeExaminationsQuestion.getQuestion())) {
				continue;
			}
			Boolean isCorrect = takeExaminationsQuestion.isCorrect();
			Date timestamp = takeExaminationsQuestion.getModifiedDate();
			// TODO オーバーフローする可能性
			Long score = Duration.between(timestamp.toInstant(), instant).getSeconds() * (isCorrect ? 1 : 100);
			resultMap.put(takeExaminationsQuestion.getQuestion(), score);
		}
		
		// Reset Unanswered Questios score to Long.MAX_VALUE
		for(Entry<Question, Long> entry : resultMap.entrySet()) {
			if(entry.getValue() == 0L) {
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
