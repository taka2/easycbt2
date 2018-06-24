package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Question;
import easycbt2.model.QuestionAnswer;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
	public List<QuestionAnswer> findByQuestion(Question question);
	public List<QuestionAnswer> findByQuestionAndEnabled(Question question, Boolean enabled);
	public QuestionAnswer findByIdAndEnabled(Long id, Boolean enabled);
}