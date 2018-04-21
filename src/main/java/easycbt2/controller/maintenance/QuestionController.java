package easycbt2.controller.maintenance;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/maintenance/questions")
public class QuestionController {

	@GetMapping("/upload_json")
	public String showUploadJSON() {
		return "maintenance/questions/upload_json";
	}
	
	@PostMapping("/upload_json")
	public String uploadJSON(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
		String str = new String(file.getBytes(), "UTF-8");
		JsonParser jsonParser = JsonParserFactory.getJsonParser();
		List<Object> objList = jsonParser.parseList(str);
		for(Object o : objList) {
			System.out.println(o.getClass().getName() + " " + o);
		}
		//jsonParser.parseList(file.)
		return "redirect:/examinations";
	}

}
