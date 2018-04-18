package easycbt2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import easycbt2.service.UserService;

@Controller
public class IndexController {

	@Autowired
	UserService userService;
	
	@RequestMapping("/")
	public String root() {
		if(!userService.isExistAdminUser()) {
			return "register_admin";
		} else {
			return "index";
		}
	}

	@PostMapping("/register_admin")
	public String registerAdmin(@RequestParam("admin_password") String adminPassword) {
		userService.registerUser(UserService.ADMIN_USER_NAME, adminPassword, UserService.ROLE_ADMIN);
		return "index";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error() {
		return "error";
	}

}
