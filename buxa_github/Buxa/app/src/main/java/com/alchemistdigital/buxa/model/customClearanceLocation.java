package com.alchemistdigital.buxa.model;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class CustomClearanceLocation {
    private int cclId ;
    private int serverId ;
    private int ccCategoryId ;
    private String name ;
    private String location ;
    private String state ;
    private int status ;
    private String createdAt ;

    public CustomClearanceLocation() {
    }

    public CustomClearanceLocation(int serverId, int ccCategoryId, String name, String location, String state, int status) {
        this.serverId = serverId;
        this.ccCategoryId = ccCategoryId;
        this.name = name;
        this.location = location;
        this.state = state;
        this.status = status;
    }

    @Override
    public String toString() {
        return this.name+", "+this.location+" ("+(this.state).trim()+") ";
    }

    public int getCclId() {
        return cclId;
    }

    public void setCclId(int cclId) {
        this.cclId = cclId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getCcCategoryId() {
        return ccCategoryId;
    }

    public void setCcCategoryId(int ccCategoryId) {
        this.ccCategoryId = ccCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
