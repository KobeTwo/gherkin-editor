package de.gherkineditor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DocumentationController extends AbstractProjectController {

    @GetMapping("/{projectId}/documentation")
    public String homepage(Model model, @PathVariable String projectId) {
        setCurrentProject(projectId, model);

        setCurrentNav(AbstractProjectController.NAV_DOCUMENTATION, model);

        return "pages/documentation";
    }

}