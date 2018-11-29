package de.gherkineditor.facade;

import de.gherkineditor.model.Project;
import de.gherkineditor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class DefaultProjectFacade implements ProjectFacade {

    @Autowired
    ProjectService projectService;

    @Override
    public Project importProject(String projectId, MultipartFile file) {
        Project project = null;
        try {
            project = this.projectService.importProject(projectId, file.getInputStream());
        } catch (IOException e) {

        }
        return project;
    }

    @Override
    public ByteArrayOutputStream exportProject(String projectId) {

        return this.projectService.exportProject(projectId);
    }


}
