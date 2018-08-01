package easycbt2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	public User registerUser(String username, String password, String role, Boolean enabled) {
		User user = new User();
		user.setUsername(username);
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		user.setEnabled(enabled);
		user = userRepository.save(user);
		
		Authority authority = new Authority();
		authority.setUsername(username);
		authority.setAuthority(role);
		authorityRepository.save(authority);

		return user;
	}

	public User registerUser(String username, String password, String role) {
		return registerUser(username, password, role, true);
	}

    public List<User> findAll() {
        return userRepository.findAllByOrderByUsernameAsc();
    }

    public User findOne(String id) {
        return userRepository.findById(id).get();
    }

    public User save(User user) {
        return registerUser(user.getUsername(), user.getPassword(), ROLE_USER, user.isEnabled());
    }

    public void delete(String id) {
    	User obj = findOne(id);
    	obj.setEnabled(false);
    	save(obj);
    }
    
    public User getLoginUser() {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	return findOne(username);
    }
}
