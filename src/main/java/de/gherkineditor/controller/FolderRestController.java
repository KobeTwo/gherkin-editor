package de.gherkineditor.controller;

import de.gherkineditor.dto.FolderStructureItem;
import de.gherkineditor.facade.FolderStructureFacade;
import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Folder;
import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.FolderRepository;
import de.gherkineditor.repository.ScenarioRepository;
import de.gherkineditor.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class FolderRestController {

    @Autowired
    FolderStructureFacade folderStructureFacade;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    FolderRepository folderRepository;

    @RequestMapping(value = "/rest/api/treeStructure", produces = "application/json")
    public List<FolderStructureItem> folderStructure(@RequestParam(name = "projectId", required = true) String projectId) {
        return this.folderStructureFacade.getFolderStructure(projectId);
    }

    @RequestMapping(value = "/rest/api/folder/{folderId}", produces = "application/json", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFolder(@PathVariable(name = "folderId", required = true) String folderId) {
        Optional<Folder> folderOptional = this.folderRepository.findById(folderId);
        if (folderOptional.isPresent()) {
            String path = Util.getConcatenatedPath(folderOptional.get().getPath(), folderOptional.get().getFileName());
            Iterable<Scenario> scenariosToDelete = this.scenarioRepository.findChildrenRecursive(folderOptional.get().getProjectId(), path);
            Iterable<Feature> featuresToDelete = this.featureRepository.findChildrenRecursive(folderOptional.get().getProjectId(), path);
            this.scenarioRepository.deleteAll(scenariosToDelete);
            this.featureRepository.deleteAll(featuresToDelete);
            this.folderRepository.delete(folderOptional.get());
        }
    }

}
