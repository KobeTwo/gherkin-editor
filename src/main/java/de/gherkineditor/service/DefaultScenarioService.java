package de.gherkineditor.service;

import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultScenarioService implements ScenarioService{

    @Autowired
    ScenarioRepository scenarioRepository;

    @Override
    public Iterable<Scenario> listScenarios() {
        Iterable<Scenario> scenarios = scenarioRepository.findAll();
        return scenarios;
    }
}
