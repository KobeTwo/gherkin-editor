package de.gherkineditor.repository;

import de.gherkineditor.model.Feature;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "feature", path = "feature")
public interface FeatureRepository extends CrudRepository<Feature, String>, PathItemRepository<Feature> {

}
