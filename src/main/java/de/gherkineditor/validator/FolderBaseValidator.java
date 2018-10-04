package de.gherkineditor.validator;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class FolderBaseValidator extends AbstractValidator implements Validator {

    @Autowired
    FolderRepository folderRepository;

    @Override
    public void validate(Object obj, Errors errors) {
        Folder folder = (Folder) obj;


        if (isInputStringEmpty(folder.getProjectId())) {
            errors.rejectValue("projectId", "projectId.empty");
        }

        if (isInputStringEmpty(folder.getPath())) {
            errors.rejectValue("path", "path.empty");

        } else {
            if (isPathPatternInvalid(folder.getPath())) {
                errors.rejectValue("path", "path.pattern.error");
            }
        }

        if (isInputStringEmpty(folder.getName())) {
            errors.rejectValue("name", "name.empty");
        } else {
            if (isNamePatternInvalid(folder.getName())) {
                errors.rejectValue("name", "name.pattern.error");
            }
        }
    }

    protected boolean isNamePatternInvalid(String name) {
        return !name.matches("^[a-zA-Z0-9-_]+$");
    }

}
