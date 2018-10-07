package de.gherkineditor.controller;

import de.gherkineditor.facade.FolderStructureFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/{projectId}/feature", method = RequestMethod.GET)
public class FeatureController extends AbstractProjectController {

    @Autowired
    FolderStructureFacade folderStructureFacade;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String projectRoot(Model model, @PathVariable String projectId) {
        return projectDetail(model, projectId, null);
    }

    @RequestMapping(value = "/{featureId}", method = RequestMethod.GET)
    public String projectDetail(Model model, @PathVariable String projectId, @PathVariable String featureId) {
        setCurrentProject(projectId, model);

        setCurrentNav(AbstractProjectController.NAV_EDITOR, model);

        if (featureId != null) {
            model.addAttribute("selectedTreeItem", this.folderStructureFacade.getFolderStructureFeature(featureId));
        }


        return "pages/editor";
    }

}