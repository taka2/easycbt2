package easycbt2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Examination;
import easycbt2.model.ExaminationsCategories;
import easycbt2.repository.ExaminationsCategoriesRepository;

@Service
public class ExaminationsCategoriesService {
	@Autowired
	ExaminationsCategoriesRepository examinationsCategoriesRepository;

    public List<ExaminationsCategories> findAll() {
        return examinationsCategoriesRepository.findAll();
    }

	public ExaminationsCategories findOne(Long id) {
		return examinationsCategoriesRepository.findById(id).get();
	}

    public ExaminationsCategories save(ExaminationsCategories examinationsCategories) {
        return examinationsCategoriesRepository.save(examinationsCategories);
    }

    public void delete(Long id) {
    	examinationsCategoriesRepository.deleteById(id);
    }

    public List<ExaminationsCategories> findByExamination(Examination examination) {
    	return examinationsCategoriesRepository.findByExamination(examination);
    }
}
