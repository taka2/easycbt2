package easycbt2.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.model.TakeExamination;
//import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.service.TakeExaminationService;
//import easycbt2.service.TakeExaminationService;
import easycbt2.service.UserService;

@Controller
public class ResultController {

	@Autowired
	UserService userService;
	@Autowired
	TakeExaminationService takeExaminationService;

    @RequestMapping("/results")
    public String results(Model model, Principal principal) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	List<TakeExamination> takeExaminations = takeExaminationService.findTakeExaminationsByUserOrderByIdDesc(user);
    	model.addAttribute("takeExaminations", takeExaminations);

    	return "results";
    }
}
