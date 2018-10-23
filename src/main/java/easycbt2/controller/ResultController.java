package easycbt2.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.model.Question;
import easycbt2.model.QuestionCategory;
import easycbt2.model.TakeExamination;
import easycbt2.model.User;
import easycbt2.service.QuestionCategoryService;
import easycbt2.service.QuestionService;
import easycbt2.service.TakeExaminationService;
import easycbt2.service.TakeExaminationService.FillProgressByQuestionCategory;
import easycbt2.service.TakeExaminationsQuestionService;
import easycbt2.service.TakeExaminationsQuestionService.SummaryByQuestion;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/results")
public class ResultController {

	@Autowired
	UserService userService;
	@Autowired
	TakeExaminationService takeExaminationService;
	@Autowired
	TakeExaminationsQuestionService takeExaminationsQuestionService;
	@Autowired
	QuestionService questionService;
	@Autowired
	QuestionCategoryService questionCategoryService;

    @GetMapping
    public String index(Model model, Principal principal, Pageable pageable) throws IOException {
    	User user = userService.getLoginUser();

    	Page<TakeExamination> takeExaminations = takeExaminationService.findByUserOrderByIdDescWithPageable(user, pageable);
    	model.addAttribute("page", takeExaminations);
    	model.addAttribute("takeExaminations", takeExaminations.getContent());
    	model.addAttribute("url", "/results/results");

    	return "results/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model, Principal principal) {
    	User user = userService.getLoginUser();

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
    	User user = userService.getLoginUser();

//    	Map<QuestionCategory, List<Question>> summaryByQuestionCategory = takeExaminationService.summaryByQuestionCategoryByUser(user);
//    	model.addAttribute("summaryByQuestionCategory", summaryByQuestionCategory);
//
//    	Map<QuestionCategory, Integer> questionCountByQuestionCategory = new HashMap<>();
//    	for(QuestionCategory questionCategory : summaryByQuestionCategory.keySet()) {
//    		questionCountByQuestionCategory.put(questionCategory, questionService.findByUserAndQuestionCategory(user, questionCategory).size());
//    	}
//    	model.addAttribute("questionCountByQuestionCategory", questionCountByQuestionCategory);
    	
    	Map<QuestionCategory, FillProgressByQuestionCategory> fillProgress = takeExaminationService.getFillProgressByUser(user);
    	model.addAttribute("fillProgress", fillProgress);

    	return "results/category_progress";
    }
    
    @GetMapping("categories/{id}")
    public String indexByQuestionCategory(@PathVariable Long id, Model model, Principal principal) throws IOException {
    	User user = userService.getLoginUser();

    	QuestionCategory questionCategory = questionCategoryService.findByIdAndUser(id, user);
    	Map<Question, SummaryByQuestion> summaryByQuestions = takeExaminationsQuestionService.summaryByUserAndQuestionCateogry(user, questionCategory);
    	
    	model.addAttribute("summaryByQuestions", summaryByQuestions);
    	
    	Integer totalNumCorrect = 0;
    	Integer totalNumWrong = 0;
    	double totalCorrectPercentage = 0d;
    	for(Map.Entry<Question, SummaryByQuestion> entry : summaryByQuestions.entrySet()) {
    		totalNumCorrect += entry.getValue().getNumCorrect();
    		totalNumWrong += entry.getValue().getNumWrong();
    	}
    	if(totalNumCorrect != 0 && totalNumWrong != 0) {
    		totalCorrectPercentage = totalNumCorrect / (double)(totalNumCorrect + totalNumWrong) * 100;
    	}
    	
    	model.addAttribute("totalNumCorrect", totalNumCorrect);
    	model.addAttribute("totalNumWrong", totalNumWrong);
    	model.addAttribute("totalCorrectPercentage", totalCorrectPercentage);
    	
    	return "results/summary_by_question";
    }
}
