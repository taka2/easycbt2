package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.QuestionsAuthUsers;
import easycbt2.model.User;

public interface QuestionsAuthUsersRepository extends JpaRepository<QuestionsAuthUsers, Long> {
	public List<QuestionsAuthUsers> findByUser(User user);
}