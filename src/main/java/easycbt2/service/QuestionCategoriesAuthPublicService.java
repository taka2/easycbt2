package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionCategoriesAuthPublic;
import easycbt2.repository.QuestionCategoriesAuthPublicRepository;

@Service
public class QuestionCategoriesAuthPublicService {
	@Autowired
	QuestionCategoriesAuthPublicRepository questionCategoriesAuthPublicRepository;

    public QuestionCategoriesAuthPublic save(QuestionCategoriesAuthPublic questionCategoriesAuthPublic) {
        return questionCategoriesAuthPublicRepository.save(questionCategoriesAuthPublic);
    }
}
