package de.gherkineditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "feature", type = "feature", shards = 1, replicas = 0, refreshInterval = "-1")
public class Feature extends AbstractPathItem {

    @Id
    private String id;

    private String name;

    private String description;

    @Field(type = FieldType.Keyword)
    private List<String> tags = new ArrayList<>();

    @Field(type = FieldType.Nested)
    private List<Step> backgroundSteps = new ArrayList<>();

    @MultiField(
            mainField = @Field(type = FieldType.Text, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword)
            }
    )
    private String fileName;

    public Feature(String projectId, String path, String fileName, String name, String description) {
        this.name = name;
        this.fileName = fileName;
        setPath(path);
        this.setProjectId(projectId);
        this.description = description;
    }

    private Feature() {

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

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public Feature addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public Feature removeTag(String tag) {
        this.tags.remove(tag);
        return this;
    }

    public List<Step> getBackgroundSteps() {
        return this.backgroundSteps;
    }

    public void setBackgroundSteps(List<Step> backgroundSteps) {
        this.backgroundSteps = backgroundSteps;
    }

    public void addBackgroundSteps(Step step) {
        this.backgroundSteps.add(step);
    }

    public void removeBackgroundSteps(Step step) {
        this.backgroundSteps.remove(step);
    }

    @Override
    public String toString() {
        return String.format("Folder[id=%s, name='%s']", this.id,
                this.name);
    }

}