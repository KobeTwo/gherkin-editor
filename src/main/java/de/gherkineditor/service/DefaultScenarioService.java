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
            Step step = new Step(Step.TYPE.valueOf(stepMessage.getKeyword().toUpperCase().trim()), stepMessage.getText().trim());

            //add doc string
            if (stepMessage.getArgumentCase().equals(Messages.Step.ArgumentCase.DOC_STRING)) {
                step.setDocstring(stepMessage.getDocString().getContent());
            }

            //add data table
            if (stepMessage.getArgumentCase().equals(Messages.Step.ArgumentCase.DATA_TABLE)) {
                Messages.DataTable dataTable = stepMessage.getDataTable();
                String[][] dataTableArray = new String[dataTable.getRowsCount()][];
                for (int i = 0; i < dataTable.getRowsCount(); i++) {
                    Messages.TableRow row = dataTable.getRows(i);
                    String[] rowArray = new String[row.getCellsCount()];
                    for (int j = 0; j < row.getCellsCount(); j++) {
                        Messages.TableCell cell = row.getCells(j);
                        rowArray[j] = cell.getValue();
                    }
                    dataTableArray[i] = rowArray;
                }
                step.setDatatable(dataTableArray);
            }
            scenario.addStep(step);
        }

        this.scenarioRepository.save(scenario);
    }
}
