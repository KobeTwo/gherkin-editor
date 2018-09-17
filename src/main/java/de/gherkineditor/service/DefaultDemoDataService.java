package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Folder;
import de.gherkineditor.model.Project;
import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.FolderRepository;
import de.gherkineditor.repository.ProjectRepository;
import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultDemoDataService implements DemoDataService{

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Value( "${create.demodata}" )
    private Boolean createDemoData;

    @Override
    public void createDemoData() {
        if(createDemoData){
            createProjects();
            createFolder();
            createFeatures();
            createScenarios();
        }
    }

    private void createProjects(){
        List<Project> projects = new ArrayList<Project>();

        projects.add(new Project("project1"));
        projects.add(new Project("project2"));
        projects.add(new Project("project3"));

        projectRepository.saveAll(projects);
    }

    private void createFolder(){
        List<Folder> folders = new ArrayList<Folder>();

        folders.add(new Folder("folder1", null, "project1"));
        folders.add(new Folder("folder2", null, "project1"));
        folders.add(new Folder("folder1_1", null, "project1"));
        folders.add(new Folder("folder1_2", null, "project1"));
        folders.add(new Folder("folder1_3", null, "project1"));
        folders.add(new Folder("folder2_1", null, "project1"));

        folderRepository.saveAll(folders);
    }

    private void createFeatures(){
        List<Feature> features = new ArrayList<Feature>();

        features.add( new Feature("feature_p1_1", "feature1file", null, "project1"));
        features.add( new Feature("feature_p1_2", "feature2file", "folder1", "project1"));
        features.add( new Feature("feature_p1_3", "feature3file", "folder1_1", "project1"));
        features.add( new Feature("feature_p1_4", "feature4file", "folder1_1", "project1"));
        features.add( new Feature("feature_p1_5", "feature5file", "folder1_2", "project1"));
        features.add( new Feature("feature_p2_1", "feature1file", null, "project2"));

        featureRepository.saveAll(features);
    }

    private void createScenarios(){
        List<Scenario> scenarios = new ArrayList<Scenario>();

        scenarios.add( new Scenario("project1", "feature_p1_1","p1f1s1", "desc1"));
        scenarios.add( new Scenario("project1", "feature_p1_1","p1f1s2", "desc2"));
        scenarios.add( new Scenario("project1", "feature_p1_1","p1f1s3", "desc3"));
        scenarios.add( new Scenario("project2", "feature_p2_1","p2f1s1", "desc4"));
        scenarios.add( new Scenario("project2", "feature_p2_1","p2sf12", "desc5"));
        scenarios.add( new Scenario("project2", "feature_p2_1","p2f1s1", "desc6"));

        scenarioRepository.saveAll(scenarios);
    }

    @PostConstruct
    private void init(){
        this.createDemoData();
    }
}
