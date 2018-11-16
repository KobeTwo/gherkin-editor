package de.gherkineditor.model;

public class Step {


    public enum TYPE {
        GIVEN, WHEN, THEN, AND, BUT;
    }

    private TYPE type;
    private String text;
    private String docstring;

    private String[][] datatable;

    private Step() {
    }

    public Step(TYPE type, String text) {
        this(type, text, null, null);
    }

    public Step(TYPE type, String text, String[][] datatable) {
        this(type, text, null, datatable);
    }

    public Step(TYPE type, String text, String docstring) {
        this(type, text, docstring, null);
    }

    public Step(TYPE type, String text, String docstring, String[][] datatable) {
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

    public String[][] getDatatable() {
        return this.datatable;
    }

    public void setDatatable(String[][] datatable) {
        this.datatable = datatable;
    }
}
