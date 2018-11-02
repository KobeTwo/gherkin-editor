package de.gherkineditor.service;

import de.gherkineditor.model.Folder;
import de.gherkineditor.model.Project;

public interface FolderService extends ModelService<Folder> {
    Iterable<Folder> listAllFolders();

    Iterable<Folder> listFolders(String projectId);

    void createFoldersForFeatureImport(Project project, String inputFileName);

}
