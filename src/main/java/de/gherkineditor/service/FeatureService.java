package de.gherkineditor.service;

import de.gherkineditor.model.Scenario;

public interface FeatureService {
    Iterable<Scenario> listAllScenarios();
    Iterable<Scenario> listScenarios(String projectId);
}
