package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import easycbt2.model.TakeExaminationsQuestion;
import easycbt2.model.User;

public interface TakeExaminationsQuestionRepository extends JpaRepository<TakeExaminationsQuestion, Long> {
	@Query("select max(teq) from TakeExaminationsQuestion teq join teq.takeExamination te where te.user = ?1 group by teq.question")
	public List<TakeExaminationsQuestion> findLatests(User user);
}