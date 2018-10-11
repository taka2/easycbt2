package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.QuestionCategoriesAuthPublic;
import easycbt2.model.QuestionCategory;

public interface QuestionCategoriesAuthPublicRepository extends JpaRepository<QuestionCategoriesAuthPublic, Long> {
	public List<QuestionCategoriesAuthPublic> findByQuestionCategory(QuestionCategory questionCategory);
}