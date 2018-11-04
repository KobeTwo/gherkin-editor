package de.gherkineditor.controller;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.ScenarioRepository;
import de.gherkineditor.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class FeatureRestController {

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ScenarioRepository scenarioRepository;

    @RequestMapping(value = "/rest/api/feature/{featureId}", produces = "application/json", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFeature(@PathVariable(name = "featureId", required = true) String featureId) {
        Optional<Feature> featureOptional = this.featureRepository.findById(featureId);
        if (featureOptional.isPresent()) {
            String path = Util.getConcatenatedPath(featureOptional.get().getPath(), featureOptional.get().getFileName());
            Iterable<Scenario> scenariosToDelete = this.scenarioRepository.findChildrenRecursive(featureOptional.get().getProjectId(), path, null);
            this.scenarioRepository.deleteAll(scenariosToDelete);
            this.featureRepository.delete(featureOptional.get());
        }
    }

}
