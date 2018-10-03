package de.gherkineditor.service;

public interface ValidationService<T> {
    void validateCreate(T object);

    void validateUpdate(T object);
}
