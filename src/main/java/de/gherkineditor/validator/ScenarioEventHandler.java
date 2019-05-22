package de.gherkineditor.validator;

import de.gherkineditor.model.Scenario;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

@RepositoryEventHandler()
public class ScenarioEventHandler {

    @HandleBeforeCreate @HandleBeforeSave public void handleScenarioSave(Scenario scenario) {
        scenario.setDefaultSuggest();
    }

}
