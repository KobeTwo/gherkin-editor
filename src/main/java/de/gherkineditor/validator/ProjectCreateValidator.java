package de.gherkineditor.validator;

import de.gherkineditor.model.Project;
import de.gherkineditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component("beforeCreateProjectValidator")
public class ProjectCreateValidator extends ProjectBaseValidator {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        Project project = (Project) obj;
        if (!isInputStringEmpty(project.getId())) {
            Optional<Project> existingProject = this.projectRepository.findById(project.getId());
            if (existingProject.isPresent()) {
                errors.reject("object.existing", "Project with id is already existing");
            }
        }
    }

}
