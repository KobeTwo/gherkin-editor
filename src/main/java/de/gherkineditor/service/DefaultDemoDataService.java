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
public class DefaultDemoDataService implements DemoDataService {

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    FolderService folderService;


    @Value("${create.demodata}")
    private Boolean createDemoData;

    @Override
    public void createDemoData() {
        if (this.createDemoData) {
            deleteAllData();
            createProjects();
            createFolder();
            createFeatures();
            createScenarios();
        }
    }

    private void createProjects() {
        List<Project> projects = new ArrayList<Project>();
        projects.add(this.projectService.create(new Project("project1")));
        projects.add(this.projectService.create(new Project("project2")));
        projects.add(this.projectService.create(new Project("project3")));

        this.projectRepository.saveAll(projects);
    }

    private void createFolder() {
        List<Folder> folders = new ArrayList<Folder>();
        folders.add(this.folderService.create(new Folder("1", "/", "project1")));
        folders.add(this.folderService.create(new Folder("2", "/", "project1")));
        folders.add(this.folderService.create(new Folder("1", "/1/", "project1")));
        folders.add(this.folderService.create(new Folder("1", "/1/1/", "project1")));
        folders.add(this.folderService.create(new Folder("2", "/1/", "project1")));
        folders.add(this.folderService.create(new Folder("3", "/1/", "project1")));
        folders.add(this.folderService.create(new Folder("1", "/2/", "project1")));

        this.folderRepository.saveAll(folders);
    }

    private void createFeatures() {
        List<Feature> features = new ArrayList<Feature>();

        features.add(new Feature("feature_p1_1", "feature1file", "/1/", "project1"));
        features.add(new Feature("feature_p1_2", "feature2file", "/1/", "project1"));
        features.add(new Feature("feature_p1_3", "feature3file", "/1/1/", "project1"));
        features.add(new Feature("feature_p1_4", "feature4file", "/1/1/", "project1"));
        features.add(new Feature("feature_p1_5", "feature5file", "/1/2/", "project1"));
        features.add(new Feature("feature_p1_5", "feature5file", "/2/", "project1"));
        features.add(new Feature("feature_p2_1", "feature1file", "/1/", "project2"));

        this.featureRepository.saveAll(features);
    }

    private void createScenarios() {
        List<Scenario> scenarios = new ArrayList<Scenario>();

        scenarios.add(new Scenario("project1", "/1/feature1file", "p1f1s1", "desc1"));
        scenarios.add(new Scenario("project1", "/1/feature1file", "p1f1s2", "desc2"));
        scenarios.add(new Scenario("project1", "/1/feature1file", "p1f1s3", "desc3"));
        scenarios.add(new Scenario("project2", "/2/feature1file", "p2f1s1", "desc4"));
        scenarios.add(new Scenario("project2", "/2/feature1file", "p2sf12", "desc5"));
        scenarios.add(new Scenario("project2", "/2/feature1file", "p2f1s1", "desc6"));

        this.scenarioRepository.saveAll(scenarios);
    }

    private void deleteAllData() {
        this.scenarioRepository.deleteAll();
        this.featureRepository.deleteAll();
        this.folderRepository.deleteAll();
        this.projectRepository.deleteAll();

    }

    @PostConstruct
    private void init() {
        this.createDemoData();
    }
}
