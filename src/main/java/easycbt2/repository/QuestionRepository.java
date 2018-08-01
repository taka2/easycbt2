package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	public List<Question> findByEnabledOrderByIdAsc(Boolean enabled);
	public Question findByIdAndEnabled(Long id, Boolean enabled);
}