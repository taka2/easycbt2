package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.ExaminationsAuthUsers;
import easycbt2.repository.ExaminationsAuthUsersRepository;

@Service
public class ExaminationsAuthUsersService {
	@Autowired
	ExaminationsAuthUsersRepository examinationsAuthUsersRepository;

    public ExaminationsAuthUsers save(ExaminationsAuthUsers examinationsAuthUsers) {
        return examinationsAuthUsersRepository.save(examinationsAuthUsers);
    }
}
