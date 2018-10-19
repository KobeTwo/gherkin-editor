package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Scenario;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DefaultGherkinService implements GherkinService {
    @Override
    public void validateScenario(Scenario scenario) {

    }

    @Override
    public void validateFeature(Feature feature) throws IOException {

    }
}
