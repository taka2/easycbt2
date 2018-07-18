package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Examination;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
	public List<Examination> findByEnabledOrderByIdAsc(Boolean enabled);
	public Examination findByIdAndEnabled(Long id, Boolean enabled);
}