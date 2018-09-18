package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Examination;
import easycbt2.model.ExaminationsAuthPublic;

public interface ExaminationsAuthPublicRepository extends JpaRepository<ExaminationsAuthPublic, Long> {
	public List<ExaminationsAuthPublic> findByExamination(Examination examination);
}