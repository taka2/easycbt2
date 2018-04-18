package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import easycbt2.model.Authority;
import easycbt2.model.User;
import easycbt2.repository.AuthorityRepository;
import easycbt2.repository.UserRepository;

@Service
public class UserService {
	public static String ADMIN_USER_NAME = "admin";
	public static String ROLE_ADMIN = "ROLE_ADMIN";
	public static String ROLE_USER = "ROLE_USER";

	@Autowired
	UserRepository userRepository;
	@Autowired
	AuthorityRepository authorityRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User findByUsername(String username) {
		return userRepository.findById(username).get();
	}

	public Boolean isExistAdminUser() {
		return userRepository.existsById(ADMIN_USER_NAME);
	}
	
	public User registerUser(String username, String password, String role) {
		User user = new User();
		user.setUsername(username);
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		user.setEnabled(true);
		user = userRepository.save(user);
		
		Authority authority = new Authority();
		authority.setUsername(username);
		authority.setAuthority(ROLE_ADMIN);
		authorityRepository.save(authority);

		return user;
	}
}
