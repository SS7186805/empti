package com.empti.app.model;

public class ContainerListModel {
    public String name = "";
    public String photo = "";
    public String id = "";

    public ContainerListModel(String name, String photo, String id, boolean isSelected) {
        this.name = name;
        this.photo = photo;
        this.id = id;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected;
}
