package com.alchemistdigital.buxa.model;

/**
 * Created by user on 10/10/2016.
 */
public class CFSAddressModel {
    private int cfsAddressId ;
    private int serverId ;
    private String name ;
    private int status ;
    private String createdAt ;

    @Override
    public String toString() {
        return this.name ;
    }

    public CFSAddressModel() {
    }

    public CFSAddressModel(int serverId, String name, int status) {
        this.serverId = serverId;
        this.name = name;
        this.status = status;
    }

    public int getCfsAddressId() {
        return cfsAddressId;
    }

    public void setCfsAddressId(int cfsAddressId) {
        this.cfsAddressId = cfsAddressId;
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
