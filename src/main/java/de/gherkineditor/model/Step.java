package de.gherkineditor.model;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

public class Step {


    public enum TYPE {
        Given, When, Then, And, But;
    }

    private TYPE type;
    @MultiField(
            mainField = @Field(type = FieldType.Text, store = true, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword)
            }
    )
    private String text;
    private String docstring;

    private String datatable;

    private Step() {
    }

    public Step(TYPE type, String text) {
        this(type, text, null, null);
    }

    public Step(TYPE type, String text, String docstring, String datatable) {
        this.type = type;
        this.text = text;
        this.docstring = docstring;
        this.datatable = datatable;
    }

    public TYPE getType() {
        return this.type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }


    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDocstring() {
        return this.docstring;
    }

    public void setDocstring(String docstring) {
        this.docstring = docstring;
    }

    public String getDatatable() {
        return this.datatable;
    }

    public void setDatatable(String datatable) {
        this.datatable = datatable;
    }
}
