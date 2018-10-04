package de.gherkineditor.controller;

import de.gherkineditor.dto.FolderStructureItem;
import de.gherkineditor.facade.FolderStructureFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FolderRestController {

    @Autowired
    FolderStructureFacade folderStructureFacade;

    @RequestMapping(value = "/api/{projectId}/folder/structure", produces = "application/json")
    public List<FolderStructureItem> folderStructure(@PathVariable String projectId) {

        return this.folderStructureFacade.getFolderStructure(projectId);
    }

}
