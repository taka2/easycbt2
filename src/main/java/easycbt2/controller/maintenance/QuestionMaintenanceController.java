package easycbt2.controller.maintenance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import easycbt2.model.Question;
import easycbt2.model.QuestionAnswer;
import easycbt2.model.QuestionCategory;
import easycbt2.model.QuestionType;
import easycbt2.model.QuestionsAuthUsers;
import easycbt2.model.User;
import easycbt2.service.QuestionAnswerService;
import easycbt2.service.QuestionCategoryService;
import easycbt2.service.QuestionService;
import easycbt2.service.QuestionsAuthUsersService;
import easycbt2.service.UserService;

@Controller
@RequestMapping("/maintenance/questions")
public class QuestionMaintenanceController {
	
	@Autowired
	UserService userService;
	@Autowired
	QuestionCategoryService questionCategoryService;
	@Autowired
	QuestionService questionService;
	@Autowired
	QuestionAnswerService questionAnswerService;
	@Autowired
	QuestionsAuthUsersService questionsAuthUsersService;

	@GetMapping("/upload_json")
	public String showUploadJSON() {
		return "maintenance/questions/upload_json";
	}
	
	@PostMapping("/upload_json")
	@Transactional
	public String uploadJSON(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findOne(username);

		String str = new String(file.getBytes(), "UTF-8");
		JsonParser jsonParser = JsonParserFactory.getJsonParser();
		List<Object> objList = jsonParser.parseList(str);

		for(Object obj : objList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)obj;
			Question question = new Question();
			question.setQuestionType(QuestionType.valueOf((String)map.get("questionType")));
			question.setText((String)map.get("text"));
			question.setQuestionCategory(questionCategoryService.findByName((String)map.get("questionCategory")));
			question.setEnabled(true);
			questionService.save(question);
			
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> answerList = (List<Map<String, Object>>)map.get("answers");
			for(Map<String, Object> answerMap : answerList) {
				QuestionAnswer questionAnswer = new QuestionAnswer();
				questionAnswer.setQuestion(question);
				questionAnswer.setText((String)answerMap.get("text"));
				questionAnswer.setIsCorrect((Boolean)answerMap.get("isCorrect"));
				questionAnswer.setEnabled(true);
				questionAnswerService.save(questionAnswer);
			}
			
			QuestionsAuthUsers questionsAuthUsers = new QuestionsAuthUsers();
			questionsAuthUsers.setQuestion(question);
			questionsAuthUsers.setUser(user);
			questionsAuthUsersService.save(questionsAuthUsers);
		}
		return "redirect:/examinations";
	}

    @GetMapping
    public String index(Model model) {
        List<Question> questions = questionService.findAll();
        model.addAttribute("questions", questions); 
        return "maintenance/questions/index";
    }

    @GetMapping("new_choice")
    public String newChoiceQuestion(Model model) {
    	return newQuestion(model, "maintenance/questions/new_choice");
    }

    @GetMapping("new_text")
    public String newTextQuestion(Model model) {
        return newQuestion(model, "maintenance/questions/new_text");
    }
    
    public String newQuestion(Model model, String forwardURL) {
    	List<QuestionCategory> questionCategories = questionCategoryService.findAll();
    	model.addAttribute("questionCategories", questionCategories);
    	
    	return forwardURL;
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	Question question = questionService.findOne(id);
        model.addAttribute("question", question);

        List<QuestionCategory> questionCategories = questionCategoryService.findAll();
    	model.addAttribute("questionCategories", questionCategories);

    	switch(question.getQuestionType()) {
    	case TEXT:
    		return "maintenance/questions/edit_text";
    	case SINGLE_CHOICE:
    	case MULTIPLE_CHOICE:
    		return "maintenance/questions/edit_choice";
    	default:
    		throw new RuntimeException("Unsupported QuestionType: " + question.getQuestionType());
    	}
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model) {
    	Question question = questionService.findOne(id);
        model.addAttribute("question", question);
        return "maintenance/questions/show";
    }

    @PostMapping
    public String create(@ModelAttribute Question question, @RequestParam MultiValueMap<String, String> params) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userService.findOne(username);

    	question.setEnabled(true);
    	Question newQuestion = questionService.save(question);

    	// Save Answers
    	switch(question.getQuestionType()) {
    	case TEXT:
    		saveTextAnswers(question, params);
        	break;
    	case SINGLE_CHOICE:
    	case MULTIPLE_CHOICE:
    		saveChoiceAnswers(question, params);
    		break;
    	default:
    		throw new RuntimeException("Unsupported QuestionType: " + question.getQuestionType());
    	}

    	// make question private
    	QuestionsAuthUsers questionsAuthUsers = new QuestionsAuthUsers();
    	questionsAuthUsers.setQuestion(newQuestion);
    	questionsAuthUsers.setUser(user);
    	questionsAuthUsersService.save(questionsAuthUsers);

        return "redirect:/maintenance/questions";
    }

    @PutMapping("{id}")
    public String update(@PathVariable Long id, @ModelAttribute Question question, @RequestParam MultiValueMap<String, String> params) {
    	question.setId(id);
    	question.setEnabled(true);
        Question newQuestion = questionService.save(question);

    	// Delete Answers
    	for(QuestionAnswer questionAnswer : questionAnswerService.findByQuestion(newQuestion)) {
    		questionAnswerService.delete(questionAnswer.getId());
    	}

    	// Save Answers
    	switch(question.getQuestionType()) {
    	case TEXT:
    		saveTextAnswers(question, params);
        	break;
    	case SINGLE_CHOICE:
    	case MULTIPLE_CHOICE:
    		saveChoiceAnswers(question, params);
    		break;
    	default:
    		throw new RuntimeException("Unsupported QuestionType: " + question.getQuestionType());
    	}

        return "redirect:/maintenance/questions";
    }

    private void saveTextAnswers(Question question, MultiValueMap<String, String> params) {
    	for(String answerText : params.get("answerText")) {
    		if(answerText.trim().length() == 0) {
    			continue;
    		}
    		QuestionAnswer questionAnswer = new QuestionAnswer();
    		questionAnswer.setQuestion(question);
    		questionAnswer.setText(answerText);
    		questionAnswer.setIsCorrect(true);
    		questionAnswer.setEnabled(true);
    		questionAnswerService.save(questionAnswer);
    	}
    }
    
    private void saveChoiceAnswers(Question question, MultiValueMap<String, String> params) {
		// TODO LOOP COUNT
		for(int i=0; i<20; i++) {
			List<String> answerTextList = params.get("answerText" + i);
			if(answerTextList == null || answerTextList.size() == 0) {
				continue;
			}
			String answerText = answerTextList.get(0);
    		if(answerText.trim().length() == 0) {
    			continue;
    		}
    		Boolean answerIsCorrect = false;
    		List<String> answerIsCorrectList = params.get("answerIsCorrect" + i);
    		if(answerIsCorrectList != null && answerIsCorrectList.size() != 0) { 
    			answerIsCorrect = answerIsCorrectList.get(0).equals("on");
    		}
    		
    		QuestionAnswer questionAnswer = new QuestionAnswer();
    		questionAnswer.setQuestion(question);
    		questionAnswer.setText(answerText);
    		questionAnswer.setIsCorrect(answerIsCorrect);
    		questionAnswer.setEnabled(true);
    		questionAnswerService.save(questionAnswer);
		}
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable Long id) {
    	questionService.delete(id);
        return "redirect:/maintenance/questions";
    }
}
