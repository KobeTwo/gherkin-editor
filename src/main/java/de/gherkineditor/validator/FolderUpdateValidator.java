package de.gherkineditor.validator;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component("beforeSaveFolderValidator")
public class FolderCreateValidator extends FolderBaseValidator {

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

        if (!isInputStringEmpty(folder.getId())) {
            Optional<Folder> existingIdFolder = this.folderRepository.findById(folder.getId());
            if (existingIdFolder.isPresent()) {
                errors.reject("object.existing");
            }
        }
    }

}
