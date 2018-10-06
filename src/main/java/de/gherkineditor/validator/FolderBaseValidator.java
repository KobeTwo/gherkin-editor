package de.gherkineditor.validator;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

public abstract class FolderBaseValidator extends AbstractPathItemValidator {

    public static final String SLASH = "/";
    @Autowired
    FolderRepository folderRepository;

    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        Folder folder = (Folder) obj;

        if (isInputStringEmpty(folder.getFileName())) {
            errors.rejectValue("fileName", "fileName.empty", "Name is empty");
        } else {
            if (isFileNamePatternInvalid(folder.getFileName())) {
                errors.rejectValue("fileName", "fileName.pattern.error", "Name must match format ^[a-zA-Z0-9-_]+$");
            }
        }
        if (!isInputStringEmpty(folder.getPath())) {
            if (!isFolderWithPathExisting(folder)) {
                errors.rejectValue("path", "notexisting", "Folder with path is not existing");
            }
        }
    }

    protected boolean isFileNamePatternInvalid(String name) {
        return !name.matches("^[a-zA-Z0-9-_]+$");
    }

}
