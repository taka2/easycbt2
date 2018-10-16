package easycbt2.controller;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.form.TakeExaminationForm;
import easycbt2.model.Examination;
import easycbt2.model.FillExtractionCondition;
import easycbt2.model.Question;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.service.DateTimeService;
import easycbt2.service.ExaminationService;
import easycbt2.service.FillExtractionConditionService;
import easycbt2.service.QuestionService;
import easycbt2.service.TakeExaminationService;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/examinations")
public class ExaminationController {

	@Autowired
	UserService userService;
	@Autowired
	ExaminationService examinationService;
	@Autowired
	QuestionService questionService;
	@Autowired
	TakeExaminationService takeExaminationService;
	@Autowired
	DateTimeService dateTimeService;
	@Autowired
	FillExtractionConditionService fillExtractionConditionService;

    @GetMapping
    public String index(Model model) {
    	User user = userService.getLoginUser();

    	List<Examination> examinations = examinationService.findByUser(user);
    	model.addAttribute("examinations", examinations);

    	return "examinations/index";
    }

    @GetMapping("{id}/take_examination_list_random")
    public String takeExaminationListRandom(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();
    	
    	TakeExaminationForm form = new TakeExaminationForm();
    	
    	Examination examination = examinationService.findByIdAndUser(id, user);
    	if(examination == null) {
    		// security check
    		return "examinations/index";
    	}
    	form.setExamination(examinationService.findOne(id));

    	List<Question> questions = questionService.findByUserAndExaminationWithRandomize(user, examination);
    	form.setQuestions(questions);

    	form.setStartDateTime(dateTimeService.getCurrentDateTime());

    	model.addAttribute("form", form);
    	return "examinations/take_examination_list";
    }

    @GetMapping("{id}/take_examination_list_fill")
    public String takeExaminationListFill(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();
    	
    	TakeExaminationForm form = new TakeExaminationForm();
    	
    	Examination examination = examinationService.findByIdAndUser(id, user);
    	if(examination == null) {
    		// security check
    		return "examinations/index";
    	}
    	form.setExamination(examinationService.findOne(id));

    	FillExtractionCondition fillExtractionCondition = fillExtractionConditionService.findByUser(user);
    	Date extractionDate = null;
    	if(fillExtractionCondition != null) {
    		extractionDate = fillExtractionCondition.getExtractionDate();
    	}

    	List<Question> questions = questionService.findByUserAndExaminationWithRandomize(user, examination, false, extractionDate);
    	if(questions.size() == 0) {
    		return "redirect:/examinations";
    	}
    	form.setQuestions(questions);

    	form.setStartDateTime(dateTimeService.getCurrentDateTime());

    	model.addAttribute("form", form);
    	return "examinations/take_examination_list";
    }

    @GetMapping("{id}/retake_examination_list_random_only_incorrect_answer")
    public String retakeExaminationListOnlyWrongAnswer(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();

    	TakeExaminationForm form = new TakeExaminationForm();

    	TakeExamination takeExamination = takeExaminationService.findByIdAndUser(id, user);
    	Examination examination = takeExamination.getExamination();
    	form.setExamination(examination);

    	List<Question> questions = takeExamination.getIncorrectAnsweredQuestions();
    	form.setQuestions(questions);
    	
    	form.setStartDateTime(dateTimeService.getCurrentDateTime());

    	model.addAttribute("form", form);
    	return "examinations/take_examination_list";
    }

    @PostMapping("{id}/answer_examination_list")
    public String takeExaminationList(@PathVariable Long id, @ModelAttribute("form") TakeExaminationForm form) {
    	User user = userService.getLoginUser();

    	// Get Information from Request
    	Examination examination = form.getExamination();
    	if(!examination.getId().equals(id)) {
    		// invalid examination
    		return "examinations/take_examination_list";
    	}

    	// End DateTime
    	Instant endDateTime = dateTimeService.getCurrentDateTime();

    	TakeExamination takeExamination = takeExaminationService.save(user, examination, form.getQuestions(), form.getAnswers(), form.getStartDateTime(), endDateTime);
    	return "redirect:/results/" + takeExamination.getId();
    }
}
