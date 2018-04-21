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
        return questionCategoryRepository.findAll();
    }

    public QuestionCategory findOne(Long id) {
        return questionCategoryRepository.getOne(id);
    }

    public QuestionCategory save(QuestionCategory questionCategory) {
        return questionCategoryRepository.save(questionCategory);
    }

    public void delete(Long id) {
    	questionCategoryRepository.deleteById(id);
    }
    
    public QuestionCategory findByName(String name) {
    	return questionCategoryRepository.findByName(name);
    }
}
