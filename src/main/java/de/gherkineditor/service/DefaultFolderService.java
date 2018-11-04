package de.gherkineditor.service;

import de.gherkineditor.model.Folder;
import de.gherkineditor.model.Project;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

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
    public void createParentFoldersForFeature(Project project, String featureAbsolutePath) {
        String inputPath = featureAbsolutePath.substring(1, featureAbsolutePath.lastIndexOf(File.separator));
        String[] dirs = inputPath.split(File.separator);
        String path = "/";
        for (String fileName : dirs) {
            Optional<Folder> folderOptional = this.folderRepository.findByPathAndName(project.getId(), path, fileName);
            if (!folderOptional.isPresent()) {
                this.create(new Folder(project.getId(), path, fileName));
            }
            path += fileName + "/";
        }
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
