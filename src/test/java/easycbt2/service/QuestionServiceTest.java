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

import easycbt2.model.Question;
import easycbt2.model.QuestionAnswer;
import easycbt2.model.QuestionCategory;
import easycbt2.model.QuestionType;
import easycbt2.repository.QuestionAnswerRepository;
import easycbt2.repository.QuestionCategoryRepository;
import easycbt2.repository.QuestionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceTest {
	@Autowired
	QuestionCategoryRepository questionCategoryRepository;
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	QuestionAnswerRepository questionAnswerRepository;
	
	@Before
	public void before() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	public void testQuestion() {
		QuestionCategory category1 = new QuestionCategory();
		category1.setName("category1");
		questionCategoryRepository.save(category1);
		
		Question question1 = new Question();
		question1.setQuestionType(QuestionType.SINGLE_CHOICE);
		question1.setText("text");
		question1.setQuestionCategory(category1);
		question1.setDefaultText("defaultText");
		question1.setExplanation("explanation");
		questionRepository.save(question1);

		QuestionAnswer answer1 = new QuestionAnswer();
		answer1.setQuestion(question1);
		answer1.setText("answer1");
		answer1.setIsCorrect(false);
		questionAnswerRepository.save(answer1);
		QuestionAnswer answer2 = new QuestionAnswer();
		answer2.setQuestion(question1);
		answer2.setText("answer2");
		answer2.setIsCorrect(true);
		questionAnswerRepository.save(answer2);
		QuestionAnswer answer3 = new QuestionAnswer();
		answer3.setQuestion(question1);
		answer3.setText("answer3");
		answer3.setIsCorrect(false);
		questionAnswerRepository.save(answer3);
		
		List<QuestionCategory> categories = questionCategoryRepository.findAll();
		assertThat(categories.size(), is(1));
		assertThat(categories.get(0).getName(), is("category1"));
		
		List<Question> questions = questionRepository.findAll();
		assertThat(questions.size(), is(1));
		Question question = questions.get(0);
		assertThat(question.getQuestionType(), is(QuestionType.SINGLE_CHOICE));
		assertThat(question.getText(), is("text"));
		assertThat(question.getQuestionCategory().getName(), is("category1"));
		assertThat(question.getDefaultText(), is("defaultText"));
		assertThat(question.getExplanation(), is("explanation"));
		
		List<QuestionAnswer> questionAnswers = question.getQuestionAnswerList();
		assertThat(questionAnswers.size(), is(3));
	}
}
