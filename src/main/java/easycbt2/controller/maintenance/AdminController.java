package easycbt2.controller.maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import easycbt2.service.UserService;

@Controller
public class AdminController {

	@Autowired
	UserService userService;

	@PostMapping("/maintenance/admin/register_admin")
	public String registerAdmin(@RequestParam("admin_password") String adminPassword) {
		userService.registerUser(UserService.ADMIN_USER_NAME, adminPassword, UserService.ROLE_ADMIN);
		return "index";
	}

}
