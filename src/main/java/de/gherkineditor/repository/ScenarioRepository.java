package de.gherkineditor.repository;

import de.gherkineditor.model.Scenario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "scenario", path = "scenario")
public interface ScenarioRepository extends CrudRepository<Scenario, String> {
}
