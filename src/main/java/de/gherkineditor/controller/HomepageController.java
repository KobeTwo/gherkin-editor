package de.gherkineditor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String homepage(Model model) {
        return "pages/homepage";
    }

}