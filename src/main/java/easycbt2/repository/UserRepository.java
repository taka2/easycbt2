package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	public List<User> findAllByOrderByUsernameAsc();
}