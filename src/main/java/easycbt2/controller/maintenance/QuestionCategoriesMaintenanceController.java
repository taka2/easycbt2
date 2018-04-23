package easycbt2.controller.maintenance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import easycbt2.model.QuestionCategory;
import easycbt2.service.QuestionCategoryService;

@Controller
@RequestMapping("/maintenance/question_categories")
public class QuestionCategoriesMaintenanceController {
    @Autowired
    private QuestionCategoryService questionCategoryService;

    @GetMapping
    public String index(Model model) {
        List<QuestionCategory> questionCategories = questionCategoryService.findAll();
        model.addAttribute("questionCategories", questionCategories); 
        return "maintenance/question_categories/index";
    }

    @GetMapping("new")
    public String newQuestionCategory(Model model) {
        return "maintenance/question_categories/new";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	QuestionCategory questionCategory = questionCategoryService.findOne(id);
        model.addAttribute("questionCategory", questionCategory);
        return "maintenance/question_categories/edit";
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model) {
    	QuestionCategory questionCategory = questionCategoryService.findOne(id);
        model.addAttribute("questionCategory", questionCategory);
        return "maintenance/question_categories/show";
    }

    @PostMapping
    public String create(@ModelAttribute QuestionCategory questionCategory) {
    	questionCategoryService.save(questionCategory);
        return "redirect:/maintenance/question_categories";
    }

    @PutMapping("{id}")
    public String update(@PathVariable Long id, @ModelAttribute QuestionCategory questionCategory) {
    	questionCategory.setId(id);
        questionCategoryService.save(questionCategory);
        return "redirect:/maintenance/question_categories";
    }

    @DeleteMapping("{id}")
    public String destroy(@PathVariable Long id) {
    	questionCategoryService.delete(id);
        return "redirect:/maintenance/question_categories";
    }
}
