package de.gherkineditor.service;

import de.gherkineditor.model.Project;
import io.cucumber.messages.Messages;

public interface ScenarioService {
    void createScenario(Project project, String absolutePath, Messages.Scenario scenarioMessage);
}
