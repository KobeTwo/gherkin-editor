package de.gherkineditor.service;

import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultFeatureService implements ScenarioService{

    @Autowired
    ScenarioRepository scenarioRepository;

    @Override
    public Iterable<Scenario> listAllScenarios() {
        Iterable<Scenario> scenarios = scenarioRepository.findAll();
        return scenarios;
    }

    @Override
    public Iterable<Scenario> listScenarios(String projectId) {
        Iterable<Scenario> scenarios = scenarioRepository.findByProjectId(projectId);
        return scenarios;
    }
}
