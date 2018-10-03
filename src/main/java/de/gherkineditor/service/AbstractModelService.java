package de.gherkineditor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractValidationService<T> implements ValidationService<T> {
    @Autowired
    private Map<String, Validator> validators;

    private static final String BEFORE_CREATE_PREFIX = "beforeCreate";
    private static final String BEFORE_SAVE_PREFIX = "beforeSave";

    @Override
    public void validateCreate(T object) {
        List<Validator> createValidators = this.validators.entrySet()
                .stream()
                .filter(map -> map.getKey().startsWith(BEFORE_CREATE_PREFIX))
                .filter(map -> map.getValue().supports(object.getClass()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        Errors errors = new BindException(object, object.getClass().getName());
        for (Validator v : createValidators) {
            v.validate(object, errors);
        }
        if (errors.hasErrors()) {
            throw new IllegalArgumentException("VALIDATION FAILED");
        }
    }

    @Override
    public void validateUpdate(T object) {
        List<Validator> createValidators = this.validators.entrySet()
                .stream()
                .filter(map -> map.getKey().startsWith(BEFORE_SAVE_PREFIX))
                .filter(map -> map.getValue().supports(object.getClass()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        Errors errors = new BindException(object, object.getClass().getName());
        for (Validator v : createValidators) {
            v.validate(object, errors);
        }
        if (errors.hasErrors()) {
            throw new IllegalArgumentException("VALIDATION FAILED");
        }
    }

}
