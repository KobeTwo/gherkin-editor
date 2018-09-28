package de.gherkineditor.validator;

import org.springframework.validation.Validator;

public abstract class AbstractValidator {


    protected boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }

    protected boolean validatePathPattern(String path){
        return true;
    }
}
