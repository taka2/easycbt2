package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	public List<Question> findByQuestionCategory(QuestionCategory questionCategory);
}