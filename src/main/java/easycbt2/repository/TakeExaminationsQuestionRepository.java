package easycbt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.TakeExaminationsQuestion;

public interface TakeExaminationsQuestionRepository extends JpaRepository<TakeExaminationsQuestion, Long> {
}