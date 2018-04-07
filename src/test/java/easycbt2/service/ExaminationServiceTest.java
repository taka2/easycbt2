package easycbt2.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import easycbt2.model.Examination;
import easycbt2.model.ExaminationsAuthPublic;
import easycbt2.model.ExaminationsAuthUsers;
import easycbt2.model.User;
import easycbt2.repository.ExaminationRepository;
import easycbt2.repository.ExaminationsAuthPublicRepository;
import easycbt2.repository.ExaminationsAuthUsersRepository;
import easycbt2.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExaminationServiceTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ExaminationRepository examinationRepository;
	@Autowired
	ExaminationsAuthPublicRepository examinationsAuthPublicRepository;
	@Autowired
	ExaminationsAuthUsersRepository examinationsAuthUsersRepository;
	@Autowired
	ExaminationService examinationService;
	
	private User user1;
	private User user2;
	
	private static boolean initialized;

	@Before
	public void before() {
		if(initialized) {
			return;
		}

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create Users
        user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);
        user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);

        // Create Examinations
        Examination examinationPublic = new Examination();
        examinationPublic.setText("examinationPublic");
        examinationPublic.setQuestionCount(10);
        examinationRepository.save(examinationPublic);

        Examination examinationForUser1 = new Examination();
        examinationForUser1.setText("examinationForUser1");
        examinationForUser1.setQuestionCount(20);
        examinationRepository.save(examinationForUser1);
        
        // Make examinationPublic public
        ExaminationsAuthPublic authPublic = new ExaminationsAuthPublic();
        authPublic.setExamination(examinationPublic);
        examinationsAuthPublicRepository.save(authPublic);

        // Make examinationForUser1 public for user1
        ExaminationsAuthUsers authUsers1 = new ExaminationsAuthUsers();
        authUsers1.setExamination(examinationForUser1);
        authUsers1.setUser(user1);
        examinationsAuthUsersRepository.save(authUsers1);
        
        initialized = true;
	}

	@Test
	public void testGetExaminationsByUser1() {
		List<Examination> examinations = examinationService.getExaminationsByUser(user1);
		assertThat(examinations.size(), is(2));
	}

	@Test
	public void testGetExaminationsByUser2() {
		List<Examination> examinations = examinationService.getExaminationsByUser(user2);
		assertThat(examinations.size(), is(1));
		assertThat(examinations.get(0).getText(), is("examinationPublic"));
	}
}
