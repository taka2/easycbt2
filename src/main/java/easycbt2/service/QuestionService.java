package easycbt2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.repository.QuestionRepository;

@Service
public class QuestionService {
	@Autowired
	QuestionRepository questionRepository;

	public List<Question> getQuestionsByCategory(QuestionCategory questionCategory) {
		return questionRepository.findByQuestionCategory(questionCategory);
	}
}
