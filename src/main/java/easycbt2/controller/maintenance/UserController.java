package easycbt2.controller.maintenance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.model.User;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/maintenance/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users); 
        return "maintenance/users/index";
    }

    @GetMapping("new")
    public String newUser(Model model) {
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
    public String create(@ModelAttribute User user, BindingResult result) {
    	if(userService.isExistsUser(user.getUsername())) {
    		result.addError(new FieldError("user", "username", "user " + user.getUsername() + " already exists."));
    		return "maintenance/users/new";
    	}
    	user.setEnabled(true);
    	userService.save(user);
        return "redirect:/maintenance/users";
    }

    @PutMapping("{id}")
    public String update(@PathVariable String id, @ModelAttribute User user) {
    	user.setUsername(id);
        userService.save(user);
        return "redirect:/maintenance/users";
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable String id) {
    	userService.delete(id);
        return "redirect:/maintenance/users";
    }
}
