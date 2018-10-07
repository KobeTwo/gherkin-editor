package de.gherkineditor.controller;

import de.gherkineditor.dto.FolderStructureItem;
import de.gherkineditor.facade.FolderStructureFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FolderRestController {

    @Autowired
    FolderStructureFacade folderStructureFacade;

    @RequestMapping(value = "/rest/api/treeStructure", produces = "application/json")
    public List<FolderStructureItem> folderStructure(@RequestParam(name = "projectId", required = true) String projectId) {
        return this.folderStructureFacade.getFolderStructure(projectId);
    }

}
