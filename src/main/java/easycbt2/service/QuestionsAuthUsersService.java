package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.QuestionsAuthUsers;
import easycbt2.repository.QuestionsAuthUsersRepository;

@Service
public class QuestionsAuthUsersService {
	@Autowired
	QuestionsAuthUsersRepository questionAuthUsersRepository;

    public QuestionsAuthUsers save(QuestionsAuthUsers questionsAuthUsers) {
        return questionAuthUsersRepository.save(questionsAuthUsers);
    }
}
