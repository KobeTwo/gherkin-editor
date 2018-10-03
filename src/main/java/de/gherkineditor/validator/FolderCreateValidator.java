package de.gherkineditor.validator;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component("beforeSaveFolderValidator")
public class FolderValidator extends AbstractValidator implements Validator {

    @Autowired
    FolderRepository folderRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Folder.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        Folder folder = (Folder) obj;

        if (isInputStringEmpty(folder.getId())) {
            errors.rejectValue("id", "field.empty");
        } else {
            Optional<Folder> existingIdFolder = this.folderRepository.findById(folder.getId());
            if (existingIdFolder.isPresent()) {
                errors.reject("object.existing");
            }
        }

        if (isInputStringEmpty(folder.getProjectId())) {
            errors.rejectValue("projectId", "projectId.empty");
        }

        if (isInputStringEmpty(folder.getPath())) {
            errors.rejectValue("path", "path.empty");

        } else {
            if (isPathPatternInvalid(folder.getPath())) {
                errors.rejectValue("path", "path.pattern.error");
            }
            Optional<Folder> existingPathFolder = this.folderRepository.findByProjectIdAndPath(folder.getProjectId(), folder.getPath());
            if (existingPathFolder.isPresent()) {
                errors.reject("object.existing");
            }
        }

        if (isInputStringEmpty(folder.getName())) {
            errors.rejectValue("name", "name.empty");
        }
    }

}
