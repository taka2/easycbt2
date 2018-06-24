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
    	return examinationsCategoriesRepository.findByEnabled(true);
    }

	public ExaminationsCategories findOne(Long id) {
		return examinationsCategoriesRepository.findByIdAndEnabled(id, true);
	}

    public ExaminationsCategories save(ExaminationsCategories examinationsCategories) {
        return examinationsCategoriesRepository.save(examinationsCategories);
    }

    public void delete(Long id) {
    	ExaminationsCategories obj = findOne(id);
    	obj.setEnabled(false);
    	save(obj);
    }

    public List<ExaminationsCategories> findByExamination(Examination examination) {
    	return examinationsCategoriesRepository.findByExaminationAndEnabled(examination, true);
    }
}
