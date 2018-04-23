package easycbt2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.Examination;
import easycbt2.model.ExaminationsAuthPublic;
import easycbt2.model.ExaminationsAuthUsers;
import easycbt2.model.User;
import easycbt2.repository.ExaminationRepository;
import easycbt2.repository.ExaminationsAuthPublicRepository;
import easycbt2.repository.ExaminationsAuthUsersRepository;

@Service
public class ExaminationService {
	@Autowired
	ExaminationRepository examinationRepository;
	@Autowired
	ExaminationsAuthPublicRepository examinationsAuthPublicRepository;
	@Autowired
	ExaminationsAuthUsersRepository examinationsAuthUsersRepository;

    public List<Examination> findAll() {
        return examinationRepository.findAll();
    }

	public Examination findOne(Long id) {
		return examinationRepository.findById(id).get();
	}

    public Examination save(Examination examination) {
        return examinationRepository.save(examination);
    }

    public void delete(Long id) {
    	examinationRepository.deleteById(id);
    }

	public List<Examination> findByUser(User user) {
		List<Examination> resultList = new ArrayList<>();
		
		// List Public Examinations
		List<ExaminationsAuthPublic> listPublic = examinationsAuthPublicRepository.findAll();
		for(ExaminationsAuthPublic anElement : listPublic) {
			resultList.add(anElement.getExamination());
		}
		
		// List Examinations restricted by user
		List<ExaminationsAuthUsers> listUsers = examinationsAuthUsersRepository.findByUser(user);
		for(ExaminationsAuthUsers anElement : listUsers) {
			resultList.add(anElement.getExamination());
		}

		return resultList;
	}
}
