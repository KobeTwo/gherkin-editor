package de.gherkineditor.controller;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Folder;
import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.ScenarioRepository;
import de.gherkineditor.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@BasePathAwareController
public class FeatureRestController {

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ScenarioRepository scenarioRepository;

    @RequestMapping(value = "feature/{featureId}", produces = "application/json", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/feature/{featureId}", method = RequestMethod.PATCH, consumes = "application/json")
    public @ResponseBody
    Feature saveFeature(@RequestBody Feature newFeature){
        Optional<Feature> oldFeature = featureRepository.findById(newFeature.getId());

        if(oldFeature.isPresent() && oldFeature.get().getPath() != newFeature.getPath()){
            List<Scenario> scenarios = this.scenarioRepository.findChildrenRecursive(oldFeature.get().getProjectId(), oldFeature.get().getPath(), null);
            for(Scenario scenario : scenarios){
                scenario.setPath(scenario.getPath().replace(oldFeature.get().getPath(), newFeature.getPath()));
            }
            if(scenarios.size() > 0){
                scenarioRepository.saveAll(scenarios);
            }
        }

        return featureRepository.save(newFeature);
    }

}
