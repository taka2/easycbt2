package easycbt2.controller.maintenance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import easycbt2.constants.PublicationScope;
import easycbt2.exception.ApplicationSecurityException;
import easycbt2.model.QuestionCategoriesAuthPublic;
import easycbt2.model.QuestionCategoriesAuthUsers;
import easycbt2.model.QuestionCategory;
import easycbt2.model.User;
import easycbt2.service.QuestionCategoriesAuthPublicService;
import easycbt2.service.QuestionCategoriesAuthUsersService;
import easycbt2.service.QuestionCategoryService;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/maintenance/question_categories")
public class QuestionCategoriesMaintenanceController {

	@Autowired
	UserService userService;
    @Autowired
    private QuestionCategoryService questionCategoryService;
    @Autowired
    private QuestionCategoriesAuthPublicService questionCategoriesAuthPublicService;
    @Autowired
    private QuestionCategoriesAuthUsersService questionCategoriesAuthUsersService;

    @GetMapping
    public String index(Model model) {
    	User user = userService.getLoginUser();

        List<QuestionCategory> questionCategories = questionCategoryService.findByUser(user);
        model.addAttribute("questionCategories", questionCategories); 

        return "maintenance/question_categories/index";
    }

    @GetMapping("new")
    public String newQuestionCategory(Model model) {
    	model.addAttribute("questionCategory", new QuestionCategory());
        return "maintenance/question_categories/new";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();
    	QuestionCategory questionCategory = getQuestionCategoryForRead(id, user);
        model.addAttribute("questionCategory", questionCategory);
        return "maintenance/question_categories/edit";
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();
    	QuestionCategory questionCategory = getQuestionCategoryForRead(id, user);
        model.addAttribute("questionCategory", questionCategory);
        return "maintenance/question_categories/show";
    }

    @PostMapping
    public String create(@Validated @ModelAttribute QuestionCategory questionCategory, BindingResult result, @RequestParam("scope") String scope) {
    	User user = userService.getLoginUser();

    	if(result.hasErrors()) {    		
    		return "maintenance/question_categories/new";
    	}

    	questionCategory.setEnabled(true);
    	QuestionCategory newQuestionCategory = questionCategoryService.save(questionCategory);

    	PublicationScope scopeObj = PublicationScope.valueOf(scope.toUpperCase());
    	switch(scopeObj) {
    	case PUBLIC:
    		// make questionCategory public
        	QuestionCategoriesAuthPublic questionCategoriesAuthPublic = new QuestionCategoriesAuthPublic();
        	questionCategoriesAuthPublic.setQuestionCategory(newQuestionCategory);
        	questionCategoriesAuthPublicService.save(questionCategoriesAuthPublic);
    		break;
    	case PRIVATE:
    	default:
        	// make questionCategory private
        	QuestionCategoriesAuthUsers questionCategoriesAuthUsers = new QuestionCategoriesAuthUsers();
        	questionCategoriesAuthUsers.setQuestionCategory(newQuestionCategory);
        	questionCategoriesAuthUsers.setUser(user);
        	questionCategoriesAuthUsersService.save(questionCategoriesAuthUsers);
    	}
    	
        return "redirect:/maintenance/question_categories";
    }

    @PutMapping("{id}")
    public String update(@PathVariable Long id, @Validated @ModelAttribute QuestionCategory questionCategory, BindingResult result) {
    	User user = userService.getLoginUser();
    	
    	// security check
    	getQuestionCategoryForWrite(id, user);

    	if(result.hasErrors()) {    		
    		return "maintenance/question_categories/edit";
    	}

    	questionCategory.setId(id);
    	questionCategory.setEnabled(true);
        questionCategoryService.save(questionCategory);
        return "redirect:/maintenance/question_categories";
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable Long id) {
    	User user = userService.getLoginUser();
    	// security check
    	getQuestionCategoryForWrite(id, user);

    	questionCategoryService.delete(id);
        return "redirect:/maintenance/question_categories";
    }

    private QuestionCategory getQuestionCategory(Long id, User user, boolean canWriteCheck) throws ApplicationSecurityException {
    	// security check
    	if(canWriteCheck) {
	    	if(!questionCategoryService.canWrite(id, user)) {
	    		throw new ApplicationSecurityException();
	    	}
    	}
    	QuestionCategory questionCategory = questionCategoryService.findByIdAndUser(id, user);
    	if(questionCategory == null) {
    		throw new ApplicationSecurityException();
    	}
    	
    	return questionCategory;
    }

    private QuestionCategory getQuestionCategoryForRead(Long id, User user) throws ApplicationSecurityException {
    	return getQuestionCategory(id, user, false);
    }

    private QuestionCategory getQuestionCategoryForWrite(Long id, User user) throws ApplicationSecurityException {
    	return getQuestionCategory(id, user, true);
    }

    @ExceptionHandler(ApplicationSecurityException.class)
    public String securityExceptionHandler(ApplicationSecurityException e) {
    	return "redirect:/maintenance/question_categories";
    }
}
