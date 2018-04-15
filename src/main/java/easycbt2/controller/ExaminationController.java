package easycbt2.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.TakeExamination;
//import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.service.DateTimeService;
import easycbt2.service.ExaminationService;
import easycbt2.service.QuestionService;
import easycbt2.service.TakeExaminationService;
//import easycbt2.service.TakeExaminationService;
import easycbt2.service.UserService;

@Controller
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

    @RequestMapping("/examinations")
    public String examinations(Model model, Principal principal) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	List<Examination> examinations = examinationService.getExaminationsByUser(user);
    	model.addAttribute("examinations", examinations);

    	return "examinations";
    }

    @RequestMapping("/take_examination_list")
    public String takeExaminationList(Model model, Principal principal, HttpSession session, @RequestParam("examination_id") Long examinationId) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	Examination examination = examinationService.getExaminationById(examinationId);
    	session.setAttribute("examination", examination);

    	List<Question> questions = questionService.getQuestionsByUserAndExamination(user, examination);
    	session.setAttribute("questions", questions);
    	
    	session.setAttribute("startTime", dateTimeService.getCurrentDateTime());

    	return "take_examination_list";
    }

    @RequestMapping("/answer_examination_list")
    public String takeExaminationList(Model model, Principal principal, HttpSession session, @RequestParam MultiValueMap<String, String> params) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	// Get Information from Session
    	Examination examination = (Examination)session.getAttribute("examination");
    	@SuppressWarnings("unchecked")
		List<Question> questions = (List<Question>)session.getAttribute("questions");
    	Instant startDateTime = (Instant)session.getAttribute("startTime");
    	
    	// End DateTime
    	Instant endDateTime = dateTimeService.getCurrentDateTime();

    	TakeExamination takeExamination = takeExaminationService.save(user, examination, questions, startDateTime, endDateTime, params);
    	model.addAttribute("takeExamination", takeExamination);

    	return "redirect:/examinations";
    }
}
