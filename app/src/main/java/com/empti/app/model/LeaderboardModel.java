package com.empti.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LeaderboardModel {

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


    public class AllLeader {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("total_pickups")
        @Expose
        private Integer totalPickups;
        @SerializedName("total_returns")
        @Expose
        private Integer totalReturns;
        @SerializedName("total_streaks")
        @Expose
        private Integer totalStreaks;
        @SerializedName("lastBadge")
        @Expose
        private LastBadge lastBadge;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public LastBadge getLastBadge() {
            return lastBadge;
        }

        public void setLastBadge(LastBadge lastBadge) {
            this.lastBadge = lastBadge;
        }

    }


    public class Data {

        @SerializedName("logged_in_user")
        @Expose
        private LoggedInUser loggedInUser;
        @SerializedName("all_leaders")
        @Expose
        private List<AllLeader> allLeaders = null;
        @SerializedName("next_page_url")
        @Expose
        private String nextPageUrl;

        public LoggedInUser getLoggedInUser() {
            return loggedInUser;
        }

        public void setLoggedInUser(LoggedInUser loggedInUser) {
            this.loggedInUser = loggedInUser;
        }

        public List<AllLeader> getAllLeaders() {
            return allLeaders;
        }

        public void setAllLeaders(List<AllLeader> allLeaders) {
            this.allLeaders = allLeaders;
        }

        public String getNextPageUrl() {
            return nextPageUrl;
        }

        public void setNextPageUrl(String nextPageUrl) {
            this.nextPageUrl = nextPageUrl;
        }

    }


    public class LastBadge {

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


    public class LoggedInUser {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("total_pickups")
        @Expose
        private Integer totalPickups;
        @SerializedName("total_returns")
        @Expose
        private Integer totalReturns;
        @SerializedName("total_streaks")
        @Expose
        private Integer totalStreaks;
        @SerializedName("lastBadge")
        @Expose
        private LastBadge lastBadge;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public LastBadge getLastBadge() {
            return lastBadge;
        }

        public void setLastBadge(LastBadge lastBadge) {
            this.lastBadge = lastBadge;
        }

    }

}


//
//public class LeaderboardModel {
//
//    @SerializedName("status")
//    @Expose
//    private Integer status;
//    @SerializedName("data")
//    @Expose
//    private Data data;
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public Data getData() {
//        return data;
//    }
//
//    public void setData(Data data) {
//        this.data = data;
//    }
//
//
//    public class AllLeader {
//
//        @SerializedName("id")
//        @Expose
//        private Integer id;
//        @SerializedName("first_name")
//        @Expose
//        private String firstName;
//        @SerializedName("last_name")
//        @Expose
//        private String lastName;
//        @SerializedName("image")
//        @Expose
//        private String image;
//        @SerializedName("total_pickups")
//        @Expose
//        private Integer totalPickups;
//        @SerializedName("total_returns")
//        @Expose
//        private Integer totalReturns;
//        @SerializedName("total_streaks")
//        @Expose
//        private Integer totalStreaks;
//        @SerializedName("lastBadge")
//        @Expose
//        private Object lastBadge;
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public String getFirstName() {
//            return firstName;
//        }
//
//        public void setFirstName(String firstName) {
//            this.firstName = firstName;
//        }
//
//        public String getLastName() {
//            return lastName;
//        }
//
//        public void setLastName(String lastName) {
//            this.lastName = lastName;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public Integer getTotalPickups() {
//            return totalPickups;
//        }
//
//        public void setTotalPickups(Integer totalPickups) {
//            this.totalPickups = totalPickups;
//        }
//
//        public Integer getTotalReturns() {
//            return totalReturns;
//        }
//
//        public void setTotalReturns(Integer totalReturns) {
//            this.totalReturns = totalReturns;
//        }
//
//        public Integer getTotalStreaks() {
//            return totalStreaks;
//        }
//
//        public void setTotalStreaks(Integer totalStreaks) {
//            this.totalStreaks = totalStreaks;
//        }
//
//        public Object getLastBadge() {
//            return lastBadge;
//        }
//
//        public void setLastBadge(Object lastBadge) {
//            this.lastBadge = lastBadge;
//        }
//
//    }
//
//    public class Data {
//
//        @SerializedName("logged_in_user")
//        @Expose
//        private LoggedInUser loggedInUser;
//        @SerializedName("all_leaders")
//        @Expose
//        private List<AllLeader> allLeaders = null;
//        @SerializedName("next_page_url")
//        @Expose
//        private String nextPageUrl;
//
//        public LoggedInUser getLoggedInUser() {
//            return loggedInUser;
//        }
//
//        public void setLoggedInUser(LoggedInUser loggedInUser) {
//            this.loggedInUser = loggedInUser;
//        }
//
//        public List<AllLeader> getAllLeaders() {
//            return allLeaders;
//        }
//
//        public void setAllLeaders(List<AllLeader> allLeaders) {
//            this.allLeaders = allLeaders;
//        }
//
//        public String getNextPageUrl() {
//            return nextPageUrl;
//        }
//
//        public void setNextPageUrl(String nextPageUrl) {
//            this.nextPageUrl = nextPageUrl;
//        }
//
//    }
//
//    public class LastBadge {
//
//        @SerializedName("id")
//        @Expose
//        private Integer id;
//        @SerializedName("type")
//        @Expose
//        private String type;
//        @SerializedName("image")
//        @Expose
//        private String image;
//        @SerializedName("value")
//        @Expose
//        private Integer value;
//        @SerializedName("created_at")
//        @Expose
//        private String createdAt;
//        @SerializedName("updated_at")
//        @Expose
//        private String updatedAt;
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public Integer getValue() {
//            return value;
//        }
//
//        public void setValue(Integer value) {
//            this.value = value;
//        }
//
//        public String getCreatedAt() {
//            return createdAt;
//        }
//
//        public void setCreatedAt(String createdAt) {
//            this.createdAt = createdAt;
//        }
//
//        public String getUpdatedAt() {
//            return updatedAt;
//        }
//
//        public void setUpdatedAt(String updatedAt) {
//            this.updatedAt = updatedAt;
//        }
//
//    }
//
//
//    public class LoggedInUser {
//
//        @SerializedName("id")
//        @Expose
//        private Integer id;
//        @SerializedName("first_name")
//        @Expose
//        private String firstName;
//        @SerializedName("last_name")
//        @Expose
//        private String lastName;
//        @SerializedName("image")
//        @Expose
//        private String image;
//        @SerializedName("total_pickups")
//        @Expose
//        private Integer totalPickups;
//        @SerializedName("total_returns")
//        @Expose
//        private Integer totalReturns;
//        @SerializedName("total_streaks")
//        @Expose
//        private Integer totalStreaks;
//        @SerializedName("lastBadge")
//        @Expose
//        private LastBadge lastBadge;
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public String getFirstName() {
//            return firstName;
//        }
//
//        public void setFirstName(String firstName) {
//            this.firstName = firstName;
//        }
//
//        public String getLastName() {
//            return lastName;
//        }
//
//        public void setLastName(String lastName) {
//            this.lastName = lastName;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public Integer getTotalPickups() {
//            return totalPickups;
//        }
//
//        public void setTotalPickups(Integer totalPickups) {
//            this.totalPickups = totalPickups;
//        }
//
//        public Integer getTotalReturns() {
//            return totalReturns;
//        }
//
//        public void setTotalReturns(Integer totalReturns) {
//            this.totalReturns = totalReturns;
//        }
//
//        public Integer getTotalStreaks() {
//            return totalStreaks;
//        }
//
//        public void setTotalStreaks(Integer totalStreaks) {
//            this.totalStreaks = totalStreaks;
//        }
//
//        public LastBadge getLastBadge() {
//            return lastBadge;
//        }
//
//        public void setLastBadge(LastBadge lastBadge) {
//            this.lastBadge = lastBadge;
//        }
//
//    }
//}