package de.gherkineditor.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PathItemRepository<T>  extends ProjectItemRepository<T>  {
    @Query("{\"bool\": " +
                "{\"filter\": " +
                    "{\"bool\": " +
                        "{\"must\": " +
                            "[" +
                                "{\"term\": " +
                                    "{\"projectId.raw\": \"?0\" " +
                                    "} " +
                                "} ," +
                                "{\"regexp\": " +
                                    "{\"path.raw\": \"?1/?\" " +
                                    "} " +
                                "} " +
                            "] " +
                        "}" +
                    "} " +
                "} " +
            "}")
    Iterable<T> findChildren(@Param("projectId") String projectId , @Param("path") String path);

    @Query("{\"bool\": " +
                "{\"filter\": " +
                    "{\"bool\": " +
                        "{\"must\": " +
                            "[" +
                                "{\"term\": " +
                                    "{\"projectId.raw\": \"?0\" " +
                                    "} " +
                                "} ," +
                                "{\"regexp\": " +
                                    "{\"path.raw\": \"?1/?([a-zA-Z0-9-_\\\\.]+/?)*\" " +
                                    "} " +
                                "} " +
                            "] " +
                        "}" +
                    "} " +
                "} " +
            "}")
    Iterable<T> findChildrenRecursive(@Param("projectId") String projectId , @Param("path") String path);

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
    Optional<T> findByPathAndName(@Param("projectId") String projectId , @Param("path") String path, @Param("fileName") String fileName);
}
