package de.gherkineditor.service;

import de.gherkineditor.model.Scenario;

public interface FolderService {
    Iterable<Scenario> listAllScenarios();
    Iterable<Scenario> listScenarios(String projectId);
}
