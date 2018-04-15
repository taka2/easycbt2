package easycbt2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.QuestionsAuthPublic;
import easycbt2.model.QuestionsAuthUsers;
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

	public Question getQuestionById(Long id) {
		return questionRepository.findById(id).get();
	}

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

		// Randomize
		Collections.shuffle(resultList);

		// Limit size
		int indexTo = Math.min(resultList.size(), examination.getQuestionCount());
		resultList = resultList.subList(0, indexTo);

		return resultList;
	}
}
