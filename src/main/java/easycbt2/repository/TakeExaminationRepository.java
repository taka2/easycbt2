package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.TakeExamination;
import easycbt2.model.User;

public interface TakeExaminationRepository extends JpaRepository<TakeExamination, Long> {
	public List<TakeExamination> findByUserOrderByIdDesc(User user);
}