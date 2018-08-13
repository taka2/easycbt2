package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.QuestionsAuthUsers;
import easycbt2.model.User;

public interface QuestionsAuthUsersRepository extends JpaRepository<QuestionsAuthUsers, Long> {
	public List<QuestionsAuthUsers> findByUser(User user);

	@Query("select distinct q from QuestionsAuthUsers qau join qau.question q where q.enabled = true AND qau.user = ?1")
	public List<Question> findQuestionsByUser(User user);

	@Query("select distinct q from QuestionsAuthUsers qau join qau.question q join ExaminationsCategories ec on ec.questionCategory = q.questionCategory where q.enabled = true and qau.user = ?1 and ec.examination = ?2")
	public List<Question> findQuestionsByUserAndExamination(User user, Examination examination);
}