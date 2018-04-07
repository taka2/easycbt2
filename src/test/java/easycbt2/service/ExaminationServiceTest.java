package easycbt2.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import easycbt2.model.Examination;
import easycbt2.model.User;
import easycbt2.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExaminationServiceTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ExaminationService examinationService;
	
	private User user1;
	private User user2;

	@Before
	public void before() {
        user1 = userRepository.findById("user1").get();
        user2 = userRepository.findById("user2").get();
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
