package de.gherkineditor.service;

import de.gherkineditor.model.Project;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface ProjectService extends ModelService<Project> {
    Iterable<Project> getAllProjects();

    Project getProject(String id);

    Project importProject(String projectId, InputStream zipFileInputStream) throws IOException;

    ByteArrayOutputStream exportProject(String projectId);
}
