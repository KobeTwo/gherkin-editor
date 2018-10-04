package de.gherkineditor.dto;

import de.gherkineditor.model.AbstractPathItem;

import java.util.ArrayList;
import java.util.List;

public class FolderStructureItem {

    public enum TYPE {
        FOLDER, FEATURE
    }

    private AbstractPathItem model;

    private TYPE type;

    private List<FolderStructureItem> childFolders = new ArrayList<>();

    public AbstractPathItem getModel() {
        return this.model;
    }

    public void setModel(AbstractPathItem model) {
        this.model = model;
    }

    public List<FolderStructureItem> getChildFolders() {
        return this.childFolders;
    }

    public void setChildFolders(List<FolderStructureItem> childFolders) {
        this.childFolders = childFolders;
    }

    public TYPE getType() {
        return this.type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
