package easycbt2.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.service.QuestionService;
import easycbt2.service.TakeExaminationService;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/results")
public class ResultController {

	@Autowired
	UserService userService;
	@Autowired
	TakeExaminationService takeExaminationService;
	@Autowired
	QuestionService questionService;

    @GetMapping
    public String index(Model model, Principal principal, Pageable pageable) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findOne(username);
    	
    	Page<TakeExamination> takeExaminations = takeExaminationService.findByUserOrderByIdDescWithPageable(user, pageable);
    	model.addAttribute("page", takeExaminations);
    	model.addAttribute("takeExaminations", takeExaminations.getContent());
    	model.addAttribute("url", "/results/results");

    	return "results/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model, Principal principal) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findOne(username);
    	
    	TakeExamination takeExamination = takeExaminationService.findByIdAndUser(id, user);
    	if(takeExamination != null) {
    		model.addAttribute("takeExamination", takeExamination);
    		return "results/show";
    	} else {
    		return "redirect:/examinations";
    	}
    }

    @GetMapping("category_progress")
    public String summaryByQuestionCategory(Model model, Principal principal) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findOne(username);
    	
    	Map<QuestionCategory, List<Question>> summaryByQuestionCategory = takeExaminationService.summaryByQuestionCategoryByUser(user);
    	model.addAttribute("summaryByQuestionCategory", summaryByQuestionCategory);

    	Map<QuestionCategory, Integer> questionCountByQuestionCategory = new HashMap<>();
    	for(QuestionCategory questionCategory : summaryByQuestionCategory.keySet()) {
    		questionCountByQuestionCategory.put(questionCategory, questionService.findByQuestionCategory(questionCategory).size());
    	}
    	model.addAttribute("questionCountByQuestionCategory", questionCountByQuestionCategory);
    	
    	return "results/category_progress";
    }
}
