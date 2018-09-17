package de.gherkineditor.controller;

import de.gherkineditor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "de.gherkineditor.controller")
public class ProjectControllerAdvice {

    @Autowired
    ProjectService projectService;

    @ModelAttribute
    public void addProjects(Model model){
        model.addAttribute("allProjects", projectService.getAllProjects());
    }
}
