package easycbt2.controller.maintenance;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.form.ChangePasswordForm;
import easycbt2.model.FillExtractionCondition;
import easycbt2.model.User;
import easycbt2.service.FillExtractionConditionService;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/maintenance/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FillExtractionConditionService fillExtractionConditionService;

    @GetMapping
    public String index(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users); 
        return "maintenance/users/index";
    }

    @GetMapping("new")
    public String newUser(Model model) {
    	model.addAttribute("user", new User());
        return "maintenance/users/new";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable String id, Model model) {
    	User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "maintenance/users/edit";
    }

    @GetMapping("{id}")
    public String show(@PathVariable String id, Model model) {
    	User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "maintenance/users/show";
    }

    @PostMapping
    public String create(@Validated @ModelAttribute User user, BindingResult result) {
    	if(result.hasErrors()) {
    		return "maintenance/users/new"; 
    	}
    	if(userService.isExistsUser(user.getUsername())) {
    		result.rejectValue("username", "username.duplicate", new Object[] {user.getUsername()}, "");
    		return "maintenance/users/new";
    	}

    	user.setEnabled(true);
    	userService.save(user);
        return "redirect:/maintenance/users";
    }

    @PutMapping("{id}")
    public String update(@PathVariable String id, @Validated @ModelAttribute User user, BindingResult result) {
    	user.setUsername(id);
    	user.setEnabled(true);

    	if(result.hasErrors()) {
    		return "maintenance/users/edit"; 
    	}

        userService.save(user);
        return "redirect:/maintenance/users";
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable String id) {
    	userService.delete(id);
        return "redirect:/maintenance/users";
    }

    @GetMapping("{id}/change_password")
    public String showChangePassword(@PathVariable String id, Model model, @ModelAttribute("form") ChangePasswordForm form) {
    	User user = userService.getLoginUser();
        form.setUser(user);
        return "maintenance/users/change_password";
    }

    @PutMapping("{id}/change_password")
    public String changePassword(@PathVariable String id, Model model, @Validated @ModelAttribute("form") ChangePasswordForm form, BindingResult result) {
    	// Get Login User
    	User user = userService.getLoginUser();
    	form.setUser(user);

    	if(result.hasErrors()) {    		
    		return "maintenance/users/change_password";
    	}
    	
    	// Same id?
    	if(!userService.hasSameId(user, id)) {
    		return "redirect:/home";
    	}
    	
    	// Same current password?
    	if(!userService.hasSameCurrentPassword(user, form.getCurrentPassword())) {
    		result.rejectValue("currentPassword", "current_password.notmatch");
    		return "maintenance/users/change_password";
    	}
    	
    	// Change password
    	userService.changePassword(user, form.getNewPassword());
    	
        return "redirect:/home";
    }

    @GetMapping("{id}/fill_extraction_status")
    public String showFillExtractionStatus(@PathVariable String id, Model model) {
    	User user = userService.getLoginUser();
    	FillExtractionCondition fillExtractionCondition = fillExtractionConditionService.findByUser(user);
        
    	model.addAttribute("user", user);
    	model.addAttribute("fillExtractionCondition", fillExtractionCondition);

        return "maintenance/users/fill_extraction_status";
    }

    @PutMapping("{id}/fill_extraction_status")
    public String updateFillExtractionStatus(@PathVariable String id, Model model) {
    	// Get Login User
    	User user = userService.getLoginUser();
    	
    	FillExtractionCondition fillExtractionCondition = fillExtractionConditionService.findByUser(user);
    	if(fillExtractionCondition == null) {
    		fillExtractionCondition = new FillExtractionCondition();
    	}
    	fillExtractionCondition.setUser(user);
    	//fillExtractionCondition.setExtractionDate(Date.from(dateTimeService.getCurrentDateTime()));
    	fillExtractionCondition.setExtractionDate(new Date());
    	fillExtractionConditionService.save(fillExtractionCondition);
    	
        return "redirect:/maintenance/users/" + user.getUsername() + "/fill_extraction_status";
    }

}
