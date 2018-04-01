package easycbt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.QuestionAnswer;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
}