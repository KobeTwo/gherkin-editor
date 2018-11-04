package de.gherkineditor.model;

public class Step {


    public enum TYPE {
        GIVEN, WHEN, THEN, AND, BUT;
    }

    private TYPE type;
    private String text;

    private Step() {
    }

    public Step(TYPE type, String text) {
        this.type = type;
        this.text = text;
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
}
