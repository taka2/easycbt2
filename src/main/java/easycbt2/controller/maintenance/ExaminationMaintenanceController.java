package easycbt2.controller.maintenance;

import java.util.ArrayList;
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

import easycbt2.constants.PublicationScope;
import easycbt2.exception.ApplicationSecurityException;
import easycbt2.form.ExaminationForm;
import easycbt2.model.Examination;
import easycbt2.model.ExaminationsAuthPublic;
import easycbt2.model.ExaminationsAuthUsers;
import easycbt2.model.ExaminationsCategories;
import easycbt2.model.QuestionCategory;
import easycbt2.model.User;
import easycbt2.service.ExaminationService;
import easycbt2.service.ExaminationsAuthPublicService;
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
    private ExaminationsAuthPublicService examinationsAuthPublicService;
    @Autowired
    private ExaminationsAuthUsersService examinationsAuthUsersService;
    @Autowired
    private QuestionCategoryService questionCategoryService;

    @GetMapping
    public String index(Model model) {
    	User user = userService.getLoginUser();

        List<Examination> examinations = examinationService.findByUser(user);
        model.addAttribute("examinations", examinations);

        return "maintenance/examinations/index";
    }

    @GetMapping("new")
    public String newExamination(Model model) {
    	User user = userService.getLoginUser();

    	ExaminationForm form = new ExaminationForm();
    	form.setQuestionCategories(questionCategoryService.findByUser(user));
    	form.setScope("private");
    	model.addAttribute("form", form);
        return "maintenance/examinations/new";
    }

    @PostMapping
    public String create(@Validated @ModelAttribute("form") ExaminationForm form, BindingResult result, Model model) {
    	User user = userService.getLoginUser();

    	if(result.hasErrors()) {
        	form.setQuestionCategories(questionCategoryService.findByUser(user));
    		return "maintenance/examinations/new";
    	}

    	Examination examination = new Examination();
    	examination.setText(form.getText());
    	examination.setQuestionCount(form.getQuestionCount());
    	examination.setEnabled(true);
    	Examination newExamination = examinationService.save(examination);
    	
    	for(QuestionCategory questionCategory : form.getSelectedQuestionCategories()) {
    		ExaminationsCategories examinationsCategories = new ExaminationsCategories();
    		examinationsCategories.setExamination(newExamination);
    		examinationsCategories.setQuestionCategory(questionCategory);
    		examinationsCategories.setEnabled(true);
    		examinationsCategoriesService.save(examinationsCategories);
    	}

    	PublicationScope scopeObj = PublicationScope.valueOf(form.getScope().toUpperCase());
    	switch(scopeObj) {
    	case PUBLIC:
    		// make examination public
        	ExaminationsAuthPublic examinationsAuthPublic = new ExaminationsAuthPublic();
        	examinationsAuthPublic.setExamination(newExamination);
        	examinationsAuthPublicService.save(examinationsAuthPublic);
    		break;
    	case PRIVATE:
    	default:
        	// make examination private
        	ExaminationsAuthUsers examinationsAuthUsers = new ExaminationsAuthUsers();
        	examinationsAuthUsers.setExamination(newExamination);
        	examinationsAuthUsers.setUser(user);
        	examinationsAuthUsersService.save(examinationsAuthUsers);
    	}
    	
        return "redirect:/maintenance/examinations";
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();
    	Examination examination = getExaminationForRead(id, user);
    	model.addAttribute("examination", examination);
    	return "maintenance/examinations/show";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();

    	Examination examination = getExaminationForRead(id, user);

    	List<QuestionCategory> questionCategories = questionCategoryService.findByUser(user);
    	
    	String scope = examinationsAuthPublicService.isPublic(examination) ? "public" : "private";

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

    	ExaminationForm form = new ExaminationForm();
    	form.setId(id);
    	form.setQuestionCategories(questionCategoryService.findByUser(user));
    	form.setText(examination.getText());
    	form.setQuestionCount(examination.getQuestionCount());
    	form.setScope(scope);
    	form.setSelectedQuestionCategories(activeQuestionCategories);
    	model.addAttribute("form", form);
    	
        return "maintenance/examinations/edit";
    }

    @PutMapping("{id}")
    public String update(@PathVariable Long id, @Validated @ModelAttribute("form") ExaminationForm form, BindingResult result, Model model) {
    	User user = userService.getLoginUser();

    	// security check
    	Examination examination = getExaminationForWrite(id, user);

    	if(result.hasErrors()) {
    		form.setQuestionCategories(questionCategoryService.findByUser(user));
    		return "maintenance/examinations/edit";
    	}

    	examination.setId(id);
    	examination.setText(form.getText());
    	examination.setQuestionCount(form.getQuestionCount());
    	examination.setEnabled(true);
    	Examination newExamination = examinationService.save(examination);

    	// Delete Examinations Categories
    	for(ExaminationsCategories examinationsCategories : examinationsCategoriesService.findByExamination(newExamination)) {
    		examinationsCategoriesService.delete(examinationsCategories.getId());
    	}

    	// Save Examinations Categories
    	for(QuestionCategory questionCategory : form.getSelectedQuestionCategories()) {
    		ExaminationsCategories examinationsCategories = new ExaminationsCategories();
    		examinationsCategories.setExamination(newExamination);
    		examinationsCategories.setQuestionCategory(questionCategory);
    		examinationsCategories.setEnabled(true);
    		examinationsCategoriesService.save(examinationsCategories);
    	}

        return "redirect:/maintenance/examinations";
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable Long id) {
    	User user = userService.getLoginUser();
    	Examination examination = getExaminationForWrite(id, user);

    	for(ExaminationsCategories examinationsCategories : examination.getCategoryList()) {
    		examinationsCategoriesService.delete(examinationsCategories.getId());
    	}
    	examinationService.delete(id);
    	return "redirect:/maintenance/examinations";
    }

    private Examination getExamination(Long id, User user, boolean canWriteCheck) throws ApplicationSecurityException {
    	// security check
    	if(canWriteCheck) {
	    	if(!examinationService.canWrite(id, user)) {
	    		throw new ApplicationSecurityException();
	    	}
    	}
    	Examination examination = examinationService.findByIdAndUser(id, user);
    	if(examination == null) {
    		throw new ApplicationSecurityException();
    	}
    	
    	return examination;
    }

    private Examination getExaminationForRead(Long id, User user) throws ApplicationSecurityException {
    	return getExamination(id, user, false);
    }

    private Examination getExaminationForWrite(Long id, User user) throws ApplicationSecurityException {
    	return getExamination(id, user, true);
    }

    @ExceptionHandler(ApplicationSecurityException.class)
    public String securityExceptionHandler(ApplicationSecurityException e) {
    	return "redirect:/maintenance/examinations";
    }
}
