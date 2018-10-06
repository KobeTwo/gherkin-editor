package de.gherkineditor.repository;

import de.gherkineditor.model.Folder;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "folder", path = "folder")
public interface FolderRepository extends CrudRepository<Folder, String>, PathItemRepository<Folder> {



}
