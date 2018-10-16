package easycbt2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easycbt2.model.FillExtractionCondition;
import easycbt2.model.User;
import easycbt2.repository.FillExtractionConditionRepository;

@Service
public class FillExtractionConditionService {

	@Autowired
	FillExtractionConditionRepository fillExtractionConditionRepository;

	public FillExtractionCondition findByUser(User user) {
		return fillExtractionConditionRepository.findByUser(user);
	}
	
	public FillExtractionCondition save(FillExtractionCondition fillExtractionCondition) {
		return fillExtractionConditionRepository.save(fillExtractionCondition);
	}
}
