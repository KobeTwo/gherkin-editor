package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Project;
import de.gherkineditor.model.Scenario;
import io.cucumber.messages.Messages;

public interface ScenarioService {
    void createScenario(Project project, String absolutePath, Messages.Scenario scenarioMessage);

    Iterable<Scenario> getScenarios(Feature feature);
}
