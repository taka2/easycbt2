package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	public List<Question> findByQuestionCategory(QuestionCategory questionCategory);
	public List<Question> findByQuestionCategoryAndEnabled(QuestionCategory questionCategory, Boolean enabled);
	public List<Question> findByEnabledOrderByIdAsc(Boolean enabled);
	public Question findByIdAndEnabled(Long id, Boolean enabled);
}