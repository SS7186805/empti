package com.empti.app.model;

import java.util.ArrayList;

public class OutletData {

    public String name = "";
    public String photo = "";
    public String container_id = "";
    public String total_container = "";

    public ArrayList<DetailModel> getModelArr() {
        return modelArr;
    }

    public OutletData(String name, String photo, String container_id, String total_container, ArrayList<DetailModel> modelArr) {
        this.name = name;
        this.photo = photo;
        this.container_id = container_id;
        this.total_container = total_container;
        this.modelArr = modelArr;
    }

    public void setModelArr(ArrayList<DetailModel> modelArr) {
        this.modelArr = modelArr;
    }

    ArrayList<DetailModel> modelArr = new ArrayList<>();

    public String getName() {
        return name;
    }

    public OutletData(String name, String photo, String container_id, String total_container) {
        this.name = name;
        this.photo = photo;
        this.container_id = container_id;
        this.total_container = total_container;
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

    public String getContainer_id() {
        return container_id;
    }

    public void setContainer_id(String container_id) {
        this.container_id = container_id;
    }

    public String getTotal_container() {
        return total_container;
    }

    public void setTotal_container(String total_container) {
        this.total_container = total_container;
    }
}
