package de.gherkineditor.repository;

import de.gherkineditor.model.Scenario;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "scenario", path = "scenario")
public interface ScenarioRepository extends CrudRepository<Scenario, String>, PathItemRepository<Scenario> {
    @Query("{ " +
                "\"bool\": {" +
                    "\"filter\": { " +
                        "\"term\": {" +
                            "\"projectId.raw\": \"?0\" " +
                            "} " +
                        "} " +
                    "} " +
                "}")
    Iterable<Scenario> findAll(@Param("projectId") String projectId);
}
