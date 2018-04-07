package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.ExaminationsAuthUsers;
import easycbt2.model.User;

public interface ExaminationsAuthUsersRepository extends JpaRepository<ExaminationsAuthUsers, Long> {
	public List<ExaminationsAuthUsers> findByUser(User user);
}