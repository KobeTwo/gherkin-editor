package de.gherkineditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "folder", type = "folder", shards = 1, replicas = 0, refreshInterval = "-1")
public class Folder extends AbstractProjectItem {

    @Id
    private String id;

    private String name;

    private String path;

    private Folder() {
    }

    public Folder(String name, String path, String projectId) {

        this.name = name;
        this.path = path;
        setProjectId(projectId);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("Folder[id=%s, name='%s']", this.id,
                this.name);
    }

}