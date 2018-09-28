package de.gherkineditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "feature", type = "feature", shards = 1, replicas = 0, refreshInterval = "-1")
public class Feature extends AbstractProjectItem {

    @Id
    private String id;

    private String name;

    private String fileName;

    private String path;

    public Feature(String name, String fileName, String path, String projectId) {
        this.name = name;
        this.fileName = fileName;
        this.path = path;
        this.setProjectId(projectId);
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return String.format("Folder[id=%s, name='%s']", this.id,
                this.name);
    }

}