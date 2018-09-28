package de.gherkineditor.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FolderRestController {

    @RequestMapping("/api/{projectId}/folder/structure")
    public FolderStructure greeting(@PathVariable String projectId) {
        return new FolderStructure();
    }

    private class FolderStructure {

    }
}
