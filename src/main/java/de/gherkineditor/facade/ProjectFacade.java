package de.gherkineditor.facade;

import de.gherkineditor.model.Project;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectFacade {
    Project importProject(String projectId, MultipartFile file);
}
