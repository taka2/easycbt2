package easycbt2.service;

import java.util.List;

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

	public Boolean isExistAdminUser() {
		return isExistsUser(ADMIN_USER_NAME);
	}
	
	public Boolean isExistsUser(String username) {
		return userRepository.existsById(username);
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
		authority.setAuthority(role);
		authorityRepository.save(authority);

		return user;
	}

    public List<User> findAll() {
        return userRepository.findAllByOrderByUsernameAsc();
    }

    public User findOne(String id) {
        return userRepository.findById(id).get();
    }

    public User save(User user) {
        return registerUser(user.getUsername(), user.getPassword(), ROLE_USER);
    }

    public void delete(String id) {
    	userRepository.deleteById(id);
    }
}
