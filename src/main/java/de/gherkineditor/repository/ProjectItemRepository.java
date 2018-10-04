package de.gherkineditor.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectItemRepository<T> {

    @Query("{ " +
                "\"bool\": {" +
                    "\"filter\": { " +
                        "\"term\": {" +
                            "\"projectId.raw\": \"?0\" " +
                            "} " +
                        "} " +
                    "} " +
                "}")
    Iterable<T> findAll(@Param("projectId") String projectId);
}
