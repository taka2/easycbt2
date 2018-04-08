package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.User;
import easycbt2.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	public User findByUsername(String username) {
		return userRepository.findById(username).get();
	}
}
