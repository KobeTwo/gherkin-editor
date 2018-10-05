package de.gherkineditor.validator;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import de.gherkineditor.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.util.Optional;

public abstract class FolderBaseValidator extends AbstractPathItemValidator {

    public static final String SLASH = "/";
    @Autowired
    FolderRepository folderRepository;

    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        Folder folder = (Folder) obj;

        if (isInputStringEmpty(folder.getFileName())) {
            errors.rejectValue("fileName", "name.empty");
        } else {
            if (isFileNamePatternInvalid(folder.getFileName())) {
                errors.rejectValue("fileName", "name.pattern.error");
            }
        }

        if (!isPathExisting(folder)) {
            errors.rejectValue("path", "notexisting");
        }
    }

    protected boolean isFileNamePatternInvalid(String name) {
        return !name.matches("^[a-zA-Z0-9-_]+$");
    }

    protected boolean isPathExisting(Folder folder) {
        if (SLASH.equals(folder.getPath())) {
            return true;
        }
        String firstPath = Util.getSplittedPath(folder.getPath());
        String fileName = Util.getSplittedFileName(folder.getPath());
        Optional<Folder> folderOptional = this.folderRepository.findByPathAndName(folder.getProjectId(), firstPath, fileName);

        if (folderOptional.isPresent()) {
            return true;
        }
        return false;
    }

}
