package easycbt2.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
import org.springframework.util.MultiValueMap;

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

	@Transactional
	public TakeExamination save(User user, Examination examination, List<Question> questions, Instant startDateTime, Instant endDateTime, MultiValueMap<String, String> params) {
		TakeExamination takeExamination = new TakeExamination();
		takeExamination.setUser(user);
		takeExamination.setExamination(examination);
		takeExamination.setElapsedTime(Duration.between(startDateTime, endDateTime).toMillis());
		takeExaminationRepository.save(takeExamination);

		Set<TakeExaminationsQuestion> takeExaminationsQuestions = new HashSet<>();
		for(Question question : questions) {
			List<String> values = params.get(Long.toString(question.getId()));

			// Questions
    		TakeExaminationsQuestion takeExaminationsQuestion = new TakeExaminationsQuestion();
    		takeExaminationsQuestion.setTakeExamination(takeExamination);
    		takeExaminationsQuestion.setQuestion(question);
    		takeExaminationsQuestion.setElapsedTime(-1L);
    		takeExaminationsQuestionRepository.save(takeExaminationsQuestion);

    		// Answers
			Set<TakeExaminationsAnswer> takeExaminationsAnswers = new HashSet<>();
			if(values == null) {
    			TakeExaminationsAnswer takeExaminationAnswer = new TakeExaminationsAnswer();
    			takeExaminationAnswer.setTakeExaminationsQuestion(takeExaminationsQuestion);
    			takeExaminationsAnswerRepository.save(takeExaminationAnswer);
    			takeExaminationsAnswers.add(takeExaminationAnswer);
			} else {
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
	
	public List<TakeExamination> findByUser(User user) {
		return takeExaminationRepository.findByUser(user);
	}

	public Map<QuestionCategory, List<Question>> summaryByQuestionCategoryByUser(User user) {
		Map<QuestionCategory, List<Question>> summary = new HashMap<>();

		for(TakeExamination takeExamination : findByUser(user)) {
			for(TakeExaminationsQuestion question : takeExamination.getTakeExaminationsQuestions()) {
				QuestionCategory questionCategory = question.getQuestion().getQuestionCategory();
				if(summary.containsKey(questionCategory)) {
					List<Question> questions = summary.get(questionCategory);
					if(!questions.contains(question.getQuestion())) {
						questions.add(question.getQuestion());
						summary.put(questionCategory, questions);
					}
				} else {
					List<Question> questions = new ArrayList<>();
					questions.add(question.getQuestion());
					summary.put(questionCategory, questions);
				}
			}
		}
		
		return summary;
	}
}
