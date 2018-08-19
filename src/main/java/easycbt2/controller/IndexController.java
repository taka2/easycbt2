package easycbt2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import easycbt2.service.UserService;

@Controller
public class IndexController {

	@Autowired
	UserService userService;

	@RequestMapping("/")
	public String root() {
		if(!userService.isExistAdminUser()) {
			return "/maintenance/admin/register_admin";
		} else {
			return "index";
		}
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error() {
		return "error";
	}

	@GetMapping("/home")
	public String home(HttpSession httpSession) {
		httpSession.setAttribute("user", userService.getLoginUser());
		return "redirect:/examinations";
	}
}
