package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Project;
import de.gherkineditor.model.Step;
import de.gherkineditor.repository.FeatureRepository;
import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class DefaultFeatureService implements FeatureService {

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    FolderService folderService;

    @Autowired
    ScenarioService scenarioService;

    @Autowired
    TemplateEngine basicTemplateEngine;

    @Override
    public Iterable<Feature> listAllFeatures() {
        Iterable<Feature> features = this.featureRepository.findAll();
        return features;
    }

    @Override
    public Iterable<Feature> listFeatures(String projectId) {
        Iterable<Feature> features = this.featureRepository.findAll(projectId);
        return features;
    }

    @Override
    public void createFeature(Project project, String absolutePath, String content) {

        Messages.Source source = Messages.Source.newBuilder().setData(content).build();
        List<Messages.Wrapper> messages = Gherkin.fromSources(Collections.singletonList(source), false, true, false);
        Messages.GherkinDocument gherkinDocument = messages.get(0).getGherkinDocument();
        Messages.Feature feature = gherkinDocument.getFeature();
        this.createFeature(project, absolutePath, feature);
    }

    @Override
    public void createFeature(Project project, String absolutePath, Messages.Feature featureMessage) {
        String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator) + 1);
        String fileName = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);

        //create folder structure
        this.folderService.createParentFoldersForFeature(project, absolutePath);

        //create basic feature
        Feature feature = new Feature(project.getId(), path, fileName, featureMessage.getName().trim(), featureMessage.getDescription().trim());

        //add tags to feature
        for (Messages.Tag tag : featureMessage.getTagsList()) {
            feature.addTag(tag.getName().trim());
        }

        for (Messages.FeatureChild child : featureMessage.getChildrenList()) {
            if (child.hasBackground()) {
                Messages.Background backgroundMessage = child.getBackground();
                for (Messages.Step stepMessage : backgroundMessage.getStepsList()) {
                    feature.addBackgroundSteps(new Step(Step.TYPE.valueOf(stepMessage.getKeyword().toUpperCase().trim()), stepMessage.getText().trim()));
                }

            }

            if (child.hasScenario()) {
                this.scenarioService.createScenario(project, absolutePath, child.getScenario());
            }
        }


        this.featureRepository.save(feature);
    }

    @Override
    public String getFeatureContent(Feature feature) {
        final Context ctx = new Context();

        ctx.setVariable("feature", feature);
        ctx.setVariable("scenarios", this.scenarioService.getScenarios(feature));
        String featureContent = this.basicTemplateEngine.process("text/feature.txt", ctx);

        System.out.println(featureContent);

        return featureContent;
    }


}
