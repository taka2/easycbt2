package easycbt2.repository;

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
import easycbt2.service.ExaminationService;
import easycbt2.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionsAuthUsersRepositoryTest {
	@Autowired
	UserService userService;
	@Autowired
	ExaminationService examinationService;
	@Autowired
	QuestionsAuthUsersRepository questionsAuthUsersRepository;

	private User user3;
	private Examination examination3;
	private Examination examination5;

	@Before
	public void before() {
		user3 = userService.findOne("user3");
        examination3 = examinationService.findOne(3L);
        examination5 = examinationService.findOne(5L);
	}

	@Test
	public void testFindQuestionsByExamination() {
		List<Question> list3 = questionsAuthUsersRepository.findQuestionsByUserAndExamination(user3, examination3);
		assertThat(list3.size(), is(2));
		List<Question> list5 = questionsAuthUsersRepository.findQuestionsByUserAndExamination(user3, examination5);
		assertThat(list5.size(), is(3));
	}
}

