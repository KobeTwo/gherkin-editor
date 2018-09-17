package de.gherkineditor.controller;

import de.gherkineditor.model.Project;
import de.gherkineditor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

public abstract class AbstractProjectController {

   @Autowired
    ProjectService projectService;

    protected void setCurrentProject(String currentProjectId, Model model){
        Project project = projectService.getProject(currentProjectId);
        model.addAttribute("currentProject", project);
    }
}
