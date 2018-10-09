package de.gherkineditor.dto;

import de.gherkineditor.model.AbstractPathItem;

import java.util.ArrayList;
import java.util.List;

public class FolderStructureItem {

    public enum TYPE {
        FOLDER, FEATURE, SCENARIO
    }

    private AbstractPathItem model;

    private TYPE type;

    private List<FolderStructureItem> children = new ArrayList<>();

    public AbstractPathItem getModel() {
        return this.model;
    }

    public void setModel(AbstractPathItem model) {
        this.model = model;
    }

    public List<FolderStructureItem> getChildren() {
        return this.children;
    }

    public void setChildren(List<FolderStructureItem> children) {
        this.children = children;
    }

    public TYPE getType() {
        return this.type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
