package de.gherkineditor.service;

import de.gherkineditor.model.Folder;

public interface FolderService extends ModelService<Folder> {
    Iterable<Folder> listAllFolders();

    Iterable<Folder> listFolders(String projectId);

}
