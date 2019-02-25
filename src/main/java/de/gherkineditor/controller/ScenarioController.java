package de.gherkineditor.controller;

import de.gherkineditor.facade.FolderStructureFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/{projectId}/scenario", method = RequestMethod.GET)
public class ScenarioController extends AbstractProjectController {

    @Autowired
    FolderStructureFacade folderStructureFacade;

    @RequestMapping(value = "/{scenarioId}", method = RequestMethod.GET)
    public String scenario(Model model, @PathVariable String projectId, @PathVariable String scenarioId) {
        setCurrentProject(projectId, model);

        setCurrentNav(AbstractProjectController.NAV_EDITOR, model);
        

        if (scenarioId != null) {
            model.addAttribute("selectedTreeItem", this.folderStructureFacade.getFolderStructureScenario(scenarioId));
        }


        return "pages/editor";
    }

}