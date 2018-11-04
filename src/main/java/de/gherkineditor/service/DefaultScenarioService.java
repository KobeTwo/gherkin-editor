package de.gherkineditor.service;

import de.gherkineditor.model.Project;
import de.gherkineditor.model.Scenario;
import de.gherkineditor.model.Step;
import de.gherkineditor.repository.ScenarioRepository;
import io.cucumber.messages.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultScenarioService implements ScenarioService {

    @Autowired
    ScenarioRepository scenarioRepository;


    @Override
    public void createScenario(Project project, String absolutePath, Messages.Scenario scenarioMessage) {
        Scenario scenario = new Scenario(project.getId(), absolutePath, scenarioMessage.getName().trim(), scenarioMessage.getDescription().trim());

        //add tags to scenario
        for (Messages.Tag tagMessage : scenarioMessage.getTagsList()) {
            scenario.addTag(tagMessage.getName().trim());
        }

        //add steps for scenario
        for (Messages.Step stepMessage : scenarioMessage.getStepsList()) {
            scenario.addStep(new Step(Step.TYPE.valueOf(stepMessage.getKeyword().toUpperCase().trim()), stepMessage.getText().trim()));
        }

        this.scenarioRepository.save(scenario);
    }
}
