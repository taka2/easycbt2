package easycbt2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Examination;
import easycbt2.model.ExaminationsCategories;

public interface ExaminationsCategoriesRepository extends JpaRepository<ExaminationsCategories, Long> {
	public List<ExaminationsCategories> findByEnabled(Boolean enabled);
	public ExaminationsCategories findByIdAndEnabled(Long id, Boolean enaled);
	public List<ExaminationsCategories> findByExaminationAndEnabled(Examination examination, Boolean enabled);
}