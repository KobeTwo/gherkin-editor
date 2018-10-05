package de.gherkineditor.validator;

import de.gherkineditor.model.AbstractProjectItem;
import de.gherkineditor.model.Project;
import de.gherkineditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.util.Optional;

public abstract class AbstractProjectItemValidator extends AbstractValidator {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public void validate(Object obj, Errors errors) {
        AbstractProjectItem projectItem = (AbstractProjectItem) obj;

        if (isInputStringEmpty(projectItem.getProjectId())) {
            errors.rejectValue("projectId", "projectId.empty");
        }

        if (!isProjectExisting(projectItem)) {
            errors.rejectValue("projectId", "notexisting");
        }
    }

    protected boolean isProjectExisting(AbstractProjectItem projectItem) {

        Optional<Project> project = this.projectRepository.findById(projectItem.getProjectId());

        if (project.isPresent()) {
            return true;
        }
        return false;
    }
}
