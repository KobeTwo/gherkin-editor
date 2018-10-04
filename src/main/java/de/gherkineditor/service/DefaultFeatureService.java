package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultFeatureService implements FeatureService {

    @Autowired
    FeatureRepository featureRepository;

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
}
