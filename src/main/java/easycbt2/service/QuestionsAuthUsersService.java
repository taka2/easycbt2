package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionsAuthUsers;
import easycbt2.repository.QuestionsAuthUsersRepository;

@Service
public class QuestionsAuthUsersService {
	@Autowired
	QuestionsAuthUsersRepository questionsAuthUsersRepository;

    public QuestionsAuthUsers save(QuestionsAuthUsers questionsAuthUsers) {
        return questionsAuthUsersRepository.save(questionsAuthUsers);
    }
}
