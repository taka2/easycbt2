package easycbt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.TakeExamination;

public interface TakeExaminationRepository extends JpaRepository<TakeExamination, Long> {
}