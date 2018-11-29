package de.gherkineditor.facade;

import de.gherkineditor.model.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface ProjectFacade {
    Project importProject(String projectId, MultipartFile file);

    ByteArrayOutputStream exportProject(String projectId);
}
