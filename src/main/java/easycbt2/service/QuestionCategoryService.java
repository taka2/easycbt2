package easycbt2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionCategoriesAuthPublic;
import easycbt2.model.QuestionCategoriesAuthUsers;
import easycbt2.model.QuestionCategory;
import easycbt2.model.User;
import easycbt2.repository.QuestionCategoriesAuthPublicRepository;
import easycbt2.repository.QuestionCategoriesAuthUsersRepository;
import easycbt2.repository.QuestionCategoryRepository;

@Service
public class QuestionCategoryService {
    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;
	@Autowired
	QuestionCategoriesAuthPublicRepository questionCategoriesAuthPublicRepository;
	@Autowired
	QuestionCategoriesAuthUsersRepository questionCategoriesAuthUsersRepository;

    public List<QuestionCategory> findAll() {
    	return questionCategoryRepository.findByEnabledOrderByIdAsc(true);
    }

    public QuestionCategory findOne(Long id) {
    	return questionCategoryRepository.findByIdAndEnabled(id, true);
    }

    public QuestionCategory save(QuestionCategory questionCategory) {
        return questionCategoryRepository.save(questionCategory);
    }

    public void delete(Long id) {
    	QuestionCategory obj = findOne(id);
    	obj.setEnabled(false);
    	save(obj);
    }
    
    public QuestionCategory findByName(String name) {
    	return questionCategoryRepository.findByNameAndEnabled(name, true);
    }

	public List<QuestionCategory> findByUser(User user) {
		List<QuestionCategory> resultList = new ArrayList<>();
		
		// List Public QuestionCategories
		List<QuestionCategoriesAuthPublic> listPublic = questionCategoriesAuthPublicRepository.findAll();
		for(QuestionCategoriesAuthPublic anElement : listPublic) {
			if(anElement.getQuestionCategory().getEnabled()) {
				resultList.add(anElement.getQuestionCategory());
			}
		}
		
		// List QuestionCategories restricted by user
		List<QuestionCategoriesAuthUsers> listUsers = questionCategoriesAuthUsersRepository.findByUser(user);
		for(QuestionCategoriesAuthUsers anElement : listUsers) {
			if(anElement.getQuestionCategory().getEnabled()) {
				if(!resultList.contains(anElement.getQuestionCategory())) {
					resultList.add(anElement.getQuestionCategory());
				}
			}
		}

		Collections.sort(resultList, QuestionCategory.getIdComparator());
		return resultList;
	}
	
	public QuestionCategory findByIdAndUser(Long id, User user) {
		List<QuestionCategory> questionCategories = findByUser(user);
		QuestionCategory result = null;
		for(QuestionCategory questionCategory : questionCategories) { 
			if(questionCategory.getId() == id) {
				result = questionCategory;
				break;
			}
		}
		
		return result;
	}
}
