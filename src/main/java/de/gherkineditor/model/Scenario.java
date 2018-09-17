package de.gherkineditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "scenario", type = "scenario", shards = 1, replicas = 0, refreshInterval = "-1")
public class Scenario extends AbstractProjectItem {

    @Id
    private String id;

    private String name;

    private String description;

    String featureId;

    private Scenario() {
    }

    public Scenario(String projectId, String featureId, String name, String description) {
        this.name = name;
        this.description = description;
        setProjectId(projectId);
        this.featureId = featureId;
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

    public String getText() {
        return this.description;
    }

    public void setText(String text) {
        this.description = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    @Override
    public String toString() {
        return String.format("Scenario[id=%s, name='%s']", this.id,
                this.name);
    }

}