package easycbt2.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.User;
import easycbt2.service.TakeExaminationsQuestionService.SummaryByQuestion;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TakeExaminationsQuestionServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	QuestionCategoryService questionCategoryService;
	@Autowired
	QuestionService questionService;
	@Autowired
	TakeExaminationsQuestionService takeExaminationsQuestionService;
	
	private User user6;
	private QuestionCategory questionCategory1;
	private Question question1;
	private Question question3;
	
	@Before
	public void before() {
        user6 = userService.findOne("user6");
        questionCategory1 = questionCategoryService.findOne(1L);
        question1 = questionService.findOne(1L);
        question3 = questionService.findOne(3L);
	}

	@Test
	public void testSummaryByUserAndQuestionCategory() {
		Map<Question, SummaryByQuestion> resultMap = takeExaminationsQuestionService.summaryByUserAndQuestionCateogry(user6, questionCategory1);
		assertThat(resultMap.size(), is(2));
		assertThat(resultMap.get(question1).getNumCorrect(), is(1));
		assertThat(resultMap.get(question1).getNumWrong(), is(1));
		assertThat(resultMap.get(question3).getNumCorrect(), is(1));
		assertThat(resultMap.get(question3).getNumWrong(), is(0));
	}
}
