package de.gherkineditor.validator;

import de.gherkineditor.model.Project;
import de.gherkineditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("beforeSaveProjectValidator")
public class ProjectUpdateValidator extends ProjectBaseValidator {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
    }

}
