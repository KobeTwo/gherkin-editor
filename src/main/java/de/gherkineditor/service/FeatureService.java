package de.gherkineditor.service;

import de.gherkineditor.model.Feature;

public interface FeatureService {
    Iterable<Feature> listAllFeatures();

    Iterable<Feature> listFeatures(String projectId);
}
