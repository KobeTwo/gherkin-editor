package de.gherkineditor.service;

import de.gherkineditor.model.Project;
import de.gherkineditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultProjectService implements ProjectService{

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project) {
        if(StringUtils.isEmpty(project.getId())){
            throw new IllegalArgumentException("The project id must not be null");
        }

        if(projectRepository.findById(project.getId()).isPresent()){
            throw new IllegalArgumentException("The project is already existing");
        }

        return projectRepository.save(project);
    }

    @Override
    public Iterable<Project> getAllProjects() {
        Iterable<Project> projects = projectRepository.findAll();
        return projects;
    }

    @Override
    public Project getProject(String id) {
        Optional<Project> project = projectRepository.findById(id);
        if(!project.isPresent()){
            throw new IllegalArgumentException("Project not found");
        }
        return project.get();
    }
}
