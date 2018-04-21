package easycbt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.QuestionCategory;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
	public QuestionCategory findByName(String name);
}