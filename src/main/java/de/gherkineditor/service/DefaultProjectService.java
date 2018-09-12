package de.gherkineditor.service;

import de.gherkineditor.model.Project;
import de.gherkineditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

@Service
public class DefaultProjectService implements ProjectService{

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public Project createProject(String id) {
        if(projectRepository.findById(id).isPresent()){
            throw new IllegalArgumentException("The project is already existing");
        }
        Project project = new Project(id);
        return projectRepository.save(project);
    }
}
