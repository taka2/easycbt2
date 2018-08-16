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

import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TakeExaminationsQuestionRepositoryTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	TakeExaminationsQuestionRepository takeExaminationsQuestionRepository;
	
	private User user6;

	@Before
	public void before() {
        user6 = userRepository.findById("user6").get();
	}

	@Test
	public void testFindQuestionCategoryByUser6() {
		List<TakeExaminationsQuestion> list = takeExaminationsQuestionRepository.findLatests(user6);
		assertThat(list.size(), is(2));
		for(TakeExaminationsQuestion anElement : list) {
			if(anElement.getQuestion().getId().equals(1L)) {
				assertThat(anElement.getElapsedTime(), is(400L));
			}
			if(anElement.getQuestion().getId().equals(2L)) {
				assertThat(anElement.getElapsedTime(), is(500L));
			}
		}
	}
}
