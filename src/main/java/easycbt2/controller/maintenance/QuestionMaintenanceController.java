package easycbt2.controller.maintenance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import easycbt2.constants.PublicationScope;
import easycbt2.constants.QuestionType;
import easycbt2.exception.ApplicationSecurityException;
import easycbt2.form.QuestionChoiceForm;
import easycbt2.form.QuestionTextForm;
import easycbt2.model.Question;
import easycbt2.model.QuestionAnswer;
import easycbt2.model.QuestionsAuthPublic;
import easycbt2.model.QuestionsAuthUsers;
import easycbt2.model.User;
import easycbt2.service.QuestionAnswerService;
import easycbt2.service.QuestionCategoryService;
import easycbt2.service.QuestionService;
import easycbt2.service.QuestionsAuthPublicService;
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
	QuestionsAuthPublicService questionsAuthPublicService;
	@Autowired
	QuestionsAuthUsersService questionsAuthUsersService;

	@GetMapping("/upload_json")
	public String showUploadJSON() {
		return "maintenance/questions/upload_json";
	}
	
	@PostMapping("/upload_json")
	@Transactional
	public String uploadJSON(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
    	User user = userService.getLoginUser();

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
			question.setDefaultText((String)map.get("defaultText"));
			question.setExplanation((String)map.get("explanation"));
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
    	User user = userService.getLoginUser();

        List<Question> questions = questionService.findByUser(user);
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
    	User user = userService.getLoginUser();

    	QuestionChoiceForm form = new QuestionChoiceForm();
    	form.setQuestionCategories(questionCategoryService.findByUser(user));
    	model.addAttribute("form", form);
    	
    	return forwardURL;
    }

    @PostMapping("new_choice")
    public String createChoice(@Validated @ModelAttribute("form") QuestionChoiceForm form, BindingResult result) {
    	User user = userService.getLoginUser();

    	if(result.hasErrors()) {
        	form.setQuestionCategories(questionCategoryService.findByUser(user));
    		return "maintenance/questions/new_choice";
    	}

    	// Question Type=SINGLE_CHOICE and multiple correct answers selected
    	if(form.getQuestionType().equals(QuestionType.SINGLE_CHOICE) && form.getCorrectAnswersCount() != 1) {
    		result.rejectValue("questionsAnswers", "questionsAnswers.single_choice_multiple_answer");
    		form.setQuestionCategories(questionCategoryService.findByUser(user));
    		return "maintenance/questions/new_choice";
    	}

    	Question question = new Question();
    	question.setQuestionType(form.getQuestionType());
    	question.setText(form.getText());
    	question.setQuestionCategory(form.getSelectedQuestionCategory());
    	question.setExplanation(form.getExplanation());
    	question.setEnabled(true);
    	Question newQuestion = questionService.save(question);
    	
    	for(QuestionAnswer questionAnswer : form.getQuestionsAnswers()) {
    		questionAnswer.setQuestion(newQuestion);
    		questionAnswer.setEnabled(true);
    		questionAnswerService.save(questionAnswer);
    	}

    	PublicationScope scopeObj = PublicationScope.valueOf(form.getScope().toUpperCase());
    	switch(scopeObj) {
    	case PUBLIC:
    		// make question public
        	QuestionsAuthPublic questionsAuthPublic = new QuestionsAuthPublic();
        	questionsAuthPublic.setQuestion(newQuestion);
        	questionsAuthPublicService.save(questionsAuthPublic);
    		break;
    	case PRIVATE:
    	default:
        	// make question private
        	QuestionsAuthUsers questionsAuthUsers = new QuestionsAuthUsers();
        	questionsAuthUsers.setQuestion(newQuestion);
        	questionsAuthUsers.setUser(user);
        	questionsAuthUsersService.save(questionsAuthUsers);
    	}

        return "redirect:/maintenance/questions";
    }

    @PostMapping("new_text")
    public String createText(@Validated @ModelAttribute("form") QuestionTextForm form, BindingResult result) {
    	User user = userService.getLoginUser();

    	if(result.hasErrors()) {
        	form.setQuestionCategories(questionCategoryService.findByUser(user));
    		return "maintenance/questions/new_text";
    	}

    	Question question = new Question();
    	question.setQuestionType(form.getQuestionType());
    	question.setText(form.getText());
    	question.setQuestionCategory(form.getSelectedQuestionCategory());
    	question.setDefaultText(form.getDefaultText());
    	question.setExplanation(form.getExplanation());
    	question.setEnabled(true);
    	Question newQuestion = questionService.save(question);
    	
    	for(QuestionAnswer questionAnswer : form.getQuestionsAnswers()) {
    		questionAnswer.setQuestion(newQuestion);
    		questionAnswer.setIsCorrect(true);
    		questionAnswer.setEnabled(true);
    		questionAnswerService.save(questionAnswer);
    	}

    	PublicationScope scopeObj = PublicationScope.valueOf(form.getScope().toUpperCase());
    	switch(scopeObj) {
    	case PUBLIC:
    		// make question public
        	QuestionsAuthPublic questionsAuthPublic = new QuestionsAuthPublic();
        	questionsAuthPublic.setQuestion(newQuestion);
        	questionsAuthPublicService.save(questionsAuthPublic);
    		break;
    	case PRIVATE:
    	default:
        	// make question private
        	QuestionsAuthUsers questionsAuthUsers = new QuestionsAuthUsers();
        	questionsAuthUsers.setQuestion(newQuestion);
        	questionsAuthUsers.setUser(user);
        	questionsAuthUsersService.save(questionsAuthUsers);
    	}

        return "redirect:/maintenance/questions";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();

    	Question question = getQuestionForRead(id, user);

    	String scope = questionsAuthPublicService.isPublic(question) ? "public" : "private";

    	switch(question.getQuestionType()) {
    	case TEXT:
        	QuestionTextForm textForm = new QuestionTextForm();
        	textForm.setId(id);
        	textForm.setText(question.getText());
        	textForm.setQuestionType(question.getQuestionType());
        	textForm.setSelectedQuestionCategory(question.getQuestionCategory());
        	textForm.setDefaultText(question.getDefaultText());
        	textForm.setExplanation(question.getExplanation());
        	textForm.setQuestionsAnswers(question.getQuestionAnswerListOrderByIdAsc());
        	textForm.setScope(scope);
        	textForm.setQuestionCategories(questionCategoryService.findByUser(user));
        	model.addAttribute("form", textForm);
    		return "maintenance/questions/edit_text";
    	case SINGLE_CHOICE:
    	case MULTIPLE_CHOICE:
    		QuestionChoiceForm choiceForm = new QuestionChoiceForm();
    		choiceForm.setId(id);
    		choiceForm.setText(question.getText());
    		choiceForm.setQuestionType(question.getQuestionType());
    		choiceForm.setSelectedQuestionCategory(question.getQuestionCategory());
    		choiceForm.setExplanation(question.getExplanation());
    		choiceForm.setQuestionsAnswers(question.getQuestionAnswerListOrderByIdAsc());
    		choiceForm.setScope(scope);
    		choiceForm.setQuestionCategories(questionCategoryService.findByUser(user));
        	model.addAttribute("form", choiceForm);
    		return "maintenance/questions/edit_choice";
    	default:
    		throw new RuntimeException("Unsupported QuestionType: " + question.getQuestionType());
    	}
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model) {
    	User user = userService.getLoginUser();
    	Question question = getQuestionForRead(id, user);
        model.addAttribute("question", question);
        return "maintenance/questions/show";
    }

    @PutMapping("{id}/update_choice")
    public String updateChoice(@PathVariable Long id, @Validated @ModelAttribute("form") QuestionChoiceForm form, BindingResult result) {
    	User user = userService.getLoginUser();

    	// security check
    	Question question = getQuestionForWrite(id, user);

    	if(result.hasErrors()) {
        	form.setQuestionCategories(questionCategoryService.findByUser(user));
    		return "maintenance/questions/edit_choice";
    	}

    	// Question Type=SINGLE_CHOICE and multiple correct answers selected
    	if(form.getQuestionType().equals(QuestionType.SINGLE_CHOICE) && form.getCorrectAnswersCount() != 1) {
    		result.rejectValue("questionsAnswers", "questionsAnswers.single_choice_multiple_answer");
    		return "maintenance/questions/edit_choice";
    	}

    	question.setId(id);
    	question.setQuestionType(form.getQuestionType());
    	question.setText(form.getText());
    	question.setQuestionCategory(form.getSelectedQuestionCategory());
    	question.setExplanation(form.getExplanation());
    	question.setEnabled(true);
        Question newQuestion = questionService.save(question);

    	// Delete Answers
    	for(QuestionAnswer questionAnswer : questionAnswerService.findByQuestion(newQuestion)) {
    		questionAnswerService.delete(questionAnswer.getId());
    	}

    	// Save Answers
    	for(QuestionAnswer questionAnswer : form.getQuestionsAnswers()) {
    		questionAnswer.setQuestion(newQuestion);
    		questionAnswer.setEnabled(true);
    		questionAnswerService.save(questionAnswer);
    	}

        return "redirect:/maintenance/questions";
    }

    @PutMapping("{id}/update_text")
    public String updateText(@PathVariable Long id, @Validated @ModelAttribute("form") QuestionTextForm form, BindingResult result) {
    	User user = userService.getLoginUser();

    	// security check
    	Question question = getQuestionForWrite(id, user);

    	if(result.hasErrors()) {
        	form.setQuestionCategories(questionCategoryService.findByUser(user));
    		return "maintenance/questions/edit_text";
    	}

    	question.setId(id);
    	question.setQuestionType(form.getQuestionType());
    	question.setText(form.getText());
    	question.setQuestionCategory(form.getSelectedQuestionCategory());
    	question.setDefaultText(form.getDefaultText());
    	question.setExplanation(form.getExplanation());
    	question.setEnabled(true);
    	Question newQuestion = questionService.save(question);

    	// Delete Answers
    	for(QuestionAnswer questionAnswer : questionAnswerService.findByQuestion(newQuestion)) {
    		questionAnswerService.delete(questionAnswer.getId());
    	}

    	// Save Answers
    	for(QuestionAnswer questionAnswer : form.getQuestionsAnswers()) {
			questionAnswer.setQuestion(newQuestion);
			questionAnswer.setIsCorrect(true);
			questionAnswer.setEnabled(true);
			questionAnswerService.save(questionAnswer);
    	}

        return "redirect:/maintenance/questions";
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable Long id) {
    	User user = userService.getLoginUser();
    	// security check
    	getQuestionForWrite(id, user);

    	questionService.delete(id);
        return "redirect:/maintenance/questions";
    }
    
    private Question getQuestion(Long id, User user, boolean canWriteCheck) throws ApplicationSecurityException {
    	// security check
    	if(canWriteCheck) {
	    	if(!questionService.canWrite(id, user)) {
	    		throw new ApplicationSecurityException();
	    	}
    	}
    	Question question = questionService.findByIdAndUser(id, user);
    	if(question == null) {
    		throw new ApplicationSecurityException();
    	}
    	
    	return question;
    }

    private Question getQuestionForRead(Long id, User user) throws ApplicationSecurityException {
    	return getQuestion(id, user, false);
    }

    private Question getQuestionForWrite(Long id, User user) throws ApplicationSecurityException {
    	return getQuestion(id, user, true);
    }

    @ExceptionHandler(ApplicationSecurityException.class)
    public String securityExceptionHandler(ApplicationSecurityException e) {
    	return "redirect:/maintenance/questions";
    }
}
