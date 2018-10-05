package de.gherkineditor.validator;

import org.springframework.validation.Validator;

public abstract class AbstractValidator implements Validator {
    protected boolean isInputStringEmpty(String input) {
        return (input == null || input.trim().length() == 0);
    }
}
