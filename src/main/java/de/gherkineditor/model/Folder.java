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
    private String fileName;


    private Folder() {
    }

    public Folder(String projectId, String path, String fileName) {

        this.fileName = fileName;
        this.setPath(path);
        setProjectId(projectId);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
                this.fileName);
    }

}