package easycbt2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;

public interface TakeExaminationRepository extends JpaRepository<TakeExamination, Long> {
	public List<TakeExamination> findByUserOrderByIdDesc(User user);
	public List<TakeExamination> findByUser(User user);
	
	public Page<TakeExamination> findByUserOrderByIdDesc(User user, Pageable pageable);

	@Query("select distinct qc from TakeExamination t join t.takeExaminationsQuestions tq join tq.question q join q.questionCategory qc where t.user = ?1")
	public List<QuestionCategory> findQuestionCategoryByUser(User user);
	
	@Query("select distinct q from TakeExamination t join t.takeExaminationsQuestions tq join tq.question q where t.user = ?1 and q.questionCategory = ?2")
	public List<Question> findByUserAndQuestionCateogry(User user, QuestionCategory questionCategory);
}