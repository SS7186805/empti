package com.empti.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BatchesModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("shop_name")
        @Expose
        private String shopName;
        @SerializedName("shop_address")
        @Expose
        private String shopAddress;
        @SerializedName("shop_logo")
        @Expose
        private String shopLogo;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("phone_no")
        @Expose
        private String phoneNo;
        @SerializedName("date_of_birth")
        @Expose
        private String dateOfBirth;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("google_token")
        @Expose
        private String googleToken;
        @SerializedName("facebook_token")
        @Expose
        private String facebookToken;
        @SerializedName("apple_token")
        @Expose
        private String appleToken;
        @SerializedName("forgotpassword_token")
        @Expose
        private String forgotpasswordToken;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("total_pickups")
        @Expose
        private Integer totalPickups;
        @SerializedName("total_returns")
        @Expose
        private Integer totalReturns;
        @SerializedName("total_streaks")
        @Expose
        private Integer totalStreaks;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("scans")
        @Expose
        private List<Scan> scans = null;
        @SerializedName("returns")
        @Expose
        private List<Return> returns = null;
        @SerializedName("streaks")
        @Expose
        private List<Streak> streaks = null;
        @SerializedName("next_return_batch")
        @Expose
        private NextReturnBatch nextReturnBatch;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getGoogleToken() {
            return googleToken;
        }

        public void setGoogleToken(String googleToken) {
            this.googleToken = googleToken;
        }

        public String getFacebookToken() {
            return facebookToken;
        }

        public void setFacebookToken(String facebookToken) {
            this.facebookToken = facebookToken;
        }

        public String getAppleToken() {
            return appleToken;
        }

        public void setAppleToken(String appleToken) {
            this.appleToken = appleToken;
        }

        public String getForgotpasswordToken() {
            return forgotpasswordToken;
        }

        public void setForgotpasswordToken(String forgotpasswordToken) {
            this.forgotpasswordToken = forgotpasswordToken;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
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

        public Integer getTotalPickups() {
            return totalPickups;
        }

        public void setTotalPickups(Integer totalPickups) {
            this.totalPickups = totalPickups;
        }

        public Integer getTotalReturns() {
            return totalReturns;
        }

        public void setTotalReturns(Integer totalReturns) {
            this.totalReturns = totalReturns;
        }

        public Integer getTotalStreaks() {
            return totalStreaks;
        }

        public void setTotalStreaks(Integer totalStreaks) {
            this.totalStreaks = totalStreaks;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<Scan> getScans() {
            return scans;
        }

        public void setScans(List<Scan> scans) {
            this.scans = scans;
        }

        public List<Return> getReturns() {
            return returns;
        }

        public void setReturns(List<Return> returns) {
            this.returns = returns;
        }

        public List<Streak> getStreaks() {
            return streaks;
        }

        public void setStreaks(List<Streak> streaks) {
            this.streaks = streaks;
        }

        public NextReturnBatch getNextReturnBatch() {
            return nextReturnBatch;
        }

        public void setNextReturnBatch(NextReturnBatch nextReturnBatch) {
            this.nextReturnBatch = nextReturnBatch;
        }

    }

    public class NextReturnBatch {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("value")
        @Expose
        private Integer value;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

    public class Return {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("value")
        @Expose
        private Integer value;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("is_occupied")
        @Expose
        private Integer isOccupied;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Integer getIsOccupied() {
            return isOccupied;
        }

        public void setIsOccupied(Integer isOccupied) {
            this.isOccupied = isOccupied;
        }

    }

    public class Scan {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("value")
        @Expose
        private Integer value;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("is_occupied")
        @Expose
        private Integer isOccupied;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Integer getIsOccupied() {
            return isOccupied;
        }

        public void setIsOccupied(Integer isOccupied) {
            this.isOccupied = isOccupied;
        }

    }

    public class Streak {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("value")
        @Expose
        private Integer value;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("is_occupied")
        @Expose
        private Integer isOccupied;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Integer getIsOccupied() {
            return isOccupied;
        }

        public void setIsOccupied(Integer isOccupied) {
            this.isOccupied = isOccupied;
        }

    }
}
