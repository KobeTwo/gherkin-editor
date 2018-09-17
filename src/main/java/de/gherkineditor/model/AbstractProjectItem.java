package de.gherkineditor.model;

public abstract class AbstractProjectItem {
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
