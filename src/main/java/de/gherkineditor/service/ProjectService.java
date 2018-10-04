package de.gherkineditor.service;

import de.gherkineditor.model.Project;

public interface ProjectService extends ModelService<Project> {
    Iterable<Project> getAllProjects();

    Project getProject(String id);
}
