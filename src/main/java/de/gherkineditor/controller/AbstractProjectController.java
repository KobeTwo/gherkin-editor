package de.gherkineditor.controller;

import de.gherkineditor.model.Project;
import de.gherkineditor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

public abstract class AbstractProjectController {

   @Autowired
    ProjectService projectService;

    public static String NAV_SCENARIOS = "scenarios";
    public static String NAV_DOCUMENTATION = "documentation";

    protected void setCurrentProject(String currentProjectId, Model model){
        Project project = projectService.getProject(currentProjectId);
        model.addAttribute("currentProject", project);
    }
    protected void setCurrentNav(String currentNav, Model model){
        model.addAttribute("currentNav", currentNav);
    }
}
