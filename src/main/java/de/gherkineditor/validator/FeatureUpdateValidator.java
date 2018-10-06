package de.gherkineditor.validator;

import de.gherkineditor.model.Feature;
import de.gherkineditor.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component("beforeSaveFeatureValidator")
public class FeatureUpdateValidator extends FeatureBaseValidator {

    @Autowired
    FeatureRepository featureRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Feature.class.equals(clazz);
    }


    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        Feature feature = (Feature) obj;

        if (isInputStringEmpty(feature.getId())) {
            errors.rejectValue("id", "field.empty", "Id must not be empty");
        } else {
            Optional<Feature> featureOptional = this.featureRepository.findById(feature.getId());
            if (featureOptional.isPresent() && !featureOptional.get().getId().equals(feature.getId())) {
                errors.reject("object.existing", "Feature with id is already existing");
            }
        }

        if (!isInputStringEmpty(feature.getPath()) && !isInputStringEmpty(feature.getProjectId())) {
            Optional<Feature> featureOptional = this.featureRepository.findByPathAndName(feature.getProjectId(), feature.getPath(), feature.getFileName());
            if (featureOptional.isPresent() && !featureOptional.get().getId().equals(feature.getId())) {
                errors.reject("object.existing", "Feature with this path and file name is already existing within project");
            }
        }
    }

}
