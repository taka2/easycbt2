package easycbt2.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExaminationController {

    @RequestMapping("/examinations")
    public String examinations(Model model, Principal principal) throws IOException {
    	return "examinations";
    }

}
