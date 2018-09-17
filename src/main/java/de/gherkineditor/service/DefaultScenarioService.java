package de.gherkineditor.service;

import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultScenarioService implements ScenarioService{

    @Autowired
    ScenarioRepository scenarioRepository;

    @Override
    public Iterable<Scenario> listAllScenarios() {
        Iterable<Scenario> scenarios = scenarioRepository.findAll();
        return scenarios;
    }

    @Override
    public Iterable<Scenario> listScenarios(String projectId) {
        Iterable<Scenario> scenarios = scenarioRepository.findByProject(projectId);
        return scenarios;
    }
}
