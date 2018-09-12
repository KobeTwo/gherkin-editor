package de.gherkineditor.service;

import de.gherkineditor.model.Project;
import de.gherkineditor.repository.FolderRepository;
import de.gherkineditor.repository.ProjectRepository;
import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DefaultDemoDataService implements DemoDataService{

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Value( "${create.demodata}" )
    private Boolean createDemoData;

    @Override
    public void createDemoData() {
        if(createDemoData){
            Project project1 = new Project("project1");
            Project project2 = new Project("project2");
            Project project3 = new Project("project3");
            projectRepository.save(project1);
            projectRepository.save(project2);
            projectRepository.save(project3);
        }
    }

    @PostConstruct
    private void init(){
        this.createDemoData();
    }
}
