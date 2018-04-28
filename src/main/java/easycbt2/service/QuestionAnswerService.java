package easycbt2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Question;
import easycbt2.model.QuestionAnswer;
import easycbt2.repository.QuestionAnswerRepository;

@Service
public class QuestionAnswerService {
	@Autowired
	QuestionAnswerRepository questionAnswerRepository;

    public QuestionAnswer save(QuestionAnswer questionAnswer) {
        return questionAnswerRepository.save(questionAnswer);
    }

    public List<QuestionAnswer> findByQuestion(Question question) {
    	return questionAnswerRepository.findByQuestion(question);
    }

    public void delete(Long id) {
    	questionAnswerRepository.deleteById(id);
    }
}
