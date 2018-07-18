package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.QuestionCategory;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
	public List<QuestionCategory> findByEnabledOrderByIdAsc(Boolean enabled);
	public QuestionCategory findByIdAndEnabled(Long id, Boolean enabled);
	public QuestionCategory findByNameAndEnabled(String name, Boolean enabled);
	
	public QuestionCategory findByName(String name);

}