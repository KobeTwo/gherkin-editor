package de.gherkineditor.controller;

import de.gherkineditor.model.Scenario;
import de.gherkineditor.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ScenarioListController {

    @Autowired
    ScenarioService scenarioService;

    @GetMapping("/scenarios")
    public String homepage(Model model) {
        model.addAttribute("scenarios", scenarioService.listScenarios());
        List<String> list1 = new ArrayList<String>();
        list1.add("1");
        list1.add("2");
        model.addAttribute("list1", list1);

        Scenario scenario1 = new Scenario("s1", "t1");
        Scenario scenario2 = new Scenario("s2", "t2");

        List<Scenario> list2 = new ArrayList<>();
        list2.add(scenario1);
        list2.add(scenario2);
        model.addAttribute("list2",list2);

        return "pages/scenarios";
    }

}