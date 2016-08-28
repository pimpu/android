package com.alchemistdigital.buxa.model;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class TransportServiceModel {
    private int transServiceId ;
    private int serverId ;
    private String name ;
    private int status ;
    private String createdAt ;

    public TransportServiceModel() {
    }

    public TransportServiceModel(int serverId, String name, int status, String createdAt) {
        this.serverId = serverId;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getTransServiceId() {
        return transServiceId;
    }

    public void setTransServiceId(int transServiceId) {
        this.transServiceId = transServiceId;
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
