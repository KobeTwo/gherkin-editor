package de.gherkineditor.validator;

import de.gherkineditor.model.AbstractPathItem;
import org.springframework.validation.Errors;

public abstract class AbstractPathItemValidator extends AbstractProjectItemValidator {

    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        AbstractPathItem pathItem = (AbstractPathItem) obj;

        if (isInputStringEmpty(pathItem.getPath())) {
            errors.rejectValue("path", "path.empty");

        } else {
            if (isPathPatternInvalid(pathItem.getPath())) {
                errors.rejectValue("path", "path.pattern.error");
            }
        }
    }

    protected boolean isPathPatternInvalid(String path) {
        return !path.matches("^/([a-zA-Z0-9-_]+/)*$");
    }
}
