package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Scenario;

import java.io.IOException;

public interface GherkinService {
    void validateScenario(Scenario scenario);

    void validateFeature(Feature feature) throws IOException;

}
