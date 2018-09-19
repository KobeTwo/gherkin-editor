package de.gherkineditor.repository;

import de.gherkineditor.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "project", path = "project")
public interface ProjectRepository extends CrudRepository<Project, String> {
}
