package de.gherkineditor.controller;

import de.gherkineditor.facade.ProjectFacade;
import de.gherkineditor.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class ProjectRestController {

    @Autowired
    ProjectFacade projectFacade;

    @RequestMapping(value = "/rest/api/project/import", method = RequestMethod.POST, produces = "application/json")
    public Project importProject(@RequestParam(name = "file", required = true) MultipartFile file, @RequestParam(name = "projectId", required = true) String projectId) {
        return this.projectFacade.importProject(projectId, file);
    }
}
