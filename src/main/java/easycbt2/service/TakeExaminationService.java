package easycbt2.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.repository.TakeExaminationRepository;
import easycbt2.repository.TakeExaminationsAnswerRepository;
import easycbt2.repository.TakeExaminationsQuestionRepository;

@Service
public class TakeExaminationService {
	@Autowired
	TakeExaminationRepository takeExaminationRepository;
	@Autowired
	TakeExaminationsQuestionRepository takeExaminationsQuestionRepository;
	@Autowired
	TakeExaminationsAnswerRepository takeExaminationsAnswerRepository;
	@Autowired
	ExaminationService examinationService;
	@Autowired
	QuestionService questionService;

	private static final String EXAMINATION_ID_PARAM_NAME = "examination_id";

	public TakeExamination save(User user, MultiValueMap<String, String> params) {
		TakeExamination takeExamination = new TakeExamination();

		Examination examination = examinationService.getExaminationById(Long.parseLong(params.getFirst(EXAMINATION_ID_PARAM_NAME)));
		for(Map.Entry<String,List<String>> entry : params.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();

			if(EXAMINATION_ID_PARAM_NAME.equals(key)) { 
				continue;
			}

    		// Questions
    		System.out.println(entry);
    		Question question = questionService.getQuestionById(Long.parseLong(key));
    		System.out.println(examination);
    		System.out.println(question);
    	}

		return takeExamination;
	}
}
