package de.gherkineditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "scenario", type = "scenario", shards = 1, replicas = 0, refreshInterval = "-1")
public class Scenario extends AbstractPathItem {

    @Id
    private String id;

    private String name;

    private String description;

    @Field(type = FieldType.Keyword)
    private List<String> tags = new ArrayList<>();

    @Field(type = FieldType.Nested)
    private List<Step> steps = new ArrayList<>();

    private String[][] examples;

    private Scenario() {
    }

    public Scenario(String projectId, String path, String name, String description) {
        this.name = name;
        this.description = description;
        setProjectId(projectId);
        setPath(path);
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Scenario addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public Scenario removeTag(String tag) {
        this.tags.remove(tag);
        return this;
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Scenario addStep(Step step) {
        this.steps.add(step);
        return this;
    }

    public Scenario removeStep(Step step) {
        this.steps.remove(step);
        return this;
    }

    public String[][] getExamples() {
        return this.examples;
    }

    public void setExamples(String[][] examples) {
        this.examples = examples;
    }

    @Override
    public String toString() {
        return String.format("Scenario[id=%s, name='%s']", this.id,
                this.name);
    }

}