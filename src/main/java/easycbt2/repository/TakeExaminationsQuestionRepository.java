package easycbt2.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;

public interface TakeExaminationsQuestionRepository extends JpaRepository<TakeExaminationsQuestion, Long> {
	@Query("select max(teq) from TakeExaminationsQuestion teq join teq.takeExamination te where te.user = ?1 group by teq.question")
	public List<TakeExaminationsQuestion> findLatests(User user);

	@Query("select max(teq) from TakeExaminationsQuestion teq join teq.takeExamination te where te.user = ?1 and te.createdDate >= ?2 group by teq.question")
	public List<TakeExaminationsQuestion> findLatestsByTakeExaminationDate(User user, Date takeExaminationDate);

	@Query("select teq from TakeExaminationsQuestion teq join teq.takeExamination te join teq.question q where te.user = ?1 AND q.questionCategory = ?2")
	public List<TakeExaminationsQuestion> findByUserAndQuestionCategory(User user, QuestionCategory questionCategory);
}