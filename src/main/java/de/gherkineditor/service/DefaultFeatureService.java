package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Project;
import de.gherkineditor.repository.FeatureRepository;
import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class DefaultFeatureService implements FeatureService {

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    FolderService folderService;

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
        String path = "/" + absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
        String fileName = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);

        //create folder structure
        this.folderService.createParentFoldersForFeature(project, absolutePath);

        //create basic feature
        Feature feature = new Feature(project.getId(), path, fileName, featureMessage.getName(), featureMessage.getDescription());

        //add tags
        for (Messages.Tag tag : featureMessage.getTagsList()) {
            feature.addTag(tag.getName());
        }


        this.featureRepository.save(feature);
    }

}
