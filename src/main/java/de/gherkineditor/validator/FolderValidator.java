package de.gherkineditor.validator;

import de.gherkineditor.model.Folder;
import de.gherkineditor.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component("beforeCreateFolderValidator")
public class FolderValidator extends AbstractValidator implements Validator {

    @Autowired
    FolderRepository folderRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Folder.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        Folder folder = (Folder) obj;

        if (checkInputString(folder.getId())) {
            errors.rejectValue("id", "field.empty");
        }

        if (checkInputString(folder.getId())) {
            errors.rejectValue("projectId", "projectId.empty");
        }

        if (checkInputString(folder.getPath())) {
            errors.rejectValue("path", "path.empty");
        }

        if (checkInputString(folder.getName())) {
            errors.rejectValue("name", "name.empty");
        }

        if (validatePathPattern(folder.getPath())) {
            errors.rejectValue("path", "path.pattern.error");
        }

        if (validatePathUnique(folder.getPath())) {

            errors.rejectValue("path", "path.pattern.error");
        }

        Optional<Folder> existingFolder = folderRepository.findById(folder.getId());
        if(existingFolder.isPresent()){
            errors.reject("object.existing");
        }
    }

    private boolean validatePathUnique(String path){
        return true;
    }



}
