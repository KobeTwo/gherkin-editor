package de.gherkineditor.facade;

import de.gherkineditor.dto.FolderStructureItem;
import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Folder;
import de.gherkineditor.model.Project;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.FolderRepository;
import de.gherkineditor.service.FeatureService;
import de.gherkineditor.service.FolderService;
import de.gherkineditor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultFolderStructureFacade implements FolderStructureFacade {

    @Autowired
    ProjectService projectService;

    @Autowired
    FolderService folderService;

    @Autowired
    FeatureService featureService;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Override
    public List<FolderStructureItem> getFolderStructure(String projectId) {
        return getFolderStructure(projectId, "/");


    }

    @Override
    public List<FolderStructureItem> getFolderStructure(String projectId, String path) {

        Project project = this.projectService.getProject(projectId);
        List<FolderStructureItem> struct = new ArrayList<>();

        Iterable<Folder> folders = this.folderRepository.findChildren(project.getId(), path);
        Iterable<Feature> features = this.featureRepository.findChildren(project.getId(), path);

        if (!folders.iterator().hasNext()) {
            return null;
        }

        for (Folder folder : folders) {
            FolderStructureItem item = new FolderStructureItem();
            item.setModel(folder);
            item.setType(FolderStructureItem.TYPE.FOLDER);
            item.setChildFolders(getFolderStructure(project.getId(), folder.getPath().concat(folder.getName()).concat("/")));
            struct.add(item);
        }

        for (Feature feature : features) {
            FolderStructureItem item = new FolderStructureItem();
            item.setModel(feature);
            item.setType(FolderStructureItem.TYPE.FEATURE);
            item.setChildFolders(null);
            struct.add(item);
        }

        return struct;
    }

}
