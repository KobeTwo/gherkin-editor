package de.gherkineditor.service;

import de.gherkineditor.model.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(String id);
    Iterable<Project> getAllProjects();
    Project getProject(String id);
}
