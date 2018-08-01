package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionsAuthPublic;
import easycbt2.repository.QuestionsAuthPublicRepository;

@Service
public class QuestionsAuthPublicService {
	@Autowired
	QuestionsAuthPublicRepository questionsAuthPublicRepository;

    public QuestionsAuthPublic save(QuestionsAuthPublic questionsAuthPublic) {
        return questionsAuthPublicRepository.save(questionsAuthPublic);
    }
}
