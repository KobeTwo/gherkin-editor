package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Project;
import de.gherkineditor.model.Scenario;
import de.gherkineditor.model.Step;
import de.gherkineditor.repository.FeatureRepository;
import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import io.netty.util.internal.StringUtil;
import org.elasticsearch.search.aggregations.metrics.percentiles.hdr.InternalHDRPercentiles;
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

    final static String INDENT0 = "";
    final static String INDENT1 = "  ";
    final static String INDENT2 = "    ";
    final static String INDENT3 = "      ";
    final static String DOCSTRING_QUOTES = "\"\"\"";

    @Override
    public Iterable<Feature> listAllFeatures() {
        return this.featureRepository.findAll();
    }

    @Override
    public Iterable<Feature> listFeatures(String projectId) {
        return this.featureRepository.findAll(projectId);
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

        Iterable<Scenario> scenarios = this.scenarioService.getScenarios(feature);

        StringBuilder builder = new StringBuilder();

        writeTags(feature.getTags(), builder, INDENT0);
        builder.append("Feature: " + feature.getName())
        .append("\n")
        .append(INDENT1 + feature.getDescription());
        writeBackgroundSteps(feature,builder);
        writeScenarios(scenarios,builder);


        String featureContent = builder.toString();

        System.out.println(featureContent);

        return featureContent;
    }


    private void writeTags(List<String> tags, StringBuilder builder, String indent){
        if(!tags.isEmpty()){
            builder.append(indent);
            for(String tag: tags){
                builder.append(tag + " ");
            }
            builder.append("\n");
        }
    }

    private void writeBackgroundSteps(Feature feature, StringBuilder builder){
        if(!feature.getBackgroundSteps().isEmpty()){
            builder.append("\n\n");
            builder.append(INDENT1 + "Background:");
            writeSteps(feature.getBackgroundSteps(),builder);
        }
    }

    private void writeScenarios(Iterable<Scenario> scenarios, StringBuilder builder){
        for(Scenario scenario: scenarios){
            builder.append("\n\n");
            writeTags(scenario.getTags(), builder, INDENT1);
            builder.append(INDENT1 + "Scenario: " + scenario.getName());
            writeSteps(scenario.getSteps(), builder);
        }

    }

    private void writeSteps(List<Step> steps, StringBuilder builder){
        for(Step step : steps){
            builder.append("\n");
            builder.append(INDENT2 + step.getType() + StringUtil.SPACE + step.getText());
            writeDocstring(step, builder);
            writeDatatable(step, builder);
        }
    }

    private void writeDocstring(Step step, StringBuilder builder){
        if(step.getDocstring() != null){
            builder.append("\n")
            .append(INDENT3 + DOCSTRING_QUOTES)
            .append("\n")
            .append(INDENT3 + step.getDocstring())
            .append("\n")
            .append(INDENT3 + DOCSTRING_QUOTES);
        }
    }

    private void writeDatatable(Step step, StringBuilder builder){
        if(step.getDatatable() != null){
            builder.append("\n");
            String indentedDatatable = step.getDatatable().replaceAll("\\n", StringUtil.NEWLINE + INDENT3 );
            builder.append(INDENT3 + indentedDatatable);
        }
    }


}
