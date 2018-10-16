package easycbt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.FillExtractionCondition;
import easycbt2.model.User;

public interface FillExtractionConditionRepository extends JpaRepository<FillExtractionCondition, String> {
	public FillExtractionCondition findByUser(User user);
}