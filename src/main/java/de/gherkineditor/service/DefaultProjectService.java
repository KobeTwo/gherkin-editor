package de.gherkineditor.service;

import de.gherkineditor.model.Project;
import de.gherkineditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultProjectService extends AbstractModelService<Project> implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public Iterable<Project> getAllProjects() {
        Iterable<Project> projects = this.projectRepository.findAll();
        return projects;
    }

    @Override
    public Project getProject(String id) {
        Optional<Project> project = this.projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new IllegalArgumentException("Project not found");
        }
        return project.get();
    }

    @Override
    public Project create(Project project) {
        validateCreate(project);
        return this.projectRepository.save(project);
    }

    @Override
    public Project update(Project project) {
        validateUpdate(project);
        return this.projectRepository.save(project);
    }
}
