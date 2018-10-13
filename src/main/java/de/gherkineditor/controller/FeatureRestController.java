package de.gherkineditor.controller;

import de.gherkineditor.model.Feature;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class FeatureRestController {

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ScenarioRepository scenarioRepository;

    @RequestMapping(value = "/rest/api/feature/{featureId}", produces = "application/json", method = RequestMethod.DELETE)
    public String deleteFeature(@PathVariable(name = "featureId", required = true) String featureId) {
        Optional<Feature> featureOptional = this.featureRepository.findById(featureId);
        if (featureOptional.isPresent()) {
            this.scenarioRepository.deleteAll(this.scenarioRepository.findChildrenRecursive(featureOptional.get().getProjectId(), featureOptional.get().getPath()));
            this.featureRepository.delete(featureOptional.get());
        }
        return "";
    }

}