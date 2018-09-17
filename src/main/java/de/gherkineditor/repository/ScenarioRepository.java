package de.gherkineditor.repository;

import de.gherkineditor.model.Scenario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "scenario", path = "scenario")
public interface ScenarioRepository extends CrudRepository<Scenario, String> {
    @Query("{ \"bool\": { \"filter\": { \"term\": {\"projectId\": \"?0\" } } } }")
    Iterable<Scenario> findByProjectId( @Param("projectId") String projectId);
    long countByProjectId(@Param("projectId") String projectId);
}
