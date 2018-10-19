package de.gherkineditor.controlleradvice;

import de.gherkineditor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "de.gherkineditor.controller")
public class ProjectControllerAdvice {

    @Autowired
    ProjectService projectService;

    @Value(value = "${com.auth0.domain}")
    private String domain;
    @Value(value = "${com.auth0.clientId}")
    private String clientId;

    @ModelAttribute
    public void addGeneralData(Model model) {
        model.addAttribute("allProjects", this.projectService.getAllProjects());
        model.addAttribute("authDomain", this.domain);
        model.addAttribute("authClientId", this.clientId);
    }
}
