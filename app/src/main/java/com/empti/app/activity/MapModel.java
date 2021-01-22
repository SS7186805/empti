package com.empti.app.activity;

import java.util.List;

public class MapModel {

    /**
     * status : 1
     * data : [{"id":68,"shop_name":"Christopher's Dining","shop_address":"Industrial Area, C-104, Industraial Area, Phase 7, Mohali, Punjab, India, Mohali, Punjab, India","shop_logo":"shop-profile-pic0.53220400_1562223307.png","latitude":"76.700356","longitude":"30.723902","role":"Outlet","distance":0.6123092463835945},{"id":67,"shop_name":"My Apple's Cafe","shop_address":"Sector 34 C, Chandigarh, Chandigarh, India","shop_logo":"shop-profile-pic0.49240300_1562223140.png","latitude":"76.766759","longitude":"30.717522","role":"Outlet","distance":7.432045922556534}]
     */

    private int status;
    private List<DataBean> data;
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public List<DataBean> getData() {
        return data;
    }
    public void setData(List<DataBean> data) {
        this.data = data;
    }
    public static class DataBean {
        /**
         * id : 68
         * shop_name : Christopher's Dining
         * shop_address : Industrial Area, C-104, Industraial Area, Phase 7, Mohali, Punjab, India, Mohali, Punjab, India
         * shop_logo : shop-profile-pic0.53220400_1562223307.png
         * latitude : 76.700356
         * longitude : 30.723902
         * role : Outlet
         * distance : 0.6123092463835945
         */

        private int id;
        private String shop_name;
        private String shop_address;
        private String shop_logo;
        private String latitude;
        private String longitude;
        private String role;
        private double distance;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getShop_name() {
            return shop_name;
        }
        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }
        public String getShop_address() {
            return shop_address;
        }
        public void setShop_address(String shop_address) {
            this.shop_address = shop_address;
        }
        public String getShop_logo() {
            return shop_logo;
        }
        public void setShop_logo(String shop_logo) {
            this.shop_logo = shop_logo;
        }
        public String getLatitude() {
            return latitude;
        }
        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
        public String getLongitude() {
            return longitude;
        }
        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public double getDistance() {
            return distance;
        }
        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}


