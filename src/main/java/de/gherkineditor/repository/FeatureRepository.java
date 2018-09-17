package de.gherkineditor.repository;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "feature", path = "feature")
public interface FeatureRepository extends CrudRepository<Feature, String> {
    long countByProjectId(@Param("projectId") String projectId);
}
