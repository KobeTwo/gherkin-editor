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
public interface ScenarioRepository extends CrudRepository<Scenario, String> {
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
    Optional<Scenario> findByPathAndFileName(@Param("projectId") String projectId , @Param("path") String path, @Param("fileName") String fileName);


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
