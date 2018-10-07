package de.gherkineditor.service;

import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultScenarioService implements ScenarioService {

    @Autowired
    ScenarioRepository scenarioRepository;


}
