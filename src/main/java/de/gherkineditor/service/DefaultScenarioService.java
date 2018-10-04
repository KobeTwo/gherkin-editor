package de.gherkineditor.service;

import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultScenarioService implements ScenarioService {

    @Autowired
    ScenarioRepository scenarioRepository;

    @Override
    public Iterable<Scenario> listAllScenarios() {
        Iterable<Scenario> scenarios = this.scenarioRepository.findAll();
        return scenarios;
    }

    @Override
    public Iterable<Scenario> listScenarios(String projectId) {
        Iterable<Scenario> scenarios = this.scenarioRepository.findAll(projectId);
        return scenarios;
    }
}
