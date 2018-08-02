package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.QuestionCategoriesAuthUsers;
import easycbt2.model.User;

public interface QuestionCategoriesAuthUsersRepository extends JpaRepository<QuestionCategoriesAuthUsers, Long> {
	public List<QuestionCategoriesAuthUsers> findByUser(User user);
}