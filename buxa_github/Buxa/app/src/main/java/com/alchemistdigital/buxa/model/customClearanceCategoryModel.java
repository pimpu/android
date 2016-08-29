package com.alchemistdigital.buxa.model;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class CustomClearanceCategoryModel {
    private int cccId ;
    private int serverId ;
    private String name ;
    private int status ;
    private String createdAt ;

    public CustomClearanceCategoryModel() {
    }

    public CustomClearanceCategoryModel(int serverId, String name, int status, String createdAt) {
        this.serverId = serverId;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getCccId() {
        return cccId;
    }

    public void setCccId(int cccId) {
        this.cccId = cccId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
