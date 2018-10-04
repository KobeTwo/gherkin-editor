package de.gherkineditor.validator;

public abstract class AbstractValidator {


    protected boolean isInputStringEmpty(String input) {
        return (input == null || input.trim().length() == 0);
    }

    protected boolean isPathPatternInvalid(String path) {
        return !path.matches("^/([a-zA-Z0-9-_]+/)*$");
    }
}
