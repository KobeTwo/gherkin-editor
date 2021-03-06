package de.gherkineditor.repository;

import de.gherkineditor.model.Feature;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "feature", path = "feature")
public interface FeatureRepository extends CrudRepository<Feature, String>, PathItemRepository<Feature> {
    @Query("{\"bool\": " +
                "{\"filter\": " +
                    "{\"bool\": " +
                        "{\"must\": " +
                            "[" +
                                "{\"term\": " +
                                    "{\"projectId.raw\": \"?0\" " +
                                    "} " +
                                "} ," +
                                "{\"term\": " +
                                    "{\"path.raw\": \"?1\" " +
                                    "} " +
                                "} ," +
                                "{\"term\": " +
                                    "{\"fileName.raw\": \"?2\" " +
                                    "} " +
                                "} " +
                            "] " +
                        "}" +
                    "} " +
                "} " +
            "}")
    Optional<Feature> findByPathAndName(@Param("projectId") String projectId , @Param("path") String path,  @Param("fileName") String fileName);

}