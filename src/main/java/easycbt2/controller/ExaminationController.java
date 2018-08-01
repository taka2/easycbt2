package easycbt2.controller;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.service.DateTimeService;
import easycbt2.service.ExaminationService;
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

    @GetMapping
    public String index(Model model, Principal principal) {
    	User user = userService.getLoginUser();

    	List<Examination> examinations = examinationService.findByUser(user);
    	model.addAttribute("examinations", examinations);

    	return "examinations/index";
    }

    @GetMapping("{id}/take_examination_list")
    public String takeExaminationList(@PathVariable Long id, Model model, Principal principal, HttpSession session) {
    	User user = userService.getLoginUser();

    	Examination examination = examinationService.findOne(id);
    	session.setAttribute("examination", examination);

    	List<Question> questions = questionService.findByUserAndExamination(user, examination, true);
    	session.setAttribute("questions", questions);
    	
    	session.setAttribute("startTime", dateTimeService.getCurrentDateTime());

    	return "examinations/take_examination_list";
    }

    @GetMapping("{id}/retake_examination_list_only_incorrect_answer")
    public String retakeExaminationListOnlyWrongAnswer(@PathVariable Long id, Model model, Principal principal, HttpSession session) {
    	User user = userService.getLoginUser();

    	TakeExamination takeExamination = takeExaminationService.findByIdAndUser(id, user);
    	Examination examination = takeExamination.getExamination();
    	session.setAttribute("examination", examination);

    	List<Question> questions = takeExamination.getIncorrectAnsweredQuestions();
    	session.setAttribute("questions", questions);
    	
    	session.setAttribute("startTime", dateTimeService.getCurrentDateTime());

    	return "examinations/take_examination_list";
    }

    @PostMapping("{id}/answer_examination_list")
    public String takeExaminationList(@PathVariable Long id, Model model, Principal principal, HttpSession session, @RequestParam MultiValueMap<String, String> params) {
    	User user = userService.getLoginUser();

    	// Get Information from Session
    	Examination examination = (Examination)session.getAttribute("examination");
    	@SuppressWarnings("unchecked")
		List<Question> questions = (List<Question>)session.getAttribute("questions");
    	Instant startDateTime = (Instant)session.getAttribute("startTime");
    	
    	// End DateTime
    	Instant endDateTime = dateTimeService.getCurrentDateTime();

    	TakeExamination takeExamination = takeExaminationService.save(user, examination, questions, startDateTime, endDateTime, params);
    	return "redirect:/results/" + takeExamination.getId();
    }
}
