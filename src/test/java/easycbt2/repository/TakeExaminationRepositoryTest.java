package easycbt2.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TakeExaminationRepositoryTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	QuestionCategoryRepository questionCategoryRepository;
	@Autowired
	TakeExaminationRepository takeExaminationRepository;
	
	private User user1;
	private QuestionCategory questionCategory1;

	@Before
	public void before() {
        user1 = userRepository.findById("user1").get();
        questionCategory1 = questionCategoryRepository.findByName("category1");
	}

	@Test
	public void testGetExaminationsByUser1() {
		List<TakeExamination> list = takeExaminationRepository.findByUserAndQuestionCateogry(user1, questionCategory1);
		System.out.println(list);
	}
}
