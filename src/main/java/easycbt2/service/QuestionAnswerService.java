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

    public QuestionAnswer findOne(Long id) {
    	return questionAnswerRepository.findByIdAndEnabled(id, true);
    }

    public QuestionAnswer save(QuestionAnswer questionAnswer) {
        return questionAnswerRepository.save(questionAnswer);
    }

    public List<QuestionAnswer> findByQuestion(Question question) {
    	return questionAnswerRepository.findByQuestionAndEnabled(question, true);
    }

    public void delete(Long id) {
    	QuestionAnswer obj = findOne(id);
    	obj.setEnabled(false);
    	save(obj);
    }
}
