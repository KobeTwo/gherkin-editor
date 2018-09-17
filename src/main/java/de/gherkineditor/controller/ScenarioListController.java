package de.gherkineditor.controller;

import de.gherkineditor.model.Scenario;
import de.gherkineditor.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ScenarioListController extends AbstractProjectController {

    @Autowired
    ScenarioService scenarioService;

    @GetMapping("/{projectId}/scenarios")
    public String homepage(Model model, @PathVariable String projectId) {
        setCurrentProject(projectId, model);
        model.addAttribute("scenarios", scenarioService.listScenarios(projectId));
        model.addAttribute("allScenarios", scenarioService.listAllScenarios());

        return "pages/scenarios";
    }

}