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

import easycbt2.model.QuestionCategory;
import easycbt2.repository.QuestionCategoryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceTest {
	@Autowired
	QuestionCategoryRepository questionCategoryRepository;
	
	@Before
	public void before() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	public void test() {
		QuestionCategory category1 = new QuestionCategory();
		category1.setName("Java");
		questionCategoryRepository.save(category1);
		
		List<QuestionCategory> categories = questionCategoryRepository.findAll();
		assertThat(1, is(categories.size()));
	}
}
