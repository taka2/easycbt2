package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Examination;
import easycbt2.model.ExaminationsCategories;

public interface ExaminationsCategoriesRepository extends JpaRepository<ExaminationsCategories, Long> {
	public List<ExaminationsCategories> findByExamination(Examination examination);
}