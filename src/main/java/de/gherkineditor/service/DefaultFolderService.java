package de.gherkineditor.service;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultFolderService extends AbstractModelService<Folder> implements FolderService {

    @Autowired
    FolderRepository folderRepository;

    @Override
    public Iterable<Folder> listAllFolders() {
        Iterable<Folder> folders = this.folderRepository.findAll();
        return folders;
    }

    @Override
    public Iterable<Folder> listFolders(String projectId) {
        Iterable<Folder> folders = this.folderRepository.findAll(projectId);
        return folders;
    }


    @Override
    public Folder create(Folder folder) {
        validateCreate(folder);
        return this.folderRepository.save(folder);

    }

    @Override
    public Folder update(Folder folder) {
        validateUpdate(folder);
        return this.folderRepository.save(folder);
    }
}
