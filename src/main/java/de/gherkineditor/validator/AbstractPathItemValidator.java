package de.gherkineditor.validator;

import de.gherkineditor.model.AbstractPathItem;
import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.FolderRepository;
import de.gherkineditor.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.util.Optional;

public abstract class AbstractPathItemValidator extends AbstractProjectItemValidator {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        AbstractPathItem pathItem = (AbstractPathItem) obj;

        if (isInputStringEmpty(pathItem.getPath())) {
            errors.rejectValue("path", "path.empty", "Path must not be empty");

        } else {
            if (isPathPatternInvalid(pathItem.getPath())) {
                errors.rejectValue("path", "path.pattern.error", "Path must match pattern ^/([a-zA-Z0-9-_]+/)*$");
            }
        }
    }

    protected boolean isPathPatternInvalid(String path) {
        return !path.matches("^/([a-zA-Z0-9-_]+/)*$");
    }

    protected boolean isFolderWithPathExisting(AbstractPathItem pathItem) {
        if (SLASH.equals(pathItem.getPath())) {
            return true;
        }
        String firstPath = Util.getSplittedPath(pathItem.getPath());
        String fileName = Util.getSplittedFileName(pathItem.getPath());
        Optional<Folder> folderOptional = this.folderRepository.findByPathAndName(pathItem.getProjectId(), firstPath, fileName);

        if (folderOptional.isPresent()) {
            return true;
        }
        return false;
    }

    protected boolean isFeatureWithPathExisting(AbstractPathItem pathItem) {
        if (SLASH.equals(pathItem.getPath())) {
            return true;
        }
        String firstPath = Util.getSplittedPath(pathItem.getPath());
        String fileName = Util.getSplittedFileName(pathItem.getPath());
        Optional<Feature> featureOptional = this.featureRepository.findByPathAndName(pathItem.getProjectId(), firstPath, fileName);

        if (featureOptional.isPresent()) {
            return true;
        }
        return false;
    }
}
