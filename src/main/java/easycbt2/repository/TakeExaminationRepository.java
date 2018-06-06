package easycbt2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.TakeExamination;
import easycbt2.model.User;

public interface TakeExaminationRepository extends JpaRepository<TakeExamination, Long> {
	public List<TakeExamination> findByUserOrderByIdDesc(User user);
	public List<TakeExamination> findByUser(User user);
	
	public Page<TakeExamination> findByUserOrderByIdDesc(User user, Pageable pageable);
}