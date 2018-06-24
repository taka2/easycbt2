package easycbt2.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.QuestionsAuthPublic;
import easycbt2.model.QuestionsAuthUsers;
import easycbt2.model.TakeExamination;
import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;
import easycbt2.repository.QuestionRepository;
import easycbt2.repository.QuestionsAuthPublicRepository;
import easycbt2.repository.QuestionsAuthUsersRepository;

@Service
public class QuestionService {
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	QuestionsAuthPublicRepository questionsAuthPublicRepository;
	@Autowired
	QuestionsAuthUsersRepository questionsAuthUsersRepository;
	@Autowired
	TakeExaminationService takeExaminationService;
	@Autowired
	DateTimeService dateTimeService; 

	public List<Question> getQuestionsByUserAndExamination(User user, Examination examination) {
		return getQuestionsByUserAndExamination(user, examination, false);
	}

	public List<Question> getQuestionsByUserAndExamination(User user, Examination examination, Boolean isRandomize) {
		List<Question> resultList = new ArrayList<>();

		// Get Examination categories
		List<QuestionCategory> categoryList = examination.getCategories();

		// List Public Questions
		List<QuestionsAuthPublic> listPublic = questionsAuthPublicRepository.findAll();
		for(QuestionsAuthPublic anElement : listPublic) {
			Question question = anElement.getQuestion();
			if(categoryList.contains(question.getQuestionCategory())) {
				resultList.add(question);
			}
		}
		
		// List Questions restricted by user
		List<QuestionsAuthUsers> listUsers = questionsAuthUsersRepository.findByUser(user);
		for(QuestionsAuthUsers anElement : listUsers) {
			Question question = anElement.getQuestion();
			if(categoryList.contains(question.getQuestionCategory())) {
				resultList.add(question);
			}
		}

		// Calc weight
		Map<Question, Long> weightMap = calcWeight(user, resultList);
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
		
		List<TakeExamination> takeExaminations = takeExaminationService.findByUser(user);
		for(TakeExamination takeExamination : takeExaminations) {
			for(TakeExaminationsQuestion takeExaminationsQuestion : takeExamination.getTakeExaminationsQuestions()) {
				Question question = takeExaminationsQuestion.getQuestion();
				if(!resultMap.containsKey(question)) {
					continue;
				}
				
				Boolean isCorrect = takeExaminationsQuestion.isCorrect();
				Date timestamp = takeExaminationsQuestion.getModifiedDate();
				Long score = Duration.between(instant, timestamp.toInstant()).getSeconds() * (isCorrect ? 1 : -1);
				resultMap.put(question, resultMap.get(question) + score);
			}
		}
		
		// Reset Unanswered Questios score to Long.MAX_VALUE
		for(Entry<Question, Long> entry : resultMap.entrySet()) {
			if(entry.getValue() == 0L) {
				resultMap.put(entry.getKey(), Long.MAX_VALUE);
			}
		}
		
		return resultMap;
	}

    public List<Question> findAll() {
        return questionRepository.findByEnabled(true);
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
    
    public List<Question> findByQuestionCategory(QuestionCategory questionCategory) {
    	return questionRepository.findByQuestionCategoryAndEnabled(questionCategory, true);
    }
}
