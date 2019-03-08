package easycbt2.controller.maintenance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import easycbt2.exception.ApplicationSecurityException;
import easycbt2.model.Question;
import easycbt2.model.User;
import easycbt2.service.QuestionService;
import easycbt2.service.UserService;

@RestController
@RequestMapping("/api/maintenance/questions")
public class QuestionMaintenanceRestController {
	
	@Autowired
	UserService userService;
	@Autowired
	QuestionService questionService;

    @GetMapping
    public Map<String, Object> index(Model model, Pageable pageable) {
    	User user = userService.getLoginUser();
    	
    	// Tabulator用にpageNumber - 1の調整
    	Pageable myPageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
    	Page<Question> questions = questionService.findByUser(user, myPageable);
    	
    	Map<String, Object> resultMap = new HashMap<>();
    	resultMap.put("data", questions.getContent());
    	resultMap.put("last_page", questions.getTotalPages());
    	
    	return resultMap;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
    	User user = userService.getLoginUser();
    	// security check
    	getQuestionForWrite(id, user);

    	questionService.delete(id);
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

    private Question getQuestionForWrite(Long id, User user) throws ApplicationSecurityException {
    	return getQuestion(id, user, true);
    }
}
