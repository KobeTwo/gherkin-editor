package de.gherkineditor.service;

import de.gherkineditor.model.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project);
    Iterable<Project> getAllProjects();
    Project getProject(String id);
}
