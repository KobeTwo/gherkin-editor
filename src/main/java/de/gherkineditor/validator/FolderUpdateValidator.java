package de.gherkineditor.validator;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component("beforeSaveFolderValidator")
public class FolderUpdateValidator extends FolderBaseValidator {

    @Autowired
    FolderRepository folderRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Folder.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        Folder folder = (Folder) obj;

        if (isInputStringEmpty(folder.getId())) {
            errors.rejectValue("id", "field.empty", "Id must not be empty");
        } else {
            Optional<Folder> existingIdFolder = this.folderRepository.findById(folder.getId());
            if (existingIdFolder.isPresent() && !existingIdFolder.get().getId().equals(folder.getId())) {
                errors.reject("object.existing", "Folder with id is already existing");
            }
        }

        if (!isInputStringEmpty(folder.getPath()) && !isInputStringEmpty(folder.getProjectId())) {
            Optional<Folder> existingPathFolder = this.folderRepository.findByPathAndName(folder.getProjectId(), folder.getPath(), folder.getFileName());
            if (existingPathFolder.isPresent() && !existingPathFolder.get().getId().equals(folder.getId())) {
                errors.reject("object.existing", "Folder with this path and file name is already existing within project");
            }
        }
    }

}
