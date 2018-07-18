package easycbt2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionCategory;
import easycbt2.repository.QuestionCategoryRepository;

@Service
public class QuestionCategoryService {
    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

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
}
