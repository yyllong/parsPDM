package com.pdm;

public class Reference {
    private String FKId;
    private String parentId;
    public String getFKId() {
        return FKId;
    }
    public void setFKId(String fKId) {
        FKId = fKId;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}