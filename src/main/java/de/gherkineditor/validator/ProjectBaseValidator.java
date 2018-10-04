package de.gherkineditor.validator;

import de.gherkineditor.model.Project;
import de.gherkineditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public abstract class ProjectBaseValidator extends AbstractValidator implements Validator {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public void validate(Object obj, Errors errors) {
        Project project = (Project) obj;
        if (isInputStringEmpty(project.getId())) {
            errors.rejectValue("id", "field.empty");
        }
    }
}
