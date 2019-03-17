package de.gherkineditor.validator;

import de.gherkineditor.model.Feature;
import org.springframework.validation.Errors;

public abstract class FeatureBaseValidator extends AbstractPathItemValidator {

    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        Feature feature = (Feature) obj;

        if (isInputStringEmpty(feature.getFileName())) {
            errors.rejectValue("fileName", "fileName.empty", "File name is empty");
        } else {
            if (isFileNamePatternInvalid(feature.getFileName())) {
                errors.rejectValue("fileName", "fileName.pattern.error", "Name must match format ^[a-zA-Z0-9-_]+\\.feature$");
            }
        }
        if (!isInputStringEmpty(feature.getPath())) {
            if (!isFolderWithPathExisting(feature)) {
                errors.rejectValue("path", "notexisting", "Folder with path is not existing");
            }
        }

        if (isInputStringEmpty(feature.getName())) {
            errors.rejectValue("name", "name.empty", "Name is empty");
        }

        //validate all tags starting with @
        for (String tag : feature.getTags()) {
            if (!tag.startsWith("@")) {
                errors.rejectValue("tags", "tags.pattern.error", "One of the feature tags do not start with @");
            }
        }
    }

    protected boolean isFileNamePatternInvalid(String name) {
        return !name.matches("^[a-zA-Z0-9-_]+\\.feature$");
    }


}
