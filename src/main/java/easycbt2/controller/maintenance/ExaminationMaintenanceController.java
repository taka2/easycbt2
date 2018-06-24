package easycbt2.controller.maintenance;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import easycbt2.model.Examination;
import easycbt2.model.ExaminationsAuthUsers;
import easycbt2.model.ExaminationsCategories;
import easycbt2.model.QuestionCategory;
import easycbt2.model.User;
import easycbt2.service.ExaminationService;
import easycbt2.service.ExaminationsAuthUsersService;
import easycbt2.service.ExaminationsCategoriesService;
import easycbt2.service.QuestionCategoryService;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/maintenance/examinations")
public class ExaminationMaintenanceController {

	@Autowired
	UserService userService;
    @Autowired
    private ExaminationService examinationService;
    @Autowired
    private ExaminationsCategoriesService examinationsCategoriesService;
    @Autowired
    private ExaminationsAuthUsersService examinationsAuthUsersService;
    @Autowired
    private QuestionCategoryService questionCategoryService;

    @GetMapping
    public String index(Model model) {
        List<Examination> examinations = examinationService.findAll();
        model.addAttribute("examinations", examinations);
        return "maintenance/examinations/index";
    }

    @GetMapping("new")
    public String newExamination(Model model) {
    	List<QuestionCategory> questionCategories = questionCategoryService.findAll();
    	model.addAttribute("questionCategories", questionCategories);
        return "maintenance/examinations/new";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	Examination examination = examinationService.findOne(id);
        model.addAttribute("examination", examination);

    	List<QuestionCategory> questionCategories = questionCategoryService.findAll();
    	model.addAttribute("questionCategories", questionCategories);

    	List<QuestionCategory> activeQuestionCategories = new ArrayList<>();
    	List<ExaminationsCategories> examinationCategoriesList = examinationsCategoriesService.findByExamination(examination);
    	for(QuestionCategory questionCategory : questionCategories) {
	    	for(ExaminationsCategories examinationCategories : examinationCategoriesList) {
	    		if(questionCategory.equals(examinationCategories.getQuestionCategory())) {
	    			activeQuestionCategories.add(questionCategory);
	    			break;
	    		}
	    	}
    	}
    	model.addAttribute("activeQuestionCategories", activeQuestionCategories);
    	
        return "maintenance/examinations/edit";
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model) {
    	Examination examination = examinationService.findOne(id);
    	model.addAttribute("examination", examination);
    	return "maintenance/examinations/show";
    }

    @PostMapping
    public String create(@ModelAttribute Examination examination, Model model, @RequestParam("examinations_categories") List<Long> examinationsCategoriesIdList) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findOne(username);

    	examination.setEnabled(true);
    	Examination newExamination = examinationService.save(examination);
    	
    	for(Long questionCategoryId : examinationsCategoriesIdList) {
    		ExaminationsCategories examinationsCategories = new ExaminationsCategories();
    		examinationsCategories.setExamination(newExamination);
    		examinationsCategories.setQuestionCategory(questionCategoryService.findOne(questionCategoryId));
    		examinationsCategories.setEnabled(true);
    		examinationsCategoriesService.save(examinationsCategories);
    	}

    	// make examination private
    	ExaminationsAuthUsers examinationsAuthUsers = new ExaminationsAuthUsers();
    	examinationsAuthUsers.setExamination(newExamination);
    	examinationsAuthUsers.setUser(user);
    	examinationsAuthUsersService.save(examinationsAuthUsers);
    	
        return "redirect:/maintenance/examinations";
    }

    @PutMapping("{id}")
    public String update(@PathVariable Long id, @ModelAttribute Examination examination, Model model, @RequestParam("examinations_categories") List<Long> examinationsCategoriesIdList) {
    	examination.setId(id);
    	examination.setEnabled(true);
    	Examination newExamination = examinationService.save(examination);

    	// Delete Examinations Categories
    	for(ExaminationsCategories examinationsCategories : examinationsCategoriesService.findByExamination(newExamination)) {
    		examinationsCategoriesService.delete(examinationsCategories.getId());
    	}

    	// Save Examinations Categories
    	for(Long questionCategoryId : examinationsCategoriesIdList) {
    		ExaminationsCategories examinationsCategories = new ExaminationsCategories();
    		examinationsCategories.setExamination(newExamination);
    		examinationsCategories.setQuestionCategory(questionCategoryService.findOne(questionCategoryId));
    		examinationsCategories.setEnabled(true);
    		examinationsCategoriesService.save(examinationsCategories);
    	}

        return "redirect:/maintenance/examinations";
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable Long id) {
    	Examination examination = examinationService.findOne(id);
    	for(ExaminationsCategories examinationsCategories : examination.getCategoryList()) {
    		examinationsCategoriesService.delete(examinationsCategories.getId());
    	}
    	examinationService.delete(id);
    	return "redirect:/maintenance/examinations";
    }
}
