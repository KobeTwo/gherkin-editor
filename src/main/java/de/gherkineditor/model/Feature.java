package de.gherkineditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "feature", type = "feature", shards = 1, replicas = 0, refreshInterval = "-1")
public class Feature extends AbstractPathItem {

    @Id
    private String id;

    private String name;

    @MultiField(
            mainField = @Field(type = FieldType.Text, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword)
            }
    )
    private String fileName;

    public Feature(String name, String fileName, String path, String projectId) {
        this.name = name;
        this.fileName = fileName;
        setPath(path);
        this.setProjectId(projectId);
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

    @Override
    public String toString() {
        return String.format("Folder[id=%s, name='%s']", this.id,
                this.name);
    }

}