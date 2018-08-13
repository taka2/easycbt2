package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.QuestionsAuthPublic;

public interface QuestionsAuthPublicRepository extends JpaRepository<QuestionsAuthPublic, Long> {
	@Query("select distinct q from QuestionsAuthPublic qap join qap.question q where q.enabled = true")
	public List<Question> findQuestions();

	@Query("select distinct q from QuestionsAuthPublic qap join qap.question q join ExaminationsCategories ec on ec.questionCategory = q.questionCategory where q.enabled = true and ec.examination = ?1")
	public List<Question> findQuestionsByExamination(Examination examination);
}