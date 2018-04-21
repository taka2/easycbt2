package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionAnswer;
import easycbt2.repository.QuestionAnswerRepository;

@Service
public class QuestionAnswerService {
	@Autowired
	QuestionAnswerRepository questionAnswerRepository;

    public QuestionAnswer save(QuestionAnswer questionAnswer) {
        return questionAnswerRepository.save(questionAnswer);
    }
}
