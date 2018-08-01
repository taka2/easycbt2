package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.ExaminationsAuthPublic;
import easycbt2.repository.ExaminationsAuthPublicRepository;

@Service
public class ExaminationsAuthPublicService {
	@Autowired
	ExaminationsAuthPublicRepository examinationsAuthPublicRepository;

    public ExaminationsAuthPublic save(ExaminationsAuthPublic examinationsAuthPublic) {
        return examinationsAuthPublicRepository.save(examinationsAuthPublic);
    }
}
