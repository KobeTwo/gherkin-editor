package de.gherkineditor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "folder", type = "folder", shards = 1, replicas = 0, refreshInterval = "-1")
public class Folder extends AbstractPathItem {

    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword)
            }
    )
    private String name;


    private Folder() {
    }

    public Folder(String name, String path, String projectId) {

        this.name = name;
        this.setPath(path);
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


    @Override
    public String toString() {
        return String.format("Folder[id=%s, name='%s']", this.id,
                this.name);
    }

}