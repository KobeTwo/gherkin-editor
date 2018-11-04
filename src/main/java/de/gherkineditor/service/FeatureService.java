package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Project;
import io.cucumber.messages.Messages;

public interface FeatureService {
    Iterable<Feature> listAllFeatures();

    Iterable<Feature> listFeatures(String projectId);

    void createFeature(Project project, String absolutePath, String content);

    void createFeature(Project project, String absolutePath, Messages.Feature featureMessage);
}
