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
import de.gherkineditor.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        if (!folders.iterator().hasNext() && !features.iterator().hasNext()) {
            return Collections.EMPTY_LIST;
        }

        for (Folder folder : folders) {
            FolderStructureItem item = new FolderStructureItem();
            item.setModel(folder);
            item.setType(FolderStructureItem.TYPE.FOLDER);
            item.setChildren(getFolderStructure(project.getId(), Util.getConcatenatedPath(folder.getPath(), folder.getFileName())));
            struct.add(item);
        }

        for (Feature feature : features) {
            FolderStructureItem item = new FolderStructureItem();
            item.setModel(feature);
            item.setType(FolderStructureItem.TYPE.FEATURE);
            item.setChildren(null);
            struct.add(item);
        }

        return struct;
    }

    @Override
    public FolderStructureItem getFolderStructureFolder(String folderId) {
        Optional<Folder> folderOptional = this.folderRepository.findById(folderId);
        if (!folderOptional.isPresent()) {
            throw new IllegalArgumentException("Folder was not found");
        }
        FolderStructureItem item = new FolderStructureItem();
        item.setModel(folderOptional.get());
        item.setType(FolderStructureItem.TYPE.FOLDER);
        item.setChildren(new ArrayList<>());

        return item;
    }

    @Override
    public FolderStructureItem getFolderStructureFeature(String featureId) {
        Optional<Feature> featureOtional = this.featureRepository.findById(featureId);
        if (!featureOtional.isPresent()) {
            throw new IllegalArgumentException("Feature was not found");
        }
        FolderStructureItem item = new FolderStructureItem();
        item.setModel(featureOtional.get());
        item.setType(FolderStructureItem.TYPE.FEATURE);
        item.setChildren(null);

        return item;
    }

}
