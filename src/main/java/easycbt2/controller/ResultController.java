package easycbt2.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.service.QuestionService;
import easycbt2.service.TakeExaminationService;
import easycbt2.service.UserService;

@Controller
public class ResultController {

	@Autowired
	UserService userService;
	@Autowired
	TakeExaminationService takeExaminationService;
	@Autowired
	QuestionService questionService;

    @RequestMapping("/results")
    public String results(Model model, Principal principal) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	List<TakeExamination> takeExaminations = takeExaminationService.findTakeExaminationsByUserOrderByIdDesc(user);
    	model.addAttribute("takeExaminations", takeExaminations);

    	return "results";
    }

    @RequestMapping("/results/summaryByQuestionCategory")
    public String summaryByQuestionCategory(Model model, Principal principal) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findByUsername(username);
    	
    	Map<QuestionCategory, List<Question>> summaryByQuestionCategory = takeExaminationService.summaryByQuestionCategoryByUser(user);
    	model.addAttribute("summaryByQuestionCategory", summaryByQuestionCategory);

    	Map<QuestionCategory, Integer> questionCountByQuestionCategory = new HashMap<>();
    	for(QuestionCategory questionCategory : summaryByQuestionCategory.keySet()) {
    		questionCountByQuestionCategory.put(questionCategory, questionService.findByQuestionCategory(questionCategory).size());
    	}
    	model.addAttribute("questionCountByQuestionCategory", questionCountByQuestionCategory);
    	
    	return "summaryByQuestionCategory";
    }
}
