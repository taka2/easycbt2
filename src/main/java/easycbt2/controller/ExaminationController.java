package easycbt2.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import easycbt2.model.Examination;
import easycbt2.model.Question;
import easycbt2.model.User;
import easycbt2.service.ExaminationService;
import easycbt2.service.QuestionService;
import easycbt2.service.UserService;

@Controller
public class ExaminationController {

	@Autowired
	UserService userService;
	@Autowired
	ExaminationService examinationService;
	@Autowired
	QuestionService questionService;

    @RequestMapping("/examinations")
    public String examinations(Model model, Principal principal) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	List<Examination> examinations = examinationService.getExaminationsByUser(user);
    	model.addAttribute("examinations", examinations);

    	return "examinations";
    }

    @RequestMapping("/take_examination_list")
    public String takeExaminationList(Model model, Principal principal, @RequestParam("examination_id") Long examinationId) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	Examination examination = examinationService.getExaminationById(examinationId);

    	List<Question> questions = questionService.getQuestionsByUserAndExamination(user, examination);
    	model.addAttribute("questions", questions);

    	return "take_examination_list";
    }

    @RequestMapping("/answer_examination_list")
    public String takeExaminationList(Model model, Principal principal, @RequestParam MultiValueMap<String, String> params) throws IOException {
    	// TODO
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	for(Map.Entry<String,List<String>> entry : params.entrySet()) {
    		System.out.println(entry);
    	}

    	return "redirect:/examinations";
    }
}
