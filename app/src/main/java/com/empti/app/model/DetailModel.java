package com.empti.app.model;

public class DetailModel {
    public String outlet_containers_id = "";
    public String deposit = "";
    public String fee = "";
    public String status = "";
    public String modified_at = "";
    public String picked_date = "";
    public String returned_date = "";
    public String name = "";
    public String photo = "";


    public DetailModel(String outlet_containers_id, String deposit, String fee, String status, String modified_at, String picked_date, String returned_date, String name,
                       String photo, boolean isReady, String qrcode, String containers_id) {
        this.outlet_containers_id = outlet_containers_id;
        this.deposit = deposit;
        this.fee = fee;
        this.status = status;
        this.modified_at = modified_at;
        this.picked_date = picked_date;
        this.returned_date = returned_date;
        this.name = name;
        this.photo = photo;
        this.isReady = isReady;
        this.qrcode = qrcode;
        this.containers_id = containers_id;
    }

    public DetailModel(String outlet_containers_id, String deposit, String fee, String status,
                       String modified_at, String picked_date, String returned_date,
                       String name, String photo, String shop_name, String shop_image,
                       String shop_lat, String shop_lng, String qrcode, String containers_id) {
        this.outlet_containers_id = outlet_containers_id;
        this.deposit = deposit;
        this.fee = fee;
        this.status = status;
        this.modified_at = modified_at;
        this.picked_date = picked_date;
        this.returned_date = returned_date;
        this.name = name;
        this.photo = photo;
        this.shop_name = shop_name;
        this.shop_image = shop_image;
        this.shop_lat = shop_lat;
        this.shop_lng = shop_lng;
        this.qrcode = qrcode;
        this.containers_id = containers_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_lat() {
        return shop_lat;
    }

    public void setShop_lat(String shop_lat) {
        this.shop_lat = shop_lat;
    }

    public String getShop_lng() {
        return shop_lng;
    }

    public void setShop_lng(String shop_lng) {
        this.shop_lng = shop_lng;
    }

    public String shop_name = "";
    public String shop_image = "";
    public String shop_lat = "";
    public String shop_lng = "";

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isReady;

    public DetailModel(String outlet_containers_id, String deposit, String fee, String status,
                       String modified_at, String picked_date, String returned_date,
                       String name, String photo, String qrcode, String containers_id) {
        this.outlet_containers_id = outlet_containers_id;
        this.deposit = deposit;
        this.fee = fee;
        this.status = status;
        this.modified_at = modified_at;
        this.picked_date = picked_date;
        this.returned_date = returned_date;
        this.name = name;
        this.photo = photo;
        this.qrcode = qrcode;
        this.containers_id = containers_id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String qrcode = "";

    public String getContainers_id() {
        return containers_id;
    }

    public void setContainers_id(String containers_id) {
        this.containers_id = containers_id;
    }

    public String containers_id = "";

    public DetailModel(String outlet_containers_id, String deposit,
                       String fee, String status, String modified_at, String picked_date,
                       String returned_date, String name, String photo) {
        this.outlet_containers_id = outlet_containers_id;
        this.deposit = deposit;
        this.fee = fee;
        this.status = status;
        this.modified_at = modified_at;
        this.picked_date = picked_date;
        this.returned_date = returned_date;
        this.name = name;
        this.photo = photo;
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

    public String getOutlet_containers_id() {
        return outlet_containers_id;
    }

    public DetailModel(String outlet_containers_id, String deposit, String fee, String status,
                       String modified_at, String picked_date, String returned_date) {
        this.outlet_containers_id = outlet_containers_id;
        this.deposit = deposit;
        this.fee = fee;
        this.status = status;
        this.modified_at = modified_at;
        this.picked_date = picked_date;
        this.returned_date = returned_date;
    }

    public void setOutlet_containers_id(String outlet_containers_id) {
        this.outlet_containers_id = outlet_containers_id;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getPicked_date() {
        return picked_date;
    }

    public void setPicked_date(String picked_date) {
        this.picked_date = picked_date;
    }

    public String getReturned_date() {
        return returned_date;
    }

    public void setReturned_date(String returned_date) {
        this.returned_date = returned_date;
    }
}
