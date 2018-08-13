package easycbt2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;
import easycbt2.repository.TakeExaminationsQuestionRepository;

@Service
public class TakeExaminationsQuestionService {
	@Autowired
	TakeExaminationsQuestionRepository takeExaminationsQuestionRepository;

	public List<TakeExaminationsQuestion> findLatests(User user) {
		return takeExaminationsQuestionRepository.findLatests(user);
	}
}
