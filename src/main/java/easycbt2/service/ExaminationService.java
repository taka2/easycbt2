package easycbt2.service;

import java.util.ArrayList;
import java.util.Collections;
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
	private UserService userService;
	@Autowired
	ExaminationRepository examinationRepository;
	@Autowired
	ExaminationsAuthPublicRepository examinationsAuthPublicRepository;
	@Autowired
	ExaminationsAuthUsersRepository examinationsAuthUsersRepository;

    public List<Examination> findAll() {
    	return examinationRepository.findByEnabledOrderByIdAsc(true);
    }

	public Examination findOne(Long id) {
		return examinationRepository.findByIdAndEnabled(id, true);
	}

    public Examination save(Examination examination) {
        return examinationRepository.save(examination);
    }

    public void delete(Long id) {
    	Examination obj = findOne(id);
    	obj.setEnabled(false);
    	save(obj);
    }

	public List<Examination> findByUser(User user) {
		List<Examination> resultList = new ArrayList<>();
		
		// List Public Examinations
		List<ExaminationsAuthPublic> listPublic = examinationsAuthPublicRepository.findAll();
		for(ExaminationsAuthPublic anElement : listPublic) {
			if(anElement.getExamination().getEnabled()) {
				resultList.add(anElement.getExamination());
			}
		}
		
		// List Examinations restricted by user
		List<ExaminationsAuthUsers> listUsers = examinationsAuthUsersRepository.findByUser(user);
		for(ExaminationsAuthUsers anElement : listUsers) {
			if(anElement.getExamination().getEnabled()) {
				if(!resultList.contains(anElement.getExamination())) {
					resultList.add(anElement.getExamination());
				}
			}
		}

		Collections.sort(resultList, Examination.getIdComparator());
		return resultList;
	}
	
	public Examination findByIdAndUser(Long id, User user) {
		List<Examination> examinations = findByUser(user);
		Examination result = null;
		for(Examination examination : examinations) { 
			if(examination.getId().equals(id)) {
				result = examination;
				break;
			}
		}
		
		return result;
	}

	public boolean canWrite(Long id, User user) {
		Examination examination = findByIdAndUser(id, user);
		if(user.getUsername().equals(examination.getCreatedBy()) 
				|| userService.hasAdminRole(user)) {
			// CreatedBy user or has ADMIN_ROLE
			return true;
		}
		return false;
	}
}
