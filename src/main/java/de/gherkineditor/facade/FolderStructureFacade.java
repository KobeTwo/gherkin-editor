package de.gherkineditor.facade;

import de.gherkineditor.dto.FolderStructureItem;

import java.util.List;

public interface FolderStructureFacade {
    List<FolderStructureItem> getFolderStructure(String projectId);

    List<FolderStructureItem> getFolderStructure(String projectId, String path);

    FolderStructureItem getFolderStructureFolder(String folderId);

    FolderStructureItem getFolderStructureFeature(String featureId);
}
