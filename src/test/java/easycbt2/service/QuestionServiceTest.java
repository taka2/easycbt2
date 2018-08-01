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
import easycbt2.model.Question;
import easycbt2.model.User;
import easycbt2.repository.ExaminationRepository;
import easycbt2.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ExaminationRepository examinationRepository;
	@Autowired
	QuestionService questionService;
	
	private User user3;
	private User user4;
	private Examination examination3;
	private Examination examination4;
	private Examination examination5;
	
	@Before
	public void before() {
        user3 = userRepository.findById("user3").get();
        user4 = userRepository.findById("user4").get();
        
        examination3 = examinationRepository.findById(3L).get();
        examination4 = examinationRepository.findById(4L).get();
        examination5 = examinationRepository.findById(5L).get();
	}

	@Test
	public void testQuestionUser3Examination3() {
		List<Question> questions = questionService.findByUserAndExamination(user3, examination3);
		assertThat(questions.size(), is(2));
	}
	@Test
	public void testQuestionUser3Examination4() {
		List<Question> questions = questionService.findByUserAndExamination(user3, examination4);
		assertThat(questions.size(), is(2));
	}
	@Test
	public void testQuestionUser3Examination5() {
		List<Question> questions = questionService.findByUserAndExamination(user3, examination5);
		assertThat(questions.size(), is(4));
	}
	@Test
	public void testQuestionUser4Examination3() {
		List<Question> questions = questionService.findByUserAndExamination(user4, examination3);
		assertThat(questions.size(), is(1));
	}
	@Test
	public void testQuestionUser4Examination4() {
		List<Question> questions = questionService.findByUserAndExamination(user4, examination4);
		assertThat(questions.size(), is(1));
	}
	@Test
	public void testQuestionUser4Examination5() {
		List<Question> questions = questionService.findByUserAndExamination(user4, examination5);
		assertThat(questions.size(), is(2));
	}
}
