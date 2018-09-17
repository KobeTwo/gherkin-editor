package de.gherkineditor.service;

import de.gherkineditor.model.Scenario;

import java.util.List;

public interface ScenarioService {
    Iterable<Scenario> listAllScenarios();
    Iterable<Scenario> listScenarios(String projectId);
}
