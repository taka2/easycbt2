package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionCategoriesAuthUsers;
import easycbt2.repository.QuestionCategoriesAuthUsersRepository;

@Service
public class QuestionCategoriesAuthUsersService {
	@Autowired
	QuestionCategoriesAuthUsersRepository questionCategoriesAuthUsersRepository;

    public QuestionCategoriesAuthUsers save(QuestionCategoriesAuthUsers questionCategoriesAuthUsers) {
        return questionCategoriesAuthUsersRepository.save(questionCategoriesAuthUsers);
    }
}
