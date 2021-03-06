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
import easycbt2.service.ExaminationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionsAuthPublicRepositoryTest {
	@Autowired
	ExaminationService examinationService;
	@Autowired
	QuestionsAuthPublicRepository questionsAuthPublicRepository;

	private Examination examination3;
	private Examination examination5;

	@Before
	public void before() {
        examination3 = examinationService.findOne(3L);
        examination5 = examinationService.findOne(5L);
	}

	@Test
	public void testFindQuestionsByExamination() {
		List<Question> list3 = questionsAuthPublicRepository.findQuestionsByExamination(examination3);
		assertThat(list3.size(), is(1));
		List<Question> list5 = questionsAuthPublicRepository.findQuestionsByExamination(examination5);
		assertThat(list5.size(), is(2));
	}
}

