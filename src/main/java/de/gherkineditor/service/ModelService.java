package de.gherkineditor.service;

public interface ModelService<T> {
    void validateCreate(T object);

    void validateUpdate(T object);

    T create(T object);

    T update(T object);
}
