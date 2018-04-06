package easycbt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Examination;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
}